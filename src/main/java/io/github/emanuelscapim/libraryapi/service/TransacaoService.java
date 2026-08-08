package io.github.emanuelscapim.libraryapi.service;


import io.github.emanuelscapim.libraryapi.model.Autor;
import io.github.emanuelscapim.libraryapi.model.Livro;
import io.github.emanuelscapim.libraryapi.model.enums.GeneroLivro;
import io.github.emanuelscapim.libraryapi.repository.AutorRepository;
import io.github.emanuelscapim.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void atualizacaoSemAtualizar(){
        var livro = livroRepository.findById(UUID.fromString("0fa66131-144e-48c1-ac31-e1b90a694944"))
                .orElse(null);

        livro.setDataPublicacao(LocalDate.of(1901,6,1));
    }

    @Transactional
    public void executar(){

        // salva o autor
        Autor autor = new Autor();
        autor.setNome("Teste Abacaxi");
        autor.setNacionalidade("Brasileiro");
        autor.setDatNascimento(LocalDate.of(1880, 2,1));

        // salva o livro
        Livro livro = new Livro();
        livro.setIsbn("90887-84874");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setTitulo("Teste A vida das briófitas");
        livro.setDataPublicacao(LocalDate.of(1900, 2, 1));



        autorRepository.save(autor);

        livro.setAutor(autor);

        livroRepository.save(livro);

        if(autor.getNome().equals("Teste Abacaxi")){
            throw new RuntimeException("Rollback");
        }

    }
}
