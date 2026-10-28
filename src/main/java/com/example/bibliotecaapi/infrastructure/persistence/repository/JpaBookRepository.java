package com.example.bibliotecaapi.infrastructure.persistence.repository;

import com.example.bibliotecaapi.domain.Book;
import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.domain.BookRepository;
import com.example.bibliotecaapi.infrastructure.persistence.entity.BookEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Repository
public class JpaBookRepository  implements BookRepository {

    private final BookCrudRepository repository;

    @Override
    public Book save(Book book) {
        var entity = toEntity(book);
        var saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public List<Book> findAll() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return repository.findById((id.id()))
                .map(this::toDomain);
    }

    @Override
    public void delete(BookId id) {
        repository.deleteById(id.id());
    }

    private BookEntity toEntity(Book book) {
        return new BookEntity(
                book.getId().id(),
                book.getTitulo(),
                book.getAutor(),
                book.getAnoPublicacao(),
                book.getGenero(),
                book.getStatusLeitura()
        );
    }

    private Book toDomain(BookEntity entity) {
        return new Book(
                new BookId(entity.getId()),
                entity.getTitulo(),
                entity.getAutor(),
                entity.getAnoPublicacao(),
                entity.getGenero(),
                entity.getStatusLeitura()
        );
    }
}
