package org.iesalixar.com.publicaciones.services;

import org.iesalixar.com.publicaciones.daos.LibroDAO;
import org.iesalixar.com.publicaciones.daos.PublicacionDAO;
import org.iesalixar.com.publicaciones.dtos.*;
import org.iesalixar.com.publicaciones.entities.Libro;
import org.iesalixar.com.publicaciones.entities.Publicacion;
import org.iesalixar.com.publicaciones.exceptions.ResourceNotFoundException;
import org.iesalixar.com.publicaciones.mappers.PublicacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private PublicacionDAO publicacionDAO;

    @Autowired
    private LibroDAO libroDAO;    // ← para convertir el libroId del <select> en un Libro real

    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDTO> listAll() {
        return PublicacionMapper.toDTOList(publicacionDAO.listAllPublicaciones());
    }

    @Override
    @Transactional(readOnly = true)
    public PublicacionDetailDTO getDetail(Long id) {
        Publicacion publicacion = publicacionDAO.getPublicacionByIdWithLibro(id);
        if (publicacion == null) {
            throw new ResourceNotFoundException("publicacion", "id", id);
        }
        return PublicacionMapper.toDetailDTO(publicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicacionUpdateDTO getForEdit(Long id) {
        Publicacion publicacion = publicacionDAO.getPublicacionByIdWithLibro(id);
        if (publicacion == null) {
            throw new ResourceNotFoundException("publicacion", "id", id);
        }
        return PublicacionMapper.toUpdateDTO(publicacion);
    }

    /** Alta: el libro elegido en el <select> tiene que existir. */
    @Override
    public void create(PublicacionCreateDTO dto) {
        Libro libro = libroDAO.getLibroById(dto.getLibroId());
        if (libro == null) {
            throw new ResourceNotFoundException("libro", "id", dto.getLibroId());
        }
        Publicacion publicacion = PublicacionMapper.toEntity(dto);
        publicacion.setLibro(libro);              // ★ aquí se asigna la relación
        publicacionDAO.insertPublicacion(publicacion);
    }

    /** Edición: la publicación y el libro elegido tienen que existir. */
    @Override
    public void update(PublicacionUpdateDTO dto) {
        Publicacion publicacion = publicacionDAO.getPublicacionById(dto.getId());
        if (publicacion == null) {
            throw new ResourceNotFoundException("publicacion", "id", dto.getId());
        }
        Libro libro = libroDAO.getLibroById(dto.getLibroId());
        if (libro == null) {
            throw new ResourceNotFoundException("libro", "id", dto.getLibroId());
        }
        PublicacionMapper.copyToExistingEntity(dto, publicacion);
        publicacion.setLibro(libro);              // ★ puede haber cambiado de libro
        publicacionDAO.updatePublicacion(publicacion);
    }

    @Override
    public void delete(Long id) {
        if (!publicacionDAO.existsPublicacionById(id)) {
            throw new ResourceNotFoundException("publicacion", "id", id);
        }
        publicacionDAO.deletePublicacion(id);
    }
}