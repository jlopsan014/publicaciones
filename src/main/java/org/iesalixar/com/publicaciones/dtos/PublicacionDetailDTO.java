package org.iesalixar.com.publicaciones.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDetailDTO {
    private Long id;
    private int precio;
    private String descripcion;
    private String fechaPublicacion;

    private LibroDTO libro;
}
