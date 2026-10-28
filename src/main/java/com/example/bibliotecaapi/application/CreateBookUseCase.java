package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.application.input.CreateBookInput;
import com.example.bibliotecaapi.application.output.BookOutput;
import com.example.bibliotecaapi.domain.Book;
import com.example.bibliotecaapi.domain.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class CreateBookUseCase {

    private final BookRepository repository;

    public BookOutput execute(CreateBookInput input) {
        var book = new Book(
                input.titulo(),
                input.autor(),
                input.anoPublicacao(),
                input.genero(),
                input.statusLeitura()
        );

        var saved = repository.save(book);

        return BookOutput.from(saved);
    }
}
