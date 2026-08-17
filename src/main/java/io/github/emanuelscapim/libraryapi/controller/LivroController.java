package io.github.emanuelscapim.libraryapi.controller;


import io.github.emanuelscapim.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.emanuelscapim.libraryapi.controller.dto.ErroResposta;
import io.github.emanuelscapim.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.emanuelscapim.libraryapi.controller.mappers.LivroMapper;
import io.github.emanuelscapim.libraryapi.exceptions.RegistroDublicadoException;
import io.github.emanuelscapim.libraryapi.model.Livro;
import io.github.emanuelscapim.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Void> cadastrarLivro(@RequestBody @Valid CadastroLivroDTO dto){
            Livro livroEntidade = mapper.toEntity(dto);
            livroService.cadastrarLivro(livroEntidade);
            var url = gerarHeaderLocation(livroEntidade.getId());
            return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id ){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro ->{
                    var dto = mapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
