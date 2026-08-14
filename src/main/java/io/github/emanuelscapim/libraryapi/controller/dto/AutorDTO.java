package io.github.emanuelscapim.libraryapi.controller.dto;

import io.github.emanuelscapim.libraryapi.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID idDto,

        @NotBlank(message = "Campo obrigatório")
        @Size(max = 100, min = 10, message = "Campo fora do padrão")
        String nomeDto,

        @NotNull(message = "Campo obrigatório")
        @Past(message = "A data de nascimento não pode ser uma data futura")
        LocalDate dataNascimentoDto,

        @NotBlank(message = "Campo obrigatório")
        @Size(max = 50, min = 5, message = "Campo fora do tamanho padrão")
        String nacionalidadeDto) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nomeDto);
        autor.setDatNascimento(this.dataNascimentoDto);
        autor.setNacionalidade(this.nacionalidadeDto);
        return autor;
    }
}
