package com.example.bibliotecaapi.infrastructure.http;

import com.example.bibliotecaapi.application.*;
import com.example.bibliotecaapi.domain.BookId;
import com.example.bibliotecaapi.infrastructure.http.request.CreateBookRequest;
import com.example.bibliotecaapi.infrastructure.http.request.UpdateBookRequest;
import com.example.bibliotecaapi.infrastructure.http.response.BookResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final CreateBookUseCase createBookUseCase;
    private final ListBooksUseCase listBooksUseCase;
    private final GetBookByIdUseCase getBookByIdUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final DeleteBookUseCase deleteBookUseCase;

    @PostMapping
    public ResponseEntity<BookResponse> create(
            @RequestBody @Valid CreateBookRequest request
    ) {
        var input = request.toInput();

        var output = createBookUseCase.execute(input);

        var response = BookResponse.from(output);

        var location = URI.create("/books/" + output.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    public List<BookResponse> list() {
        return listBooksUseCase.execute()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable UUID id) {
        var output = getBookByIdUseCase.execute(
                new BookId(id)
        );

        return BookResponse.from(output);
    }

    @PutMapping("/{id}")
    public BookResponse update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateBookRequest request
    ) {
        var input = request.toInput();

        var output = updateBookUseCase.execute(
                new BookId(id),
                input
        );

        return BookResponse.from(output);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteBookUseCase.execute(
                new BookId(id)
        );

        return ResponseEntity.noContent().build();
    }
}
