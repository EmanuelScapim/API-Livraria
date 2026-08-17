package io.github.emanuelscapim.libraryapi.controller.dto;

import io.github.emanuelscapim.libraryapi.model.enums.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResultadoPesquisaLivroDTO(String isbnDTO,
                                        String tituloDTO,
                                        LocalDate dataPublicacaoDTO,
                                        GeneroLivro generoDTO,
                                        BigDecimal precoDTO,
                                        AutorDTO autorDTO) {
}
