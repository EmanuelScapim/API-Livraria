package io.github.emanuelscapim.libraryapi.model.teste;

public sealed interface BackendResult
        permits SuccessResult, ErrorResult {

    void body(String nome);
}

