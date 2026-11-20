package org.iesalixar.com.publicaciones.controllers;

import jakarta.validation.Valid;
import org.iesalixar.com.publicaciones.dtos.PublicacionCreateDTO;
import org.iesalixar.com.publicaciones.dtos.PublicacionUpdateDTO;
import org.iesalixar.com.publicaciones.exceptions.ResourceNotFoundException;
import org.iesalixar.com.publicaciones.services.LibroService;
import org.iesalixar.com.publicaciones.services.PublicacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Controller del CRUD de Publicacion (entidad principal, lado N de la relación con Libro).
 * En los formularios de alta y edición se carga la lista de libros para el {@code <select>}.
 */
@Controller
@RequestMapping("/publicaciones")   // Todas las URLs de esta clase empiezan por /publicaciones
public class PublicacionController {

    private static final Logger logger = LoggerFactory.getLogger(PublicacionController.class);

    private static final String LIST_VIEW = "views/publicacion/publicacion-list";
    private static final String FORM_VIEW = "views/publicacion/publicacion-form";
    private static final String DETAIL_VIEW = "views/publicacion/publicacion-detail";

    @Autowired
    private MessageSource messageSource;          // Para sacar los textos de messages_*.properties

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private LibroService libroService;            // Para la lista del <select>

    // ------------------------------------------------------------------ LISTADO
    @GetMapping
    public String list(Model model, Locale locale) {
        logger.info("Listando publicaciones");
        try {
            model.addAttribute("publicaciones", publicacionService.listAll());
        } catch (Exception e) {
            logger.error("Error al listar publicaciones: {}", e.getMessage(), e);
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.list.error", null, locale));
        }
        return LIST_VIEW;
    }

    // ------------------------------------------------------------------ DETALLE
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model,
                         RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Detalle de la publicación {}", id);
        try {
            model.addAttribute("publicacion", publicacionService.getDetail(id));
            return DETAIL_VIEW;
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.notFound", null, locale));
            return "redirect:/publicaciones";
        } catch (Exception e) {
            logger.error("Error al cargar el detalle {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.detail.error", null, locale));
            return "redirect:/publicaciones";
        }
    }

    // ------------------------------------------------------------------ ALTA
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Formulario de nueva publicación");
        model.addAttribute("publicacion", new PublicacionCreateDTO());
        model.addAttribute("libros", libroService.listAll());     // opciones del <select>
        return FORM_VIEW;
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("publicacion") PublicacionCreateDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        logger.info("Insertando publicación para el libro {}", dto.getLibroId());

        // Validación propia: la fecha tiene que ser una fecha real con formato yyyy-MM-dd
        validarFecha(dto.getFechaPublicacion(), result);

        // Errores de validación -> volver al formulario mostrando los errores
        if (result.hasErrors()) {
            model.addAttribute("libros", libroService.listAll());   // hay que volver a cargar el <select>
            return FORM_VIEW;
        }
        try {
            publicacionService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    messageSource.getMessage("msg.publicacion-controller.insert.success", null, locale));
            return "redirect:/publicaciones";
        } catch (ResourceNotFoundException ex) {
            // El libro elegido ya no existe
            logger.warn("Libro no encontrado al insertar: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.libro.notFound", null, locale));
            return "redirect:/publicaciones/new";
        } catch (Exception e) {
            logger.error("Error al insertar publicación: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.insert.error", null, locale));
            return "redirect:/publicaciones/new";
        }
    }

    // ------------------------------------------------------------------ EDICIÓN
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model,
                               RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Formulario de edición de la publicación {}", id);
        try {
            model.addAttribute("publicacion", publicacionService.getForEdit(id));
            model.addAttribute("libros", libroService.listAll());
            return FORM_VIEW;
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.notFound", null, locale));
            return "redirect:/publicaciones";
        } catch (Exception e) {
            logger.error("Error al cargar la publicación {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.edit.error", null, locale));
            return "redirect:/publicaciones";
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("publicacion") PublicacionUpdateDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        logger.info("Actualizando publicación {}", dto.getId());

        validarFecha(dto.getFechaPublicacion(), result);

        if (result.hasErrors()) {
            model.addAttribute("libros", libroService.listAll());
            return FORM_VIEW;
        }
        try {
            publicacionService.update(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    messageSource.getMessage("msg.publicacion-controller.update.success", null, locale));
            return "redirect:/publicaciones";
        } catch (ResourceNotFoundException ex) {
            logger.warn("No se pudo actualizar: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.notFound", null, locale));
            return "redirect:/publicaciones";
        } catch (Exception e) {
            logger.error("Error al actualizar la publicación {}: {}", dto.getId(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.update.error", null, locale));
            return "redirect:/publicaciones/edit?id=" + dto.getId();
        }
    }

    // ------------------------------------------------------------------ BORRADO
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id,
                         RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Borrando publicación {}", id);
        try {
            publicacionService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    messageSource.getMessage("msg.publicacion-controller.delete.success", null, locale));
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.notFound", null, locale));
        } catch (Exception e) {
            logger.error("Error al borrar la publicación {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.publicacion-controller.delete.error", null, locale));
        }
        return "redirect:/publicaciones";
    }

    // ------------------------------------------------------------------ VALIDACIÓN PROPIA
    /**
     * Comprueba que la fecha tiene formato yyyy-MM-dd y es una fecha real (no 2026-02-31).
     * Si no lo es, añade un error al campo para que se muestre en el formulario.
     */
    private void validarFecha(String fecha, BindingResult result) {
        if (fecha == null || fecha.isBlank() || result.hasFieldErrors("fechaPublicacion")) {
            return;  // si está vacía o ya tiene error, ya lo avisan @NotBlank / @Size
        }
        try {
            LocalDate.parse(fecha);   // formato ISO: yyyy-MM-dd
        } catch (DateTimeParseException e) {
            result.rejectValue("fechaPublicacion", "msg.publicacion.fechaPublicacion.format");
        }
    }
}
