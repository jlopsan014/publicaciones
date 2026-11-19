package org.iesalixar.com.publicaciones.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDTO {
    private Long id;
    private Integer precio;
    private String descripcion;
    private String fechaPublicacion;
    private String libroTitulo;
}
