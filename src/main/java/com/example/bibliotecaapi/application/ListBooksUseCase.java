package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.application.output.BookOutput;
import com.example.bibliotecaapi.domain.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ListBooksUseCase {

    private final BookRepository repository;

    public List<BookOutput> execute() {
        return repository.findAll()
                .stream()
                .map(BookOutput::from)
                .toList();
    }
}
