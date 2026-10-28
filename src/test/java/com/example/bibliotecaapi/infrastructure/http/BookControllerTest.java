package com.example.bibliotecaapi.infrastructure.http;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.application.CreateBookUseCase;
import com.example.bibliotecaapi.application.DeleteBookUseCase;
import com.example.bibliotecaapi.application.GetBookByIdUseCase;
import com.example.bibliotecaapi.application.ListBooksUseCase;
import com.example.bibliotecaapi.application.UpdateBookUseCase;
import com.example.bibliotecaapi.application.output.BookOutput;
import com.example.bibliotecaapi.domain.BookId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateBookUseCase createBookUseCase;

    @MockitoBean
    private ListBooksUseCase listBooksUseCase;

    @MockitoBean
    private GetBookByIdUseCase getBookByIdUseCase;

    @MockitoBean
    private UpdateBookUseCase updateBookUseCase;

    @MockitoBean
    private DeleteBookUseCase deleteBookUseCase;

    @Test
    void deve_criar_livro_e_retornar_status_201() throws Exception {
        var id = UUID.randomUUID();

        var output = new BookOutput(
                id.toString(),
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(createBookUseCase.execute(any()))
                .thenReturn(output);

        var request = """
                {
                  "titulo": "Clean Code",
                  "autor": "Robert C. Martin",
                  "anoPublicacao": 2008,
                  "genero": "Programacao",
                  "statusLeitura": "NAO_LIDO"
                }
                """;

        mockMvc.perform(
                        post("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        header().string(
                                "Location",
                                "/books/" + id
                        )
                )
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.anoPublicacao").value(2008));

        verify(createBookUseCase)
                .execute(any());
    }

    @Test
    void deve_listar_livros_e_retornar_status_200() throws Exception {
        var firstBook = new BookOutput(
                UUID.randomUUID().toString(),
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        var secondBook = new BookOutput(
                UUID.randomUUID().toString(),
                "Clean Architecture",
                "Robert C. Martin",
                2017,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(listBooksUseCase.execute())
                .thenReturn(List.of(firstBook, secondBook));

        mockMvc.perform(
                        get("/books")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[1].titulo").value("Clean Architecture"));

        verify(listBooksUseCase).execute();
    }

    @Test
    void deve_buscar_livro_por_id_e_retornar_status_200() throws Exception {
        var id = UUID.randomUUID();
        var bookId = new BookId(id);

        var output = new BookOutput(
                id.toString(),
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(getBookByIdUseCase.execute(bookId))
                .thenReturn(output);

        mockMvc.perform(
                        get("/books/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.anoPublicacao").value(2008));

        verify(getBookByIdUseCase)
                .execute(bookId);
    }

    @Test
    void deve_atualizar_livro_e_retornar_status_200() throws Exception {
        var id = UUID.randomUUID();

        var output = new BookOutput(
                id.toString(),
                "Clean Architecture",
                "Robert C. Martin",
                2017,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        when(
                updateBookUseCase.execute(
                        eq(new BookId(id)),
                        any()
                )
        ).thenReturn(output);

        var request = """
                {
                  "titulo": "Clean Architecture",
                  "autor": "Robert C. Martin",
                  "anoPublicacao": 2017,
                  "genero": "Programacao",
                  "statusLeitura": "NAO_LIDO"
                }
                """;

        mockMvc.perform(
                        put("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Clean Architecture"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.anoPublicacao").value(2017));

        verify(updateBookUseCase)
                .execute(
                        eq(new BookId(id)),
                        any()
                );
    }

    @Test
    void deve_excluir_livro_e_retornar_status_204() throws Exception {
        var id = UUID.randomUUID();
        var bookId = new BookId(id);

        mockMvc.perform(
                        delete("/books/{id}", id)
                )
                .andExpect(status().isNoContent());

        verify(deleteBookUseCase)
                .execute(bookId);
    }
}
