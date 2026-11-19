-- Tabla del ladoc 1: se crea Primero porque publicaciones apunta a libros
CREATE TABLE IF NOT EXISTS libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    titulo VARCHAR(100) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    anio_publicacion VARCHAR(10) NOT NULL
    );
-- Tabla del lado N. la cual trae la foreing key de libro_id
CREATE TABLE IF NOT EXISTS publicaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    precio INT NOT NULL,
    descripcion VARCHAR(50) NOT NULL,
    fecha_publicacion VARCHAR(10) NOT NULL,
    libro_id INT NOT NULL,
    FOREIGN KEY (libro_id) REFERENCES libros(id)
    );