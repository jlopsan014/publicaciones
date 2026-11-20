package org.iesalixar.com.publicaciones.controllers;

import jakarta.validation.Valid;
import org.iesalixar.com.publicaciones.dtos.LibroCreateDTO;
import org.iesalixar.com.publicaciones.dtos.LibroUpdateDTO;
import org.iesalixar.com.publicaciones.exceptions.DuplicateResourceException;
import org.iesalixar.com.publicaciones.exceptions.InvalidFileException;
import org.iesalixar.com.publicaciones.exceptions.ResourceNotFoundException;
import org.iesalixar.com.publicaciones.services.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controller del CRUD de Libro (entidad relacionada, lado 1 de la relación con Publicacion).
 * El detalle muestra las publicaciones del libro.
 */
@Controller
@RequestMapping("/libros")   // Todas las URLs de esta clase empiezan por /libros
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    private static final String LIST_VIEW = "views/libro/libro-list";
    private static final String FORM_VIEW = "views/libro/libro-form";
    private static final String DETAIL_VIEW = "views/libro/libro-detail";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LibroService libroService;

    // ------------------------------------------------------------------ LISTADO
    @GetMapping
    public String list(Model model, Locale locale) {
        logger.info("Listando libros");
        try {
            model.addAttribute("libros", libroService.listAll());
        } catch (Exception e) {
            logger.error("Error al listar libros: {}", e.getMessage(), e);
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.list.error", null, locale));
        }
        return LIST_VIEW;
    }

    // ------------------------------------------------------------------ DETALLE (con sus publicaciones)
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model,
                         RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Detalle del libro {}", id);
        try {
            model.addAttribute("libro", libroService.getDetail(id));
            return DETAIL_VIEW;
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.notFound", null, locale));
            return "redirect:/libros";
        } catch (Exception e) {
            logger.error("Error al cargar el detalle del libro {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.detail.error", null, locale));
            return "redirect:/libros";
        }
    }

    // ------------------------------------------------------------------ ALTA
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Formulario de nuevo libro");
        model.addAttribute("libro", new LibroCreateDTO());
        return FORM_VIEW;
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("libro") LibroCreateDTO dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        logger.info("Insertando libro con ISBN {}", dto.getISBN());
        if (result.hasErrors()) {
            return FORM_VIEW;
        }
        try {
            libroService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    messageSource.getMessage("msg.libro-controller.insert.success", null, locale));
            return "redirect:/libros";
        } catch (DuplicateResourceException ex) {
            // Validación propia: ISBN repetido -> error en el propio campo, conservando lo escrito
            logger.warn("ISBN duplicado: {}", dto.getISBN());
            result.rejectValue("ISBN", "msg.libro-controller.isbn.exists");
            return FORM_VIEW;
        } catch (InvalidFileException ex) {
            // Validación propia de la portada: tipo o tamaño no permitido -> error en el campo
            logger.warn("Portada no válida: {}", ex.getMessage());
            result.rejectValue("portadaFile", "msg.libro.portada.invalid");
            return FORM_VIEW;
        } catch (Exception e) {
            logger.error("Error al insertar libro: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.insert.error", null, locale));
            return "redirect:/libros/new";
        }
    }

    // ------------------------------------------------------------------ EDICIÓN
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model,
                               RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Formulario de edición del libro {}", id);
        try {
            model.addAttribute("libro", libroService.getForEdit(id));
            return FORM_VIEW;
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.notFound", null, locale));
            return "redirect:/libros";
        } catch (Exception e) {
            logger.error("Error al cargar el libro {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.edit.error", null, locale));
            return "redirect:/libros";
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("libro") LibroUpdateDTO dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Locale locale) {
        logger.info("Actualizando libro {}", dto.getId());
        if (result.hasErrors()) {
            return FORM_VIEW;
        }
        try {
            libroService.update(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    messageSource.getMessage("msg.libro-controller.update.success", null, locale));
            return "redirect:/libros";
        } catch (DuplicateResourceException ex) {
            logger.warn("ISBN duplicado al editar: {}", dto.getISBN());
            result.rejectValue("ISBN", "msg.libro-controller.isbn.exists");
            return FORM_VIEW;
        } catch (InvalidFileException ex) {
            logger.warn("Portada no válida al editar: {}", ex.getMessage());
            result.rejectValue("portadaFile", "msg.libro.portada.invalid");
            return FORM_VIEW;
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.notFound", null, locale));
            return "redirect:/libros";
        } catch (Exception e) {
            logger.error("Error al actualizar el libro {}: {}", dto.getId(), e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.update.error", null, locale));
            return "redirect:/libros/edit?id=" + dto.getId();
        }
    }

    // ------------------------------------------------------------------ BORRADO
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id,
                         RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Borrando libro {}", id);
        try {
            libroService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    messageSource.getMessage("msg.libro-controller.delete.success", null, locale));
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.notFound", null, locale));
        } catch (Exception e) {
            logger.error("Error al borrar el libro {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.libro-controller.delete.error", null, locale));
        }
        return "redirect:/libros";
    }
}
