# Gestor de publicaciones

Aplicación web con Spring Boot, Thymeleaf e Hibernate (DAO con `EntityManager`, sin Spring Data) para gestionar **publicaciones** de libros.

IES Alixar · DAW2 · Desarrollo Web en Entorno Servidor · Jaime

## Qué incluye

- **CRUD de Publicaciones** (entidad principal): listado, detalle, alta, edición y borrado. En el formulario se elige el libro con un `<select>`, y el libro se muestra en el listado y en el detalle.
- **CRUD de Libros** (entidad relacionada): listado, detalle (con sus publicaciones), alta, edición y borrado.
- **Relación 1:N**: un libro tiene muchas publicaciones (`@OneToMany`) y cada publicación tiene un libro (`@ManyToOne`, FK `libro_id`).
- **Subida de imágenes**: portada del libro (JPG, PNG, WEBP o GIF, máximo 2 MB).
- **Validaciones**: anotaciones en los DTOs y validaciones propias en los controllers (ISBN repetido, formato de fecha `aaaa-mm-dd`, tipo y tamaño de la portada).
- **Internacionalización**: español e inglés (selector en la cabecera).

## Requisitos

- Java 21
- Docker (para la base de datos MariaDB)

## Cómo arrancarla

1. Crea el fichero `.env` a partir del ejemplo y pon tus contraseñas:
   ```bash
   cp .env.example .env
   ```
2. Levanta la base de datos:
   ```bash
   docker compose up -d
   ```
3. Arranca la aplicación (o ejecuta `PublicacionesApplication` desde IntelliJ):
   ```bash
   ./mvnw spring-boot:run
   ```
4. Abre http://localhost:8080

Las tablas y los datos de ejemplo se crean solos al arrancar (`schema.sql` y `data.sql`).
Las portadas subidas se guardan en la carpeta `uploads/` (no se sube al repositorio).

## URLs principales

| URL | Página |
|---|---|
| `/` | Inicio |
| `/publicaciones` | Listado de publicaciones |
| `/libros` | Listado de libros |
| `?lang=es` / `?lang=en` | Cambiar de idioma |
