package org.iesalixar.com.publicaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDetailDTO {
    private Long id;
    private String ISBN;
    private String titulo;
    private String autor;
    private String editorial;
    private String anioPublicacion;

    private List<PublicacionDTO> publicaciones = new ArrayList<>();
}
