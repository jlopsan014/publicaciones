package org.iesalixar.com.publicaciones.services;

import org.iesalixar.com.publicaciones.dtos.*;
import java.util.List;

public interface PublicacionService {
    List<PublicacionDTO> listAll();
    PublicacionDetailDTO getDetail(Long id);
    PublicacionUpdateDTO getForEdit(Long id);
    void create(PublicacionCreateDTO dto);
    void update(PublicacionUpdateDTO dto);
    void delete(Long id);
}