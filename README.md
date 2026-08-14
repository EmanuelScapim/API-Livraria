# API Livraria
 
API REST desenvolvida em Java com Spring Boot para o gerenciamento de uma livraria, contemplando o cadastro de autores e livros. O projeto tem propósito de estudo e aplica conceitos de arquitetura em camadas, boas práticas de desenvolvimento com Spring, persistência de dados com JPA/Hibernate e tratamento centralizado de exceções.
 
## Sumário
 
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Padrões de projeto](#padrões-de-projeto)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Modelo de dados](#modelo-de-dados)
- [Pré-requisitos](#pré-requisitos)
- [Como rodar o projeto](#como-rodar-o-projeto)
- [Configuração da aplicação](#configuração-da-aplicação)
- [Endpoints da API](#endpoints-da-api)
- [Tratamento de erros](#tratamento-de-erros)
- [O que já foi implementado](#o-que-já-foi-implementado)
- [Próximos passos](#próximos-passos)
- [Testes](#testes)
## Tecnologias utilizadas
 
- Java 21
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- Spring Web (MVC)
- Spring Validation (Bean Validation / Jakarta Validation)
- PostgreSQL 16.3
- HikariCP (pool de conexões)
- Lombok
- Maven (com Maven Wrapper)
- Docker e Docker Compose
- pgAdmin 4 (interface de administração do banco de dados)
- JUnit 5 (testes)
## Arquitetura
 
O projeto segue uma **arquitetura em camadas (Layered Architecture / N-Tier)**, típica de APIs REST construídas com Spring Boot. Cada camada possui uma responsabilidade bem definida e se comunica apenas com a camada imediatamente adjacente, o que favorece baixo acoplamento e facilita manutenção e testes:
 
- **Controller** (`controller`): camada de entrada da aplicação. Responsável por expor os endpoints REST, receber as requisições HTTP, validar o payload de entrada (via Bean Validation) e converter entre DTOs e entidades de domínio.
- **DTO** (`controller.dto`): objetos de transporte de dados utilizados para desacoplar o contrato da API (o que trafega na requisição/resposta) das entidades de persistência.
- **Tratamento de exceções da camada web** (`controller.common`): concentra o tratamento global de erros lançados pelos controllers.
- **Service** (`service`): camada de regras de negócio. Orquestra chamadas a repositórios e validadores, define o que pode ou não ser feito com os dados (ex.: impedir a exclusão de um autor que possua livros cadastrados) e delimita as transações.
- **Validator** (`validator`): camada dedicada exclusivamente a regras de validação de negócio (diferente da validação sintática feita pelo Bean Validation nos DTOs), mantendo o `Service` mais enxuto e coeso.
- **Repository** (`repository`): camada de acesso a dados, construída sobre o Spring Data JPA, abstraindo o acesso ao banco de dados relacional.
- **Model** (`model` e `model.enums`): entidades de domínio mapeadas via JPA/Hibernate para as tabelas do banco de dados.
- **Exceptions** (`exceptions`): exceções de domínio específicas da aplicação, que carregam significado de negócio e são mapeadas para códigos de status HTTP apropriados.
- **Config** (`config`): configurações de infraestrutura da aplicação, como o pool de conexões com o banco de dados.
O fluxo padrão de uma requisição segue: **Controller → DTO → Service → Validator/Repository → Model (entidade) → Banco de Dados**, retornando ao final um DTO ou uma resposta de erro padronizada ao cliente.
 
## Padrões de projeto
 
Os seguintes padrões e práticas de projeto são aplicados ao longo do código:
 
- **DTO (Data Transfer Object)**: as classes `AutorDTO`, `ErroResposta` e `ErroCampo` isolam o contrato exposto pela API das entidades JPA, evitando expor detalhes de persistência ao cliente.
- **Repository Pattern**: `AutorRepository` e `LivroRepository` estendem `JpaRepository`, abstraindo a camada de persistência e oferecendo Query Methods e consultas JPQL customizadas.
- **Service Layer Pattern**: `AutorService` concentra as regras de negócio e a orquestração entre validação e persistência, mantendo os controllers responsáveis apenas por questões relacionadas a HTTP.
- **Dependency Injection / Inversion of Control**: uso extensivo de injeção de dependências via construtor, com o auxílio da anotação `@RequiredArgsConstructor` do Lombok e do container IoC do Spring.
- **Validator (Single Responsibility)**: `AutorValidator` isola regras de validação de negócio (por exemplo, impedir o cadastro de autores duplicados) em um componente próprio, separado do `Service`.
- **Exception Handling centralizado (Controller Advice)**: `GlobalExceptionHandler`, anotado com `@RestControllerAdvice`, concentra o tratamento de exceções de validação em um único ponto, evitando repetição de código nos controllers.
- **Exceções de domínio customizadas**: `RegistroDublicadoException` e `OperacaoNaoPermitidaException` representam regras de negócio violadas e são convertidas em respostas HTTP específicas (409 Conflict e 400 Bad Request, respectivamente).
- **Singleton (Spring Beans)**: services, repositories, validators e beans de configuração são gerenciados como singletons pelo container do Spring.
- **Objetos de valor imutáveis**: os DTOs são implementados como `record` do Java, garantindo imutabilidade e reduzindo código repetitivo (boilerplate).
- **Auditoria de entidades (JPA Auditing)**: a entidade `Autor` utiliza `@CreatedDate` e `@LastModifiedDate` em conjunto com `@EnableJpaAuditing`, preenchendo automaticamente os campos de data de cadastro e de atualização.
## Estrutura de pastas
 
```
src
├── main
│   ├── java/io/github/emanuelscapim/libraryapi
│   │   ├── Application.java
│   │   ├── config
│   │   │   └── DatabaseConfig.java
│   │   ├── controller
│   │   │   ├── AutorController.java
│   │   │   ├── common
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── dto
│   │   │       ├── AutorDTO.java
│   │   │       ├── ErroCampo.java
│   │   │       └── ErroResposta.java
│   │   ├── exceptions
│   │   │   ├── OperacaoNaoPermitidaException.java
│   │   │   └── RegistroDublicadoException.java
│   │   ├── model
│   │   │   ├── Autor.java
│   │   │   ├── Livro.java
│   │   │   └── enums
│   │   │       └── GeneroLivro.java
│   │   ├── repository
│   │   │   ├── AutorRepository.java
│   │   │   └── LivroRepository.java
│   │   ├── service
│   │   │   ├── AutorService.java
│   │   │   └── TransacaoService.java
│   │   └── validator
│   │       └── AutorValidator.java
│   └── resources
│       └── application.yaml
└── test
    └── java/io/github/emanuelscapim/libraryapi
        ├── ApplicationTests.java
        └── repository
            ├── AutorRepositoryTeste.java
            ├── LivroRepositoryTest.java
            └── TransacoesTest.java
```
 
## Modelo de dados
 
O domínio é composto por duas entidades principais, com relacionamento de um-para-muitos entre elas:
 
**Autor** (`tb_autor`)
- `id` (UUID, chave primária)
- `nome` (obrigatório)
- `data_nascimento` (obrigatório)
- `nacionalidade` (obrigatório)
- `data_cadastro` e `data_atualizacao` (preenchidos automaticamente via auditoria JPA)
- `id_usuario`
- relacionamento `1:N` com `Livro`
**Livro** (`tb_livro`)
- `id` (UUID, chave primária)
- `isbn` (obrigatório)
- `titulo` (obrigatório)
- `data_publicacao` (obrigatório)
- `genero` (enum `GeneroLivro`: `FICCAO`, `MISTERIO`, `ROMANCE`, `BIOGRAFIA`, `CIENCIA`)
- `preco`
- `id_autor` (chave estrangeira para `Autor`)
## Pré-requisitos
 
Para executar o projeto localmente é necessário ter instalado:
 
- **JDK 21** (o projeto é compilado com `java.version = 21`, definido no `pom.xml`)
- **Docker** e **Docker Compose** (para subir o banco de dados PostgreSQL e o pgAdmin)
- Não é necessário ter o Maven instalado globalmente, pois o projeto já inclui o **Maven Wrapper** (`mvnw` / `mvnw.cmd`)
- Uma IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code, etc.) é opcional, mas recomendada
## Como rodar o projeto
 
1. Clone o repositório:
```bash
git clone https://github.com/EmanuelScapim/API-Livraria.git
cd API-Livraria
```
 
2. Suba os containers do banco de dados com o Docker Compose. Isso iniciará o PostgreSQL na porta `5432` e o pgAdmin na porta `15432`:
```bash
docker compose up -d
```
 
3. Verifique se as credenciais de acesso ao banco em `src/main/resources/application.yaml` correspondem às definidas no `docker-compose.yml` (por padrão, usuário `postgres`, senha `postgres` e banco `librarydb`, já configurados corretamente).
4. Execute a aplicação utilizando o Maven Wrapper:
Linux ou macOS:
```bash
./mvnw spring-boot:run
```
 
Windows:
```bash
mvnw.cmd spring-boot:run
```
 
Alternativamente, é possível executar a classe `Application.java` diretamente pela IDE.
 
5. A aplicação sobe, por padrão, na porta `8080` (porta padrão do Spring Boot, sem sobrescrita no `application.yaml`). A API estará disponível em `http://localhost:8080`.
6. Como o Hibernate está configurado com `ddl-auto: update`, as tabelas do banco de dados são criadas e atualizadas automaticamente a partir das entidades JPA, não sendo necessário executar scripts de migração manualmente.
7. Para inspecionar o banco de dados visualmente, acesse o pgAdmin em `http://localhost:15432` (login: `admin@admin.com` / senha: `admin`) e cadastre um novo servidor apontando para o host `postgres` (nome do serviço no Docker Compose), porta `5432`, usuário `postgres` e senha `postgres`.
## Configuração da aplicação
 
As principais configurações estão em `src/main/resources/application.yaml`:
 
```yaml
spring:
  application:
    name: libraryapi
  datasource:
    url: jdbc:postgresql://localhost:5432/librarydb
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
      properties:
        hibernate:
          org.hibernate.dialect.PostgreSQLDialect
```
 
Além disso, o pool de conexões com o banco de dados é configurado manualmente em `DatabaseConfig.java` utilizando o HikariCP, com os seguintes parâmetros:
 
- Tamanho máximo do pool: 10 conexões
- Tamanho mínimo (idle): 1 conexão
- Tempo de vida máximo de uma conexão: 10 minutos
- Tempo limite de conexão: aproximadamente 1,66 minutos
- Query de teste de validação de conexão: `select 1`
## Endpoints da API
 
Atualmente, a API expõe endpoints apenas para o recurso **Autor**, no caminho base `/autores`.
 
### Cadastrar autor
 
`POST /autores`
 
Corpo da requisição:
 
```json
{
  "nomeDto": "Machado de Assis",
  "dataNascimentoDto": "1839-06-21",
  "nacionalidadeDto": "Brasileira"
}
```
 
Respostas possíveis:
 
| Status | Descrição |
| --- | --- |
| 201 Created | Autor cadastrado com sucesso. O cabeçalho `Location` da resposta contém a URI do recurso criado. |
| 409 Conflict | Já existe um autor cadastrado com o mesmo nome, data de nascimento e nacionalidade. |
| 422 Unprocessable Entity | Erros de validação dos campos obrigatórios. |
 
### Obter detalhes de um autor
 
`GET /autores/{id}`
 
Respostas possíveis:
 
| Status | Descrição |
| --- | --- |
| 200 OK | Retorna os dados do autor. |
| 404 Not Found | Nenhum autor encontrado com o id informado. |
 
Exemplo de resposta (200 OK):
 
```json
{
  "idDto": "23727f06-d751-4149-b863-5b58b2d78916",
  "nomeDto": "Machado de Assis",
  "dataNascimentoDto": "1839-06-21",
  "nacionalidadeDto": "Brasileira"
}
```
 
### Pesquisar autores
 
`GET /autores`
 
Aceita os parâmetros de busca (query params) opcionais:
 
| Parâmetro | Obrigatório | Descrição |
| --- | --- | --- |
| `nome` | Não | Filtra autores pelo nome |
| `nacionalidade` | Não | Filtra autores pela nacionalidade |
 
Caso nenhum parâmetro seja informado, retorna todos os autores cadastrados. Retorna sempre `200 OK` com uma lista (podendo estar vazia).
 
### Atualizar autor
 
`PUT /autores/{id}`
 
Corpo da requisição: mesmo formato do cadastro (`AutorDTO`).
 
Respostas possíveis:
 
| Status | Descrição |
| --- | --- |
| 204 No Content | Autor atualizado com sucesso. |
| 404 Not Found | Nenhum autor encontrado com o id informado. |
| 409 Conflict | A atualização geraria um conflito com outro autor já cadastrado. |
 
### Remover autor
 
`DELETE /autores/{id}`
 
Respostas possíveis:
 
| Status | Descrição |
| --- | --- |
| 204 No Content | Autor removido com sucesso. |
| 404 Not Found | Nenhum autor encontrado com o id informado. |
| 400 Bad Request | Operação não permitida, pois o autor possui livros cadastrados vinculados a ele. |
 
## Tratamento de erros
 
Os erros de validação de campos (Bean Validation) são interceptados globalmente pelo `GlobalExceptionHandler` e retornados no seguinte formato padronizado:
 
```json
{
  "status": 422,
  "mensagem": "Erro de validação.",
  "erros": [
    {
      "campo": "nomeDto",
      "erro": "Campo obrigatório"
    }
  ]
}
```
 
Regras de negócio violadas (como cadastro duplicado ou exclusão não permitida) são tratadas diretamente nos controllers, a partir das exceções customizadas lançadas pela camada de serviço, seguindo o mesmo formato de resposta, porém sem a lista detalhada de erros por campo:
 
```json
{
  "status": 409,
  "mensagem": "Autor já cadastrado",
  "erros": []
}
```
 
## O que já foi implementado
 
- Estrutura base do projeto Spring Boot, incluindo configuração de conexão com PostgreSQL via HikariCP.
- Entidade `Autor`, com mapeamento JPA completo e auditoria automática de datas de cadastro e atualização.
- Entidade `Livro`, com mapeamento JPA, enum de gênero literário e relacionamento `ManyToOne` com `Autor`.
- Repositório `AutorRepository`, com Query Methods para busca por nome, nacionalidade e combinação de ambos.
- Repositório `LivroRepository`, com diversos exemplos de consultas via Query Methods, JPQL com parâmetros nomeados e posicionais, e operações de escrita (`@Modifying`) para exclusão e atualização em massa.
- CRUD completo de autores, exposto via `AutorController`, incluindo cadastro, consulta por id, pesquisa por filtros, atualização e remoção.
- Validação de dados de entrada com Bean Validation (`@NotBlank`, `@NotNull`) nos DTOs.
- Validação de regra de negócio para impedir o cadastro de autores duplicados (`AutorValidator`).
- Regra de negócio que impede a exclusão de um autor que possua livros vinculados.
- Tratamento centralizado de exceções de validação (`GlobalExceptionHandler`) e de exceções de domínio (`RegistroDublicadoException`, `OperacaoNaoPermitidaException`), com respostas HTTP padronizadas.
- Ambiente de banco de dados via Docker Compose, com PostgreSQL e pgAdmin já configurados.
- Testes exploratórios de repositório para validar operações de persistência de autores, livros e cenários de transação (commit e rollback).
## Próximos passos
 
Os itens a seguir ainda não foram implementados e representam a evolução natural do projeto:
 
- `LivroController`, expondo endpoints REST para o CRUD de livros (atualmente existe apenas a entidade e o repositório).
- DTOs, `Service` e `Validator` dedicados ao recurso `Livro`, seguindo o mesmo padrão já aplicado a `Autor`.
- Paginação e ordenação nos endpoints de listagem e pesquisa.
- Documentação interativa da API (Swagger / OpenAPI).
- Camada de autenticação e autorização.
- Testes automatizados com asserções (os testes atuais são majoritariamente exploratórios, utilizados durante o desenvolvimento).
- Perfis de configuração (`application-dev.yaml`, `application-prod.yaml`) para separar ambientes.
## Testes
 
O projeto conta com testes de integração voltados à camada de repositório, localizados em `src/test/java/io/github/emanuelscapim/libraryapi/repository`, utilizados para validar operações de persistência, consultas customizadas e comportamento transacional (incluindo cenários de rollback). Para executá-los:
 
```bash
./mvnw test
```
 
É necessário que o banco de dados definido no `docker-compose.yml` esteja em execução, uma vez que os testes atuais não utilizam um banco de dados isolado (como H2) nem containers descartáveis (Testcontainers).
