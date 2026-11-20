package org.iesalixar.com.publicaciones.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.iesalixar.com.publicaciones.entities.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository      // Componente de persistencia: Spring lo crea y lo inyecta en el service
@Transactional   // Cada método va en una transacción (o se une a la del service)
public class LibroDAOImpl implements LibroDAO {

    private static final Logger logger = LoggerFactory.getLogger(LibroDAOImpl.class);

    @PersistenceContext          // Spring inyecta el EntityManager de Hibernate
    private EntityManager entityManager;

    @Override
    public List<Libro> listAllLibros() {
        logger.info("Listando todos los libros");
        String hql = "SELECT l FROM Libro l ORDER BY l.titulo";
        return entityManager.createQuery(hql, Libro.class).getResultList();
    }

    @Override
    public Libro getLibroById(Long id) {
        logger.info("Buscando libro con id {}", id);
        return entityManager.find(Libro.class, id);   // devuelve null si no existe
    }

    @Override
    public Libro getLibroByIdWithPublicaciones(Long id) {
        logger.info("Buscando libro con id {} y sus publicaciones", id);
        String hql = "SELECT l FROM Libro l LEFT JOIN FETCH l.publicaciones WHERE l.id = :id";
        List<Libro> result = entityManager.createQuery(hql, Libro.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void insertLibro(Libro libro) {
        logger.info("Insertando libro {}", libro.getTitulo());
        entityManager.persist(libro);
    }

    @Override
    public void updateLibro(Libro libro) {
        logger.info("Actualizando libro con id {}", libro.getId());
        entityManager.merge(libro);
    }

    @Override
    public void deleteLibro(Long id) {
        logger.info("Borrando libro con id {}", id);
        Libro libro = entityManager.find(Libro.class, id);
        if (libro != null) {
            entityManager.remove(libro);
        }
    }

    @Override
    public boolean existsLibroById(Long id) {
        String hql = "SELECT COUNT(l) FROM Libro l WHERE l.id = :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsLibroByISBN(String isbn) {
        String hql = "SELECT COUNT(l) FROM Libro l WHERE l.ISBN = :isbn";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsLibroByISBNAndNotId(String isbn, Long id) {
        String hql = "SELECT COUNT(l) FROM Libro l WHERE l.ISBN = :isbn AND l.id <> :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("isbn", isbn)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}