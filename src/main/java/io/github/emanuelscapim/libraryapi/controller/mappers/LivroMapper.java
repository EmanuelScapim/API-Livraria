package io.github.emanuelscapim.libraryapi.controller.mappers;


import io.github.emanuelscapim.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.emanuelscapim.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.emanuelscapim.libraryapi.model.Livro;
import io.github.emanuelscapim.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    protected AutorRepository autorRepository;

    @Mapping(source = "isbnDTO", target = "isbn")
    @Mapping(source = "tituloDTO", target = "titulo")
    @Mapping(source = "dataPublicacaoDTO", target = "dataPublicacao")
    @Mapping(source = "generoDTO", target = "genero")
    @Mapping(source = "precoDTO", target = "preco")
    @Mapping(expression = "java(autorRepository.findById(dto.idAutorDTO()).orElse(null))", target = "autor")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    @Mapping(source = "isbn", target = "isbnDTO")
    @Mapping(source = "titulo", target = "tituloDTO")
    @Mapping(source = "dataPublicacao", target = "dataPublicacaoDTO")
    @Mapping(source = "genero", target = "generoDTO")
    @Mapping(source = "preco", target = "precoDTO")
    @Mapping(source = "autor", target = "autorDTO")
    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
