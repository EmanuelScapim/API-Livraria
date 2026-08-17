package io.github.emanuelscapim.libraryapi.service;

import io.github.emanuelscapim.libraryapi.model.Livro;
import io.github.emanuelscapim.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    public Livro cadastrarLivro(Livro livro){
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return livroRepository.findById(id);
    }
}
