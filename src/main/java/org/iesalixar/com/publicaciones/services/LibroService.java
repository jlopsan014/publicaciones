package org.iesalixar.com.publicaciones.services;

import org.iesalixar.com.publicaciones.dtos.*;

import java.util.List;

public interface LibroService {
    List<LibroDTO> listAll();
    LibroDetailDTO getDetail(Long id);
    LibroUpdateDTO getForEdit(Long id);
    void create(LibroCreateDTO dto);
    void update(LibroUpdateDTO dto);
    void delete(Long id);
}
