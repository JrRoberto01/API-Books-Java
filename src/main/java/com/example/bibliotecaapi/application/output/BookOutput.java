package com.example.bibliotecaapi.application.output;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.domain.Book;

public record BookOutput(
        String id,
        String titulo,
        String autor,
        Integer anoPublicacao,
        String genero,
        StatusLeitura statusLeitura
) {

    public static BookOutput from(Book book) {
        return new BookOutput(
                book.getId().id().toString(),
                book.getTitulo(),
                book.getAutor(),
                book.getAnoPublicacao(),
                book.getGenero(),
                book.getStatusLeitura()
        );
    }
}
