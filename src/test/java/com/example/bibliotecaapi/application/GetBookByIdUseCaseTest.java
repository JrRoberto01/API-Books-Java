package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.domain.Book;
import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.domain.BookNotFoundException;
import com.example.bibliotecaapi.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookByIdUseCaseTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private GetBookByIdUseCase useCase;

    @Test
    void deve_retornar_livro_quando_encontrado() {
        var bookId = new BookId(UUID.randomUUID());

        var book = new Book(
                bookId,
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(repository.findById(bookId))
                .thenReturn(Optional.of(book));

        var output = useCase.execute(bookId);

        assertNotNull(output);
        assertEquals(bookId.id().toString(), output.id());
        assertEquals("Clean Code", output.titulo());
        assertEquals("Robert C. Martin", output.autor());
        assertEquals(2008, output.anoPublicacao());

        verify(repository, times(1))
                .findById(bookId);
    }

    @Test
    void deve_lancar_excecao_quando_livro_nao_for_encontrado() {
        var bookId = new BookId(UUID.randomUUID());

        when(repository.findById(bookId))
                .thenReturn(Optional.empty());

        var exception = assertThrows(
                BookNotFoundException.class,
                () -> useCase.execute(bookId)
        );

        assertEquals(
                "Livro com identificador " + bookId.id() + " nao encontrado",
                exception.getMessage()
        );

        verify(repository, times(1))
                .findById(bookId);
    }
}
