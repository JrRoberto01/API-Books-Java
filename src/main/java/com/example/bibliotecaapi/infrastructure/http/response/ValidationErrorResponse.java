package com.example.bibliotecaapi.infrastructure.http.response;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<Violation> errors
) {

    public record Violation(
            String field,
            String message
    ) {
    }
}