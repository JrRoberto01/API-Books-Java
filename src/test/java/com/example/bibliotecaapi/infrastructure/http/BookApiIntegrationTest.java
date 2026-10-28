package com.example.bibliotecaapi.infrastructure.http;

import com.example.bibliotecaapi.infrastructure.persistence.repository.BookCrudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class BookApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookCrudRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deve_executar_crud_com_todos_os_campos() throws Exception {
        var criado = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requisicao("Programacao", "NAO_LIDO", 2008)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.genero").value("Programacao"))
                .andExpect(jsonPath("$.statusLeitura").value("NAO_LIDO"))
                .andReturn();

        var location = criado.getResponse().getHeader("Location");
        assertNotNull(location);

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.autor").value("Robert C. Martin"))
                .andExpect(jsonPath("$.anoPublicacao").value(2008))
                .andExpect(jsonPath("$.genero").value("Programacao"))
                .andExpect(jsonPath("$.statusLeitura").value("NAO_LIDO"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        for (var status : new String[]{"LENDO", "LIDO"}) {
            mockMvc.perform(put(location)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requisicao("Tecnologia", status, 2009)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.genero").value("Tecnologia"))
                    .andExpect(jsonPath("$.statusLeitura").value(status));

            mockMvc.perform(get(location))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.anoPublicacao").value(2009))
                    .andExpect(jsonPath("$.genero").value("Tecnologia"))
                    .andExpect(jsonPath("$.statusLeitura").value(status));
        }

        mockMvc.perform(delete(location)).andExpect(status().isNoContent());
        mockMvc.perform(get(location)).andExpect(status().isNotFound());
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "FINALIZADO"})
    void deve_rejeitar_status_invalido(String status) throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requisicao("Programacao", status, 2008)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deve_exigir_genero_e_status() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Clean Code","autor":"Robert C. Martin","anoPublicacao":2008}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'genero')]").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.field == 'statusLeitura')]").isNotEmpty());
    }

    @Test
    void deve_rejeitar_ano_futuro() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requisicao("Programacao", "LENDO", Year.now().getValue() + 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'anoPublicacao')]").isNotEmpty());
    }

    @Test
    void deve_retornar_404_ao_atualizar_ou_excluir_livro_inexistente() throws Exception {
        var location = "/books/" + UUID.randomUUID();
        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requisicao("Programacao", "LIDO", 2008)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete(location)).andExpect(status().isNotFound());
    }

    private String requisicao(String genero, String status, int ano) {
        return """
                {
                  "titulo": "Clean Code",
                  "autor": "Robert C. Martin",
                  "anoPublicacao": %d,
                  "genero": "%s",
                  "statusLeitura": "%s"
                }
                """.formatted(ano, genero, status);
    }
}
