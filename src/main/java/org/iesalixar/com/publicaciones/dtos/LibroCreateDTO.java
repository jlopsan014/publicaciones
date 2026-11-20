package org.iesalixar.com.publicaciones.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroCreateDTO {

    private Long id;

    @NotBlank(message = "{msg.libro.isbn.notEmpty}")
    @Size(max = 13, message = "{msg.libro.isbn.size}")
    private String ISBN;

    @NotBlank(message = "{msg.libro.titulo.notEmpty}")
    @Size(max = 100, message = "{msg.libro.titulo.size}")
    private String titulo;

    @NotBlank(message = "{msg.libro.autor.notEmpty}")
    @Size(max = 100, message = "{msg.libro.autor.size}")
    private String autor;

    @NotBlank(message = "{msg.libro.editorial.notEmpty}")
    @Size(max = 100, message = "{msg.libro.editorial.size}")
    private String editorial;

    @NotBlank(message = "{msg.libro.anio.notEmpty}")
    @Size(max = 10, message = "{msg.libro.anio.size}")
    private String anioPublicacion;

    // Fichero de portada subido desde el formulario (opcional).
    // El service lo valida, lo guarda en disco y guarda su ruta en Libro.urlPortada
    private MultipartFile portadaFile;
}
