package org.iesalixar.com.publicaciones.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Captura errores que ocurren ANTES de llegar al controller.
 * Si alguien sube un fichero mayor que spring.servlet.multipart.max-file-size,
 * Spring lo rechaza al leer la petición; aquí lo convertimos en un mensaje amable.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(RedirectAttributes redirectAttributes, Locale locale) {
        redirectAttributes.addFlashAttribute("errorMessage",
                messageSource.getMessage("msg.libro.portada.tooLarge", null, locale));
        return "redirect:/libros";
    }
}
