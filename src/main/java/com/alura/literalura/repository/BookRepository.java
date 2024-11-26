package com.alura.literalura.repository;

import com.alura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTituloContainsIgnoreCase(String nombreLibro);

    @Query("SELECT b From Book b LEFT JOIN FETCH b.autor LEFT JOIN FETCH b.idiomas WHERE b.titulo ILIKE %:titulo%")
    Optional<Book> findByTituloWithAutorAndIdiomas(String titulo);

    @Query("SELECT b from Book b JOIN b.idiomas i WHERE i=:idiomas")
    List<Book> findByIdiomas(String idiomas);

}
