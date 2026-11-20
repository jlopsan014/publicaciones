package org.iesalixar.com.publicaciones.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISBN", nullable = false, length = 13)
    private String ISBN;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @Column(name = "editorial", nullable = false, length = 100)
    private String editorial;

    @Column(name = "anio_publicacion", nullable = false, length = 10)
    private String anioPublicacion;

    // Ruta pública de la imagen de portada (ej. "/uploads/portadas/abc.jpg"). Opcional.
    @Column(name = "url_portada", length = 255)
    private String urlPortada;

    @OneToMany(
        mappedBy = "libro",
        fetch = FetchType.LAZY,
        cascade = {CascadeType.ALL},
        orphanRemoval = false
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Publicacion> publicaciones = new ArrayList<>();
}
