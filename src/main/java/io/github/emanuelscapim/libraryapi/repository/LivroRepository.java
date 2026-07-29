package io.github.emanuelscapim.libraryapi.repository;

import io.github.emanuelscapim.libraryapi.model.Autor;
import io.github.emanuelscapim.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    //Querry method
    // select * from tb_livro where id_autor = id
    List<Livro> findByAutor(Autor autor);

    // select * from tb_livro where titulo = titulo
    List<Livro> findByTitulo(String titulo);

    // select * from tb_livro where isbn = isbn
    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);
}
