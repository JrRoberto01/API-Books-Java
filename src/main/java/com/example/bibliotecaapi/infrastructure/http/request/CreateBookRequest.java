package com.example.bibliotecaapi.infrastructure.http.request;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.application.input.CreateBookInput;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Year;

public record CreateBookRequest(

        @NotBlank(message = "O titulo e obrigatorio")
        @Size(
                min = 3,
                max = 150,
                message = "O titulo deve possuir entre 3 e 150 caracteres"
        )
        String titulo,

        @NotBlank(message = "O autor e obrigatorio")
        @Size(
                min = 3,
                max = 100,
                message = "O autor deve possuir entre 3 e 100 caracteres"
        )
        String autor,

        @NotNull(message = "O ano de publicacao e obrigatorio")
        @Positive(message = "O ano de publicacao deve ser positivo")
        Integer anoPublicacao,
        @NotBlank(message = "O genero e obrigatorio")
        @Size(max = 100, message = "O genero deve possuir no maximo 100 caracteres")
        String genero,
        @NotNull(message = "O status de leitura e obrigatorio")
        StatusLeitura statusLeitura

) {

    @AssertTrue(message = "O ano de publicacao nao pode ser maior que o ano atual")
    public boolean isAnoPublicacaoValid() {
        return anoPublicacao == null
                || anoPublicacao <= Year.now().getValue();
    }

    public CreateBookInput toInput() {
        return new CreateBookInput(
                titulo,
                autor,
                anoPublicacao,
                genero,
                statusLeitura
        );
    }
}
