package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.application.input.CreateBookInput;
import com.example.bibliotecaapi.domain.Book;
import com.example.bibliotecaapi.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBookUseCaseTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private CreateBookUseCase useCase;

    @Test
    void deve_criar_livro_com_sucesso() {
        var input = new CreateBookInput(
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(repository.save(any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var output = useCase.execute(input);

        assertNotNull(output);
        assertNotNull(output.id());
        assertEquals("Clean Code", output.titulo());
        assertEquals("Robert C. Martin", output.autor());
        assertEquals(2008, output.anoPublicacao());

        verify(repository, times(1))
                .save(any(Book.class));
    }
}
