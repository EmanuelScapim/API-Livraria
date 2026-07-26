package io.github.emanuelscapim.libraryapi.repository;

import io.github.emanuelscapim.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
