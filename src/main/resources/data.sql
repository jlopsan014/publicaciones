-- Libros con id fijo, para saber a qué id apuntan las publicaciones
INSERT IGNORE INTO libros (id, isbn, titulo, autor, editorial, anio_publicacion) VALUES
(1, '9788408163381', 'La sombra del viento', 'Carlos Ruiz Zafón', 'Planeta', '2001'),
(2, '9788420471839', 'Cien años de soledad', 'Gabriel García Márquez', 'Alfaguara', '1967'),
(3, '9788498381498', 'Harry Potter y la piedra filosofal', 'J.K. Rowling', 'Salamandra', '1997'),
(4, '9788445000663', 'El señor de los anillos', 'J.R.R. Tolkien', 'Minotauro', '1954');

-- Publicaciones: libro_id apunta a libros.id
INSERT IGNORE INTO publicaciones (id, precio, descripcion, fecha_publicacion, libro_id) VALUES
(1, 12, 'Como nuevo, sin subrayar', '2026-10-01', 1),
(2, 8,  'Algo de desgaste en la portada', '2026-10-05', 1),
(3, 15, 'Edición de tapa dura', '2026-10-10', 2),
(4, 10, 'Buen estado general', '2026-10-12', 3),
(5, 6,  'Páginas amarillentas', '2026-10-15', 3),
(6, 20, 'Edición especial ilustrada', '2026-10-20', 4);