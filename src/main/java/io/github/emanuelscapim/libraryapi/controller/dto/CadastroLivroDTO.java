package io.github.emanuelscapim.libraryapi.controller.dto;

import io.github.emanuelscapim.libraryapi.model.Livro;
import io.github.emanuelscapim.libraryapi.model.enums.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(

        @NotBlank(message = "Campo obrigatório")
        @ISBN
        String isbnDTO,
        @NotBlank(message = "Campo obrigatório")
        String tituloDTO,
        @NotNull(message = "Campo obrigatório")
        @Past(message = "Não poed ser uma data futura")
        LocalDate dataPublicacaoDTO,
        GeneroLivro generoDTO,
        BigDecimal precoDTO,
        @NotNull(message = "Campo obrigatório")
        UUID idAutorDTO
        ) {

        public Livro mapearParaLivro(){
                Livro livro = new Livro();
                livro.setIsbn(isbnDTO);
                livro.setTitulo(tituloDTO);
                livro.setDataPublicacao(dataPublicacaoDTO);
                livro.setGenero(generoDTO);
                livro.setPreco(precoDTO);
                return livro;
        }
}
