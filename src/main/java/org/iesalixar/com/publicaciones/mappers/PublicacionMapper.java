package org.iesalixar.com.publicaciones.mappers;

import org.iesalixar.com.publicaciones.dtos.PublicacionCreateDTO;
import org.iesalixar.com.publicaciones.dtos.PublicacionDTO;
import org.iesalixar.com.publicaciones.dtos.PublicacionDetailDTO;
import org.iesalixar.com.publicaciones.dtos.PublicacionUpdateDTO;
import org.iesalixar.com.publicaciones.entities.Publicacion;

import java.util.List;

public class PublicacionMapper {
    public static PublicacionDTO toDTO(Publicacion entity) {
        if (entity == null) return null;
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(entity.getId());
        dto.setPrecio(entity.getPrecio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaPublicacion(entity.getFechaPublicacion());
        //Relacion
        dto.setLibroTitulo(entity.getLibro() != null ? entity.getLibro().getTitulo() : null);
        return dto;
    }

    public static List<PublicacionDTO> toDTOList(List<Publicacion> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(PublicacionMapper::toDTO).toList();
    }

    public static PublicacionDetailDTO toDetailDTO(Publicacion entity) {
        if (entity == null) return null;
        PublicacionDetailDTO dto = new PublicacionDetailDTO();
        dto.setId(entity.getId());
        dto.setPrecio(entity.getPrecio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaPublicacion(entity.getFechaPublicacion());
        // Relación N:1 -> el libro entero, convertido con el mapper de Libro
        dto.setLibro(LibroMapper.toDTOList(entity.getLibro()));
        return dto;
    }

    public static PublicacionUpdateDTO toUpdateDTO(Publicacion entity) {
        if (entity == null) return null;
        PublicacionUpdateDTO dto = new PublicacionUpdateDTO();
        dto.setId(entity.getId());
        dto.setPrecio(entity.getPrecio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaPublicacion(entity.getFechaPublicacion());
        // Relación: solo el id, para preseleccionar el <select>
        dto.setLibroId(entity.getLibro() != null ? entity.getLibro().getId() : null);
        return dto;
    }

    public static Publicacion toEntity(PublicacionCreateDTO dto) {
        if (dto == null) return null;
        Publicacion entity = new Publicacion();
        entity.setPrecio(dto.getPrecio());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaPublicacion(dto.getFechaPublicacion());
        return entity;
    }

    public static void copyToExistingEntity(PublicacionUpdateDTO dto, Publicacion entity) {
        if (dto == null || entity == null) return;
        entity.setPrecio(dto.getPrecio());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaPublicacion(dto.getFechaPublicacion());
    }
}
