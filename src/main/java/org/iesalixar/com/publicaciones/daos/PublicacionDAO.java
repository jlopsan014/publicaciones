package org.iesalixar.com.publicaciones.daos;

import org.iesalixar.com.publicaciones.entities.Publicacion;
import java.util.List;

public interface PublicacionDAO {
    List<Publicacion> listAllPublicaciones();
    Publicacion getPublicacionById(Long id);
    Publicacion getPublicacionByIdWithLibro(Long id);
    void insertPublicacion(Publicacion publicacion);
    void updatePublicacion(Publicacion publicacion);
    void deletePublicacion(Long id);
    boolean existsPublicacionById(Long id);
}