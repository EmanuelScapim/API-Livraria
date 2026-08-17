package io.github.emanuelscapim.libraryapi.controller.mappers;

import io.github.emanuelscapim.libraryapi.controller.dto.AutorDTO;
import io.github.emanuelscapim.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(source = "idDto", target = "id")
    @Mapping(source = "nomeDto", target = "nome")
    @Mapping(source = "dataNascimentoDto", target = "datNascimento")
    @Mapping(source = "nacionalidadeDto", target = "nacionalidade")
    Autor toEntity(AutorDTO dto);

    @Mapping(source = "id", target = "idDto")
    @Mapping(source = "nome", target = "nomeDto")
    @Mapping(source = "datNascimento", target = "dataNascimentoDto")
    @Mapping(source = "nacionalidade", target = "nacionalidadeDto")
    AutorDTO toDTO(Autor autor);
}
