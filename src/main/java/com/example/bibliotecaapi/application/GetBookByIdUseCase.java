package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.application.output.BookOutput;
import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.domain.BookNotFoundException;
import com.example.bibliotecaapi.domain.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GetBookByIdUseCase {

    private final BookRepository repository;

    public BookOutput execute(BookId id) {
        return repository.findById(id)
                .map(BookOutput::from)
                .orElseThrow(() -> new BookNotFoundException(id));
    }
}
