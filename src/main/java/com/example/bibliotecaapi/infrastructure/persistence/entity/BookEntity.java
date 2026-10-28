package com.example.bibliotecaapi.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.example.bibliotecaapi.domain.StatusLeitura;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String autor;

    @Column(nullable = false)
    private Integer anoPublicacao;

    @Column(nullable = false, length = 100)
    private String genero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusLeitura statusLeitura;

    public BookEntity(UUID id, String titulo, String autor, Integer anoPublicacao, String genero, StatusLeitura statusLeitura) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.statusLeitura = statusLeitura;
    }
}
