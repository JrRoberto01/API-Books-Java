package com.example.bibliotecaapi.infrastructure.persistence.repository;

import com.example.bibliotecaapi.domain.StatusLeitura;

import com.example.bibliotecaapi.domain.Book;
import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.infrastructure.persistence.repository.BookCrudRepository;
import com.example.bibliotecaapi.infrastructure.persistence.repository.JpaBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository repository;

    @Autowired
    private BookCrudRepository crudRepository;

    @BeforeEach
    void setUp() {
        crudRepository.deleteAll();
    }

    @Test
    void deve_salvar_e_buscar_livro_por_id() {
        var book = new Book(
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        var saved = repository.save(book);

        var result = repository.findById(
                saved.getId()
        );

        assertThat(result).isPresent();

        assertThat(result.get().getId())
                .isEqualTo(book.getId());

        assertThat(result.get().getTitulo())
                .isEqualTo("Clean Code");

        assertThat(result.get().getAutor())
                .isEqualTo("Robert C. Martin");

        assertThat(result.get().getAnoPublicacao())
                .isEqualTo(2008);
    }

    @Test
    void deve_listar_livros_persistidos() {
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

        repository.save(firstBook);
        repository.save(secondBook);

        var books = repository.findAll();

        assertThat(books)
                .hasSize(2);

        assertThat(books)
                .extracting(Book::getId)
                .containsExactlyInAnyOrder(
                        firstBook.getId(),
                        secondBook.getId()
                );
    }

    @Test
    void deve_retornar_vazio_ao_buscar_livro_inexistente() {
        var nonExistentId = new BookId();

        var result = repository.findById(
                nonExistentId
        );

        assertThat(result).isEmpty();
    }

    @Test
    void deve_atualizar_livro_persistido() {
        var book = repository.save(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        2008,
                        "Programacao",
                        StatusLeitura.NAO_LIDO
                )
        );

        book.update(
                "Clean Architecture",
                "Robert C. Martin",
                2017,
                "Programacao",
                StatusLeitura.NAO_LIDO
        );

        repository.save(book);

        var result = repository.findById(
                book.getId()
        );

        assertThat(result).isPresent();

        assertThat(result.get().getTitulo())
                .isEqualTo("Clean Architecture");

        assertThat(result.get().getAutor())
                .isEqualTo("Robert C. Martin");

        assertThat(result.get().getAnoPublicacao())
                .isEqualTo(2017);
    }

    @Test
    void deve_excluir_livro_por_id() {
        var book = repository.save(
                new Book(
                        "Clean Code",
                        "Robert C. Martin",
                        2008,
                        "Programacao",
                        StatusLeitura.NAO_LIDO
                )
        );

        var bookId = book.getId();

        repository.delete(bookId);

        var result = repository.findById(bookId);

        assertThat(result).isEmpty();
    }
}
