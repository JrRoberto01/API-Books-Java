package com.example.bibliotecaapi.infrastructure.http.response;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.application.output.BookOutput;

public record BookResponse(
        String id,
        String titulo,
        String autor,
        Integer anoPublicacao,
        String genero,
        StatusLeitura statusLeitura
) {

    public static BookResponse from(BookOutput output) {
        return new BookResponse(
                output.id(),
                output.titulo(),
                output.autor(),
                output.anoPublicacao(),
                output.genero(),
                output.statusLeitura()
        );
    }
}
