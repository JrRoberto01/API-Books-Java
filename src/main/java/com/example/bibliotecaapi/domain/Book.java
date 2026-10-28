package com.example.bibliotecaapi.domain;

import lombok.Getter;
import org.springframework.util.Assert;

import java.time.Year;

@Getter
public class Book {

    private final BookId id;
    private String titulo;
    private String autor;
    private Integer anoPublicacao;
    private String genero;
    private StatusLeitura statusLeitura;

    public Book(String titulo, String autor, Integer anoPublicacao, String genero, StatusLeitura statusLeitura) {
        validate(titulo, autor, anoPublicacao, genero, statusLeitura);

        this.id = new BookId();
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.statusLeitura = statusLeitura;
    }

    public Book(BookId id, String titulo, String autor, Integer anoPublicacao, String genero, StatusLeitura statusLeitura) {
        Assert.notNull(id, "O identificador do livro nao pode ser nulo");

        validate(titulo, autor, anoPublicacao, genero, statusLeitura);

        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.statusLeitura = statusLeitura;
    }

    public void update(String titulo, String autor, Integer anoPublicacao, String genero, StatusLeitura statusLeitura) {
        validate(titulo, autor, anoPublicacao, genero, statusLeitura);

        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.statusLeitura = statusLeitura;
    }

    private static void validate(String titulo, String autor, Integer anoPublicacao, String genero, StatusLeitura statusLeitura) {
        Assert.hasText(titulo, "O titulo nao pode estar vazio");
        Assert.isTrue(titulo.length() >= 3 && titulo.length() <= 150, "O titulo deve possuir entre 3 e 150 caracteres");

        Assert.hasText(autor, "O autor nao pode estar vazio");
        Assert.isTrue(autor.length() >= 3 && autor.length() <= 100, "O autor deve possuir entre 3 e 100 caracteres");

        Assert.notNull(anoPublicacao, "O ano de publicacao nao pode ser nulo");
        Assert.isTrue(anoPublicacao > 0, "O ano de publicacao deve ser positivo");
        Assert.isTrue(anoPublicacao <= Year.now().getValue(), "O ano de publicacao nao pode ser maior que o ano atual");
        Assert.hasText(genero, "O genero nao pode estar vazio");
        Assert.isTrue(genero.length() <= 100, "O genero deve possuir no maximo 100 caracteres");
        Assert.notNull(statusLeitura, "O status de leitura nao pode ser nulo");
    }
}
