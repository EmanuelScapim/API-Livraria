package io.github.emanuelscapim.libraryapi.controller.dto;

import io.github.emanuelscapim.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID idDto,
        String nomeDto,
        LocalDate dataNascimentoDto,
        String nacionalidadeDto) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(this.nomeDto);
        autor.setDatNascimento(this.dataNascimentoDto);
        autor.setNacionalidade(this.nacionalidadeDto);
        return autor;
    }
}
