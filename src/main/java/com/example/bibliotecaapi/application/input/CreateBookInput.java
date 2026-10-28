package com.example.bibliotecaapi.application.input;

import com.example.bibliotecaapi.domain.StatusLeitura;

public record CreateBookInput(
        String titulo,
        String autor,
        Integer anoPublicacao,
        String genero,
        StatusLeitura statusLeitura
) {
}
