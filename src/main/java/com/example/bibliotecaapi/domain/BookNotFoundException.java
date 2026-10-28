package com.example.bibliotecaapi.domain;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(BookId bookId) {
        super("Livro com identificador " + bookId.id() + " nao encontrado");
    }
}
