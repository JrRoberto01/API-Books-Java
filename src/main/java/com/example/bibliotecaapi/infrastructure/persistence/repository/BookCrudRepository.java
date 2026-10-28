package com.example.bibliotecaapi.infrastructure.persistence.repository;

import com.example.bibliotecaapi.infrastructure.persistence.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BookCrudRepository extends CrudRepository<BookEntity, UUID> {
}
