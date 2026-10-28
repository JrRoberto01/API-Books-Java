package com.example.bibliotecaapi.application;

import com.example.bibliotecaapi.application.input.UpdateBookInput;
import com.example.bibliotecaapi.application.output.BookOutput;
import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.domain.BookNotFoundException;
import com.example.bibliotecaapi.domain.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UpdateBookUseCase {

    private final BookRepository repository;

    public BookOutput execute(BookId id, UpdateBookInput input) {
        var book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.update(
                input.titulo(),
                input.autor(),
                input.anoPublicacao(),
                input.genero(),
                input.statusLeitura()
        );

        var updated = repository.save(book);

        return BookOutput.from(updated);
    }
}
