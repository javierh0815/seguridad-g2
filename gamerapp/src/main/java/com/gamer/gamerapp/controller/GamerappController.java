package com.gamer.gamerapp.controller;

import com.gamer.gamerapp.model.User;
import com.gamer.gamerapp.service.EventoService;
import com.gamer.gamerapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GamerappController {

    private final UserService userService;
    private final EventoService eventoService;

    public GamerappController(UserService userService, EventoService eventoService) {
        this.userService = userService;
        this.eventoService = eventoService;
    }

    @GetMapping({"/", "/home"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "registrado", required = false) String registrado,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("msg", "Has cerrado sesión correctamente.");
        }
        if (registrado != null) {
            model.addAttribute("msg", "Registro exitoso. ¡Ahora puedes iniciar sesión!");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("user", new User());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registrarUsuario(user);
            return "redirect:/login?registrado";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al registrar la cuenta. Intente nuevamente.");
            return "registro";
        }
    }

    @GetMapping("/buscar")
    public String buscar(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "buscar";
    }

    @GetMapping("/eventos")
    public String eventos(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "eventos";
    }

    @PostMapping("/eventos/{id}/media")
    public String agregarMediaWeb(@PathVariable Long id,
                                  @RequestParam String url,
                                  @RequestParam String tipo,
                                  @RequestParam(required = false) String descripcion,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            eventoService.agregarMedia(id, url, tipo, descripcion, authentication.getName());
            redirectAttributes.addFlashAttribute("mensajeExito", "Multimedia agregada correctamente al evento.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/eventos";
    }

    @PostMapping("/eventos/{id}/comentarios")
    public String agregarComentarioWeb(@PathVariable Long id,
                                       @RequestParam String contenido,
                                       @RequestParam Integer valoracion,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        try {
            eventoService.agregarComentario(id, contenido, valoracion, authentication.getName());
            redirectAttributes.addFlashAttribute("mensajeExito", "Comentario y valoración publicados.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/eventos";
    }

    @PostMapping("/eventos/{id}/compartir")
    public String compartirEventoWeb(@PathVariable Long id,
                                     @RequestParam String plataforma,
                                     @RequestParam(required = false) String mensaje,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            var resultado = eventoService.compartirEvento(id, plataforma, mensaje, authentication.getName());
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Evento compartido en " + resultado.get("plataforma") + ". Enlace: " + resultado.get("enlaceCompartir"));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/eventos";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }
}