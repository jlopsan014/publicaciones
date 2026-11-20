package org.iesalixar.com.publicaciones.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.iesalixar.com.publicaciones.entities.Publicacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PublicacionDAOImpl implements PublicacionDAO {

    private static final Logger logger = LoggerFactory.getLogger(PublicacionDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /** Todas las publicaciones con su libro cargado (para la columna "Libro" del listado). */
    @Override
    public List<Publicacion> listAllPublicaciones() {
        logger.info("Listando todas las publicaciones con su libro");
        String hql = "SELECT p FROM Publicacion p JOIN FETCH p.libro ORDER BY p.id";
        return entityManager.createQuery(hql, Publicacion.class).getResultList();
    }

    /** Busca por id (el libro queda LAZY). Útil para editar y borrar. */
    @Override
    public Publicacion getPublicacionById(Long id) {
        logger.info("Buscando publicación con id {}", id);
        return entityManager.find(Publicacion.class, id);
    }

    /** Busca por id con su libro cargado (para el detalle y el formulario de edición). */
    @Override
    public Publicacion getPublicacionByIdWithLibro(Long id) {
        logger.info("Buscando publicación con id {} y su libro", id);
        String hql = "SELECT p FROM Publicacion p JOIN FETCH p.libro WHERE p.id = :id";
        List<Publicacion> result = entityManager.createQuery(hql, Publicacion.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void insertPublicacion(Publicacion publicacion) {
        logger.info("Insertando publicación para el libro {}",
                publicacion.getLibro() != null ? publicacion.getLibro().getId() : null);
        entityManager.persist(publicacion);
    }

    @Override
    public void updatePublicacion(Publicacion publicacion) {
        logger.info("Actualizando publicación con id {}", publicacion.getId());
        entityManager.merge(publicacion);
    }

    @Override
    public void deletePublicacion(Long id) {
        logger.info("Borrando publicación con id {}", id);
        Publicacion publicacion = entityManager.find(Publicacion.class, id);
        if (publicacion != null) {
            entityManager.remove(publicacion);
        }
    }

    @Override
    public boolean existsPublicacionById(Long id) {
        String hql = "SELECT COUNT(p) FROM Publicacion p WHERE p.id = :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}