package org.iesalixar.com.publicaciones.mappers;

import org.iesalixar.com.publicaciones.dtos.LibroCreateDTO;
import org.iesalixar.com.publicaciones.dtos.LibroDTO;
import org.iesalixar.com.publicaciones.dtos.LibroDetailDTO;
import org.iesalixar.com.publicaciones.dtos.LibroUpdateDTO;
import org.iesalixar.com.publicaciones.entities.Libro;

import java.util.List;

public class LibroMapper {
    public static LibroDTO toDTO(Libro entity) {
        if (entity == null) return null;
        LibroDTO dto = new LibroDTO();
        dto.setId(entity.getId());
        dto.setISBN(entity.getISBN());
        dto.setTitulo(entity.getTitulo());
        dto.setAutor(entity.getAutor());
        dto.setEditorial(entity.getEditorial());
        dto.setAnioPublicacion(entity.getAnioPublicacion());
        dto.setUrlPortada(entity.getUrlPortada());
        return dto;
    }

    public static List<LibroDTO> toDTOList(List<Libro> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(LibroMapper::toDTO).toList();
    }

    public static LibroDetailDTO toDetailDTO(Libro entity) {
        if (entity == null) return null;
        LibroDetailDTO dto = new LibroDetailDTO();
        dto.setId(entity.getId());
        dto.setISBN(entity.getISBN());
        dto.setTitulo(entity.getTitulo());
        dto.setAutor(entity.getAutor());
        dto.setEditorial(entity.getEditorial());
        dto.setAnioPublicacion(entity.getAnioPublicacion());
        dto.setUrlPortada(entity.getUrlPortada());

        if (entity.getPublicaciones() != null) {
            dto.setPublicaciones(
                    entity.getPublicaciones().stream()
                            .map(PublicacionMapper::toDTO)
                            .toList()
            );
        }
        return dto;
    }

    public static Libro toEntity(LibroCreateDTO dto) {
        if (dto == null) return null;
        Libro entity = new Libro();
        entity.setISBN(dto.getISBN());
        entity.setTitulo(dto.getTitulo());
        entity.setAutor(dto.getAutor());
        entity.setEditorial(dto.getEditorial());
        entity.setAnioPublicacion(dto.getAnioPublicacion());
        return entity;
    }

    public static LibroUpdateDTO toUpdateDTO(Libro entity) {
        if (entity == null) return null;
        LibroUpdateDTO dto = new LibroUpdateDTO();
        dto.setId(entity.getId());
        dto.setISBN(entity.getISBN());
        dto.setTitulo(entity.getTitulo());
        dto.setAutor(entity.getAutor());
        dto.setEditorial(entity.getEditorial());
        dto.setAnioPublicacion(entity.getAnioPublicacion());
        dto.setUrlPortada(entity.getUrlPortada());
        return dto;
    }

    // Nota: toEntity y copyToExistingEntity NO tocan urlPortada.
    // La portada la gestiona el service (guarda el fichero y asigna la ruta).
    public static void copyToExistingEntity(LibroUpdateDTO dto, Libro entity) {
        if (dto == null || entity == null) return;
        entity.setISBN(dto.getISBN());
        entity.setTitulo(dto.getTitulo());
        entity.setAutor(dto.getAutor());
        entity.setEditorial(dto.getEditorial());
        entity.setAnioPublicacion(dto.getAnioPublicacion());
    }
}
