package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.application.input.UpdateBookInput;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookUseCaseTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private UpdateBookUseCase useCase;

    @Test
    void deve_atualizar_livro_com_sucesso() {
        var bookId = new BookId(UUID.randomUUID());

        var book = new Book(
                bookId,
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        var input = new UpdateBookInput(
                "Clean Architecture",
                "Robert C. Martin",
                2017,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(repository.findById(bookId))
                .thenReturn(Optional.of(book));

        when(repository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var output = useCase.execute(bookId, input);

        assertEquals(bookId.id().toString(), output.id());
        assertEquals("Clean Architecture", output.titulo());
        assertEquals("Robert C. Martin", output.autor());
        assertEquals(2017, output.anoPublicacao());

        verify(repository, times(1))
                .findById(bookId);

        verify(repository, times(1))
                .save(book);
    }

    @Test
    void deve_lancar_excecao_ao_atualizar_livro_inexistente() {
        var bookId = new BookId(UUID.randomUUID());

        var input = new UpdateBookInput(
                "Clean Architecture",
                "Robert C. Martin",
                2017,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(repository.findById(bookId))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> useCase.execute(bookId, input)
        );

        verify(repository, times(1))
                .findById(bookId);

        verify(repository, never())
                .save(any(Book.class));
    }
}
