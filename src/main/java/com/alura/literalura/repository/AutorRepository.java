package com.alura.literalura.repository;

import com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autores, Long> {
    Optional<Autores> findByNombre(String nombre);

    @Query("SELECT a from Autores a WHERE a.fechaNacimiento <= :anio AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :anio)")
    List<Autores> findAutoresVivosEnAnio(@Param("anio") int anio);
}
