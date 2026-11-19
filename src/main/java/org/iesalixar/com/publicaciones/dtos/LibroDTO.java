package org.iesalixar.com.publicaciones.dtos;

import lombok.*;

/**
 * DTO de lectura de Libro (select, detalle y listado)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO {
    private Long id;
    private String ISBN;
    private String titulo;
    private String autor;
    private String editorial;
    private String anioPublicacion;
}