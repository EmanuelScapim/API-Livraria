package io.github.emanuelscapim.libraryapi.repository;

import io.github.emanuelscapim.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
