package org.iesalixar.com.publicaciones.services;

import org.iesalixar.com.publicaciones.exceptions.InvalidFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/**
 * Guarda en disco las imágenes subidas (portadas de libros) y las borra cuando ya no se usan.
 * <p>
 * Las imágenes se guardan en {@code <app.upload.dir>/portadas/} con un nombre aleatorio (UUID)
 * y se sirven en la URL {@code /uploads/portadas/<nombre>} (ver {@code WebConfig}).
 * En la base de datos solo se guarda esa URL, nunca la imagen.
 */
@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    /** Tamaño máximo permitido para una portada: 2 MB. */
    private static final long MAX_SIZE_BYTES = 2 * 1024 * 1024;

    /** Tipos de imagen permitidos y la extensión con la que se guardan. */
    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif"
    );

    /** Prefijo de URL con el que se sirven los ficheros subidos. */
    public static final String URL_PREFIX = "/uploads/portadas/";

    /** Carpeta raíz de subidas (application.properties: app.upload.dir). */
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    /** Indica si el usuario ha elegido un fichero en el formulario. */
    public boolean hasFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    /**
     * Valida y guarda la portada. Devuelve la URL pública para guardarla en Libro.urlPortada.
     *
     * @throws InvalidFileException si no es una imagen permitida o supera los 2 MB
     */
    public String storePortada(MultipartFile file) {
        // 1) Validar tipo
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.containsKey(contentType)) {
            throw new InvalidFileException("libro", "portadaFile", contentType, "Tipo de fichero no permitido");
        }
        // 2) Validar tamaño
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new InvalidFileException("libro", "portadaFile", file.getSize(), "Fichero demasiado grande");
        }
        // 3) Guardar con un nombre único (evita choques y nombres peligrosos)
        String fileName = UUID.randomUUID() + ALLOWED_TYPES.get(contentType);
        Path folder = portadasFolder();
        try (InputStream in = file.getInputStream()) {
            Files.createDirectories(folder);
            Files.copy(in, folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la portada", e);
        }
        logger.info("Portada guardada: {}", fileName);
        return URL_PREFIX + fileName;
    }

    /** Borra del disco la portada de una URL guardada antes (si existe). No falla si no está. */
    public void deletePortada(String url) {
        if (url == null || !url.startsWith(URL_PREFIX)) {
            return;
        }
        String fileName = Paths.get(url.substring(URL_PREFIX.length())).getFileName().toString();
        try {
            Files.deleteIfExists(portadasFolder().resolve(fileName));
            logger.info("Portada borrada: {}", fileName);
        } catch (IOException e) {
            logger.warn("No se pudo borrar la portada {}: {}", fileName, e.getMessage());
        }
    }

    private Path portadasFolder() {
        return Paths.get(uploadDir, "portadas").toAbsolutePath().normalize();
    }
}
