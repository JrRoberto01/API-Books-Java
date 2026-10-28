package com.example.bibliotecaapi.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record BookId(UUID id) {
    public BookId {
        Assert.notNull(id, "O identificador do livro nao pode ser nulo");
    }

    public BookId() {
        this(UUID.randomUUID());
    }
}
