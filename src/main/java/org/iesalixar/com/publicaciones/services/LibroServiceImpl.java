package org.iesalixar.com.publicaciones.services;

import org.iesalixar.com.publicaciones.daos.LibroDAO;
import org.iesalixar.com.publicaciones.dtos.*;
import org.iesalixar.com.publicaciones.entities.Libro;
import org.iesalixar.com.publicaciones.exceptions.DuplicateResourceException;
import org.iesalixar.com.publicaciones.exceptions.ResourceNotFoundException;
import org.iesalixar.com.publicaciones.mappers.LibroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service          // Componente de lógica de negocio: Spring lo crea y lo inyecta en el controller
@Transactional    // Todo el método en UNA transacción: o se guarda todo o nada
public class LibroServiceImpl implements LibroService {

    @Autowired
    private LibroDAO libroDAO;

    @Autowired
    private FileStorageService fileStorageService;   // guarda y borra las portadas en disco

    /** Todos los libros (listado de libros y <select> de publicaciones). */
    @Override
    @Transactional(readOnly = true)
    public List<LibroDTO> listAll() {
        return LibroMapper.toDTOList(libroDAO.listAllLibros());
    }

    /** Detalle de un libro con sus publicaciones. */
    @Override
    @Transactional(readOnly = true)
    public LibroDetailDTO getDetail(Long id) {
        Libro libro = libroDAO.getLibroByIdWithPublicaciones(id);
        if (libro == null) {
            throw new ResourceNotFoundException("libro", "id", id);
        }
        return LibroMapper.toDetailDTO(libro);
    }

    /** Datos para rellenar el formulario de edición. */
    @Override
    @Transactional(readOnly = true)
    public LibroUpdateDTO getForEdit(Long id) {
        Libro libro = libroDAO.getLibroById(id);
        if (libro == null) {
            throw new ResourceNotFoundException("libro", "id", id);
        }
        return LibroMapper.toUpdateDTO(libro);
    }

    /** Alta: el ISBN no puede existir ya. Si viene portada, se valida y se guarda. */
    @Override
    public void create(LibroCreateDTO dto) {
        if (libroDAO.existsLibroByISBN(dto.getISBN())) {
            throw new DuplicateResourceException("libro", "ISBN", dto.getISBN());
        }
        Libro libro = LibroMapper.toEntity(dto);

        // Portada (opcional): lanza InvalidFileException si no es una imagen válida
        if (fileStorageService.hasFile(dto.getPortadaFile())) {
            libro.setUrlPortada(fileStorageService.storePortada(dto.getPortadaFile()));
        }
        libroDAO.insertLibro(libro);
    }

    /** Edición: el ISBN no puede tenerlo OTRO libro, y el libro tiene que existir. */
    @Override
    public void update(LibroUpdateDTO dto) {
        if (libroDAO.existsLibroByISBNAndNotId(dto.getISBN(), dto.getId())) {
            throw new DuplicateResourceException("libro", "ISBN", dto.getISBN());
        }
        Libro libro = libroDAO.getLibroById(dto.getId());
        if (libro == null) {
            throw new ResourceNotFoundException("libro", "id", dto.getId());
        }
        LibroMapper.copyToExistingEntity(dto, libro);

        // Nueva portada (opcional): si no se sube ninguna, se mantiene la actual
        String portadaAntigua = null;
        if (fileStorageService.hasFile(dto.getPortadaFile())) {
            portadaAntigua = libro.getUrlPortada();
            libro.setUrlPortada(fileStorageService.storePortada(dto.getPortadaFile()));
        }
        libroDAO.updateLibro(libro);

        // La portada anterior ya no se usa: se borra del disco
        if (portadaAntigua != null) {
            fileStorageService.deletePortada(portadaAntigua);
        }
    }

    /** Borrado (con cascade = ALL en la entidad, se borran también sus publicaciones) y su portada. */
    @Override
    public void delete(Long id) {
        Libro libro = libroDAO.getLibroById(id);
        if (libro == null) {
            throw new ResourceNotFoundException("libro", "id", id);
        }
        String portada = libro.getUrlPortada();
        libroDAO.deleteLibro(id);
        fileStorageService.deletePortada(portada);
    }
}