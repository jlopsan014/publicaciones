package org.iesalixar.com.publicaciones.daos;

import org.iesalixar.com.publicaciones.entities.Libro;

import java.util.List;

public interface LibroDAO {
    List<Libro> listAllLibros();
    Libro getLibroById(Long id);
    Libro getLibroByIdWithPublicaciones(Long id);
    void insertLibro(Libro libro);
    void updateLibro(Libro libro);
    void deleteLibro(Long id);
    boolean existsLibroById(Long id);
    boolean existsLibroByISBN(String isbn);
    boolean existsLibroByISBNAndNotId(String isbn, Long id);
}
