package com.gamer.gamerapp.controller;

import com.gamer.gamerapp.model.User;
import com.gamer.gamerapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GamerappController {

    private final UserService userService;

    public GamerappController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("user", new User());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute User user) {
        userService.registrarUsuario(user);
        return "redirect:/login?registrado";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
    }

    @GetMapping("/eventos")
    public String eventos() {
        return "eventos";
    }





    
}