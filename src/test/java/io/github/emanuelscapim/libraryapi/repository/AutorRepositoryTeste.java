package io.github.emanuelscapim.libraryapi.repository;

import io.github.emanuelscapim.libraryapi.model.Autor;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTeste {

    @Autowired
    AutorRepository resposiory;

    @Test
    public void salvarTeste(){
        Autor autor = new Autor();
        autor.setNome("José");
        autor.setNacionalidade("Brasileira");
        autor.setDatNascimento(LocalDate.of(1950, 1, 20));

        var autorSalvo = resposiory.save(autor);
        System.out.println("Autor salvo" + autorSalvo);
    }

    @Test
    public void atualizarTest() {
        var id = UUID.fromString("23727f06-d751-4149-b863-5b58b2d78916");

        Optional<Autor> possivelAutor = resposiory.findById(id);

        if(possivelAutor.isPresent()){
            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do autor");
            System.out.println(possivelAutor.get());

            autorEncontrado.setDatNascimento(LocalDate.of(1960, 1, 30));
            resposiory.save(autorEncontrado);
        }
    }

    @Test
    public void listarTeste(){
        List<Autor> list = resposiory.findAll();

        list.forEach(System.out::println);
    }

    @Test
    public void countTest(){
        System.out.println("Contagem de autores: " + resposiory.count());
    }

    @Test
    public void deletePorIdTest(){
        var id = UUID.fromString("23727f06-d751-4149-b863-5b58b2d78916");
        resposiory.deleteById(id);
    }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("9575ab8b-f858-4024-8e14-e29d7356a8b7");
        var maria = resposiory.findById(id).get();
        resposiory.delete(maria);
    }
}
