package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.domain.BookNotFoundException;
import com.example.bibliotecaapi.domain.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteBookUseCase {

    private final BookRepository repository;

    public void execute(BookId bookId) {
        if (repository.findById(bookId).isEmpty()) {
            throw new BookNotFoundException(bookId);
        }

        repository.delete(bookId);
    }
}
