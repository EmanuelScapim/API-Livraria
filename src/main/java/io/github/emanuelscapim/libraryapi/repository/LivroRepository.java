package io.github.emanuelscapim.libraryapi.repository;

import io.github.emanuelscapim.libraryapi.model.Autor;
import io.github.emanuelscapim.libraryapi.model.Livro;
import io.github.emanuelscapim.libraryapi.model.enums.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @see LivroRepositoryTest
 */


public interface LivroRepository extends JpaRepository<Livro, UUID> {

    //Querry method
    // select * from tb_livro where id_autor = id
    List<Livro> findByAutor(Autor autor);

    // select * from tb_livro where titulo = titulo
    List<Livro> findByTitulo(String titulo);

    // select * from tb_livro where isbn = isbn
    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    //JPQL -> Referencia as entidades e as propriedades
    @Query("select l from Livro as l order by l.titulo, l.preco")
    List<Livro> listarTodosOrdenadosPorTituloAndPreco();

    // select a.* from tb_livro l join autor a on a.id = l.id_autor
    @Query("select a from Livro l join l.autor a ")
    List<Autor> listarAutoresDosLivros();

    // select distinct l.* from tb_livro l
    @Query("select distinct l.titulo from Livro l")
    List<String> listarNomesDiferentesLivros();

    // select distinct l.genero from tb_livro l join tb_autor a on a.id = l.id_autor where a.nacionalidade = 'Brasileira' order by l.genero
    @Query("""
            select l.genero
            from Livro l
            join l.autor a
            where a.nacionalidade = 'Brasileira'
            order by l.genero
            """)
    List<String> listarGenerosAtoresBrasileiros();

    //named paramters -> parametros nomeados
    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao")
    List<Livro> findbyGenero(@Param("genero") GeneroLivro generoLivro, @Param("paramOrdenacao") String nomePropriedade);

    //positional parametrs
    @Query("select l from Livro l where l.genero = ?1 order by ?2")
    List<Livro> findbyGeneroPositionalParametrs(GeneroLivro generoLivro, String nomePropriedade);

    @Transactional
    @Modifying
    @Query("delete from Livro where genero = ?1")
    void deleteByGenero(GeneroLivro generoLivro);


    @Transactional
    @Modifying
    @Query("update Livro set dataPublicacao = ?1 ")
    void updateDatapublicacao(LocalDate novaData);

    boolean existsByAutor(Autor autor);
}
