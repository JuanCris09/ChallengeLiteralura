package com.alura.literalura.service;

import com.alura.literalura.model.Autores;
import com.alura.literalura.model.DatosAutores;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository authRepository;

    public Autores saveAuthor(DatosAutores datosAutor) {
        // Verificar si el autor ya existe
        Optional<Autores> existingAuthor = authRepository.findByNombre(datosAutor.nombre());
        if (existingAuthor.isPresent()) {
            return existingAuthor.get();
        } // Si el autor no existe, crearlo y guardarlo
        Autores autor = new Autores();
        autor.setNombre(datosAutor.nombre());
        autor.setFechaNacimiento(datosAutor.fechaNacimiento());
        autor.setFechaFallecimiento(datosAutor.fechaFallecimiento());
        return authRepository.save(autor);
    }

    // Método para listar todos los autores
    public List<Autores> listarTodosLosAutores() {
        return authRepository.findAll();
    }

    public List<Autores> listarAutoresVivos(int anio) {
        return authRepository.findAutoresVivosEnAnio(anio);
    }

}

