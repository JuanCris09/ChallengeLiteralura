package com.alura.literalura.principal;

import com.alura.literalura.model.Autores;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosAutores;
import com.alura.literalura.model.DatosBook;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner entrada = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL = "https://gutendex.com/";
    private final String BASE_URL = "books/?search=";
    @Autowired
    private BookRepository repositorio;

    @Autowired
    private AutorService autorService;

    private ConvierteDatos conversor = new ConvierteDatos();
    public Autores autores;
    private List<Book> book;
    private List<Autores> autor;

    public Principal(BookRepository repository) {
        this.repositorio = repository;
    }

    public void muestraMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elija la opción a través de su número:
                    1 - buscar libros por titulo
                    2 - agregar libros por titulo
                    3 - listar libros registrados
                    4 - listar autores registrados
                    5 - listar autores vivos en un determinado anio
                    6 - listar libros por idioma
                    0 - salir
                    """;
            System.out.println(menu);
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    agregarLibro();
                    break;
                case 3:
                    listarLibrosRegistrados();
                    break;
                case 4:
                    listarAutoresRegistrados();
                    break;
                case 5:
                    listarAutoresVivos();
                    break;
                case 6:
                    listarLibrosPorIdiomas();
                    break;
                case 0:
                    System.out.println("Cerrando aplicacion");
                    break;
                default:
                    System.out.println("Opcion inválida");
            }
        }
    }


    public Book buscarLibroAPI(String nombreLibro) {
        var json = consumoAPI.obtenerDatos(URL + BASE_URL + nombreLibro.replace(" ", "%20"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosBook> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosBook libro = libroBuscado.get();

            // Procesar autores
            DatosAutores datosAutor = libro.autores().get(0); // Tomando el primer autor como ejemplo
//            Autores autor = new Autores();
            Autores autor = autorService.saveAuthor(datosAutor); // Guardar o encontrar el autor
            Book book = new Book(libro.titulo(), autor);
            book.setIdiomas(libro.idiomas());
            book.setDescargas(libro.descargas());
            return book;
        } else {
            return null; // o puedes lanzar una excepción
        }
    }


    public void agregarLibro() {
        System.out.println("Ingrese el nombre del libro deseado: ");
        String tituloLibro = entrada.nextLine().trim().toLowerCase();
        Book libro = buscarLibroAPI(tituloLibro);

        if (libro != null) {
            repositorio.save(libro);
            System.out.println("Libro guardado: " + libro);
        } else {
            System.out.println("El libro no fue encontrado");
        }
    }


    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = entrada.nextLine();
        System.out.println("Buscando libro con titulo: " + nombreLibro);
        Optional<Book> book = repositorio.findByTituloWithAutorAndIdiomas(nombreLibro);
        if (book.isPresent()) {
            book.stream()
                    .sorted(Comparator.comparing(Book::getTitulo))
                    .forEach(System.out::println);
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        book = repositorio.findAll();

        book.stream()
                .sorted(Comparator.comparing(Book::getDescargas))
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        autor = autorService.listarTodosLosAutores();
        if (autor.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            autor.stream()
                    .sorted(Comparator.comparing(Autores::getNombre))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Ingresa el año para buscar autores vivos: ");
        var anio = entrada.nextInt();
        autor = autorService.listarAutoresVivos(anio);
        if (autor.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + anio);
        } else {
            autor.stream()
                    .sorted(Comparator.comparing(Autores::getNombre))
                    .forEach(System.out::println);
        }
    }

    private void listarLibrosPorIdiomas() {
        System.out.println("Ingrese el idiomas deseado: ");
        var idiomas = entrada.nextLine();
        book = repositorio.findByIdiomas(idiomas);
        if (book.isEmpty()) {
            System.out.println("No hay libros en el idioma " + idiomas);
        } else {
            System.out.println("Cantidad de libros en [" + idiomas + "]: " + book.size());
            book.stream()
                    .sorted(Comparator.comparing(Book::getTitulo))
                    .forEach(b -> System.out.println(b.toString()));
        }

    }

}