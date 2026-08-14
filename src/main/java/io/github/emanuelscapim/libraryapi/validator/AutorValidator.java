package io.github.emanuelscapim.libraryapi.validator;

import io.github.emanuelscapim.libraryapi.exceptions.RegistroDublicadoException;
import io.github.emanuelscapim.libraryapi.model.Autor;
import io.github.emanuelscapim.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private AutorRepository repository;

    public AutorValidator(AutorRepository repository) {
        this.repository = repository;
    }

    public void validar(Autor autor){
        if(existeAutorCadastrado(autor)){
            throw new RegistroDublicadoException("Autor já cadastrado");
        }
    }

    // Não deixa cadastrar autores que já existam na base de dados
    private boolean existeAutorCadastrado(Autor autor){
        Optional<Autor> autorEncontrado = repository.findByNomeAndDatNascimentoAndNacionalidade(
                autor.getNome(),
                autor.getDatNascimento(),
                autor.getNacionalidade()
        );

        if(autor.getId() == null){
            return autorEncontrado.isPresent();
        }

        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();
    }
}
