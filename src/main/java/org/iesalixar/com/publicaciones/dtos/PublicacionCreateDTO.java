package org.iesalixar.com.publicaciones.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionCreateDTO {
    // En un alta siempre llega null: el id lo genera la base de datos
    private Long id;

    @NotNull(message = "{msg.publicacion.precio.notNull}")
    @Positive(message = "{msg.publicacion.precio.positive}")
    private Integer precio;

    @NotBlank(message = "{msg.publicacion.descripcion.notEmpty}")
    @Size(max = 50, message = "{msg.publicacion.descripcion.size}")
    private String descripcion;

    @NotBlank(message = "{msg.publicacion.fechaPublicacion.notEmpty}")
    @Size(max = 10, message = "{msg.publicacion.fechaPublicacion.size}")
    private String fechaPublicacion;

    // Id del libro elegido en el <select>
    @NotNull(message = "{msg.publicacion.libro.notNull}")
    private Long libroId;
}
