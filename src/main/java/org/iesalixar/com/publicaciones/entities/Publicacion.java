package org.iesalixar.com.publicaciones.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "publicaciones")
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "precio", nullable = false, length = 10)
    private int precio;

    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;

    @Column(name = "fecha_publicacion", nullable = false, length = 10)
    private String fechaPublicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "libro_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Libro libro;

    public Publicacion(int precio, String descripcion, String fechaPublicacion, Libro libro) {
        this.precio = precio;
        this.descripcion = descripcion;
        this.fechaPublicacion = fechaPublicacion;
        this.libro = libro;
    }
}
