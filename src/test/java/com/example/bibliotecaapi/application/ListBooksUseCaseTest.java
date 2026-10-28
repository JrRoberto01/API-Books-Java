package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.domain.Book;
import com.example.bibliotecaapi.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListBooksUseCaseTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private ListBooksUseCase useCase;

    @Test
    void deve_listar_livros_com_sucesso() {
        var firstBook = new Book(
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        var secondBook = new Book(
                "Clean Architecture",
                "Robert C. Martin",
                2017,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(repository.findAll())
                .thenReturn(List.of(firstBook, secondBook));

        var output = useCase.execute();

        assertEquals(2, output.size());

        assertEquals("Clean Code", output.get(0).titulo());
        assertEquals("Robert C. Martin", output.get(0).autor());
        assertEquals(2008, output.get(0).anoPublicacao());

        assertEquals("Clean Architecture", output.get(1).titulo());
        assertEquals("Robert C. Martin", output.get(1).autor());
        assertEquals(2017, output.get(1).anoPublicacao());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void deve_retornar_lista_vazia_quando_nao_houver_livros() {
        when(repository.findAll())
                .thenReturn(List.of());

        var output = useCase.execute();

        assertTrue(output.isEmpty());

        verify(repository, times(1))
                .findAll();
    }
}
