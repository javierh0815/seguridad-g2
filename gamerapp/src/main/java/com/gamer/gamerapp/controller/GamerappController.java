package com.gamer.gamerapp.controller;

import com.gamer.gamerapp.model.User;
import com.gamer.gamerapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GamerappController {

    private final UserService userService;

    public GamerappController(UserService userService) {
        this.userService = userService;
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
    public String buscar() {
        return "buscar";
    }


    @GetMapping("/eventos")
    public String eventos() {
        return "eventos";
    }


    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }
}