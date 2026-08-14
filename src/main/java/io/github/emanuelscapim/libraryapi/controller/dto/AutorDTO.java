package io.github.emanuelscapim.libraryapi.controller.dto;

import io.github.emanuelscapim.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID idDto,

        @NotBlank(message = "Campo obrigatório")
        String nomeDto,

        @NotNull(message = "Campo obrigatório")
        LocalDate dataNascimentoDto,

        @NotBlank(message = "Campo obrigatório")
        String nacionalidadeDto) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nomeDto);
        autor.setDatNascimento(this.dataNascimentoDto);
        autor.setNacionalidade(this.nacionalidadeDto);
        return autor;
    }
}
