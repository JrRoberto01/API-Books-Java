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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteBookUseCaseTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private DeleteBookUseCase useCase;

    @Test
    void deve_excluir_livro_com_sucesso() {
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

        useCase.execute(bookId);

        verify(repository, times(1))
                .findById(bookId);

        verify(repository, times(1))
                .delete(bookId);
    }

    @Test
    void deve_lancar_excecao_ao_excluir_livro_inexistente() {
        var bookId = new BookId(UUID.randomUUID());

        when(repository.findById(bookId))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> useCase.execute(bookId)
        );

        verify(repository, times(1))
                .findById(bookId);

        verify(repository, never())
                .delete(bookId);
    }
}
