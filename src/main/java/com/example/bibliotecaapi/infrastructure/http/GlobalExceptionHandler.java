package com.example.bibliotecaapi.infrastructure.http;

import com.example.bibliotecaapi.domain.BookNotFoundException;
import com.example.bibliotecaapi.infrastructure.http.response.ErrorResponse;
import com.example.bibliotecaapi.infrastructure.http.response.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(
            BookNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Recurso nao encontrado",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var errors = new ArrayList<ValidationErrorResponse.Violation>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    var field = normalizeFieldName(error.getField());

                    errors.add(
                            new ValidationErrorResponse.Violation(
                                    field,
                                    error.getDefaultMessage()
                            )
                    );
                });

        exception.getBindingResult()
                .getGlobalErrors()
                .forEach(error ->
                        errors.add(
                                new ValidationErrorResponse.Violation(
                                        error.getObjectName(),
                                        error.getDefaultMessage()
                                )
                        )
                );

        var response = new ValidationErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Requisicao invalida",
                "Existem campos invalidos na requisicao",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        var message = exception.getRequiredType() == UUID.class
                ? "O identificador informado deve ser um ID valido"
                : "O parametro informado possui um formato invalido";

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Requisicao invalida",
                message,
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableMessageException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Requisicao invalida",
                "Verifique o formato dos campos. O statusLeitura deve ser NAO_LIDO, LENDO ou LIDO",
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Requisicao invalida",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        LOGGER.error(
                "Erro inesperado ao processar a requisicao",
                exception
        );

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro interno inesperado",
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ) {
        var response = new ErrorResponse(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    private String normalizeFieldName(String field) {
        if ("anoPublicacaoValid".equals(field)) {
            return "anoPublicacao";
        }

        return field;
    }
}
