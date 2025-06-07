package com.goga74.platform.controller;

import com.goga74.platform.DB.entity.fora.ForaUser;
import com.goga74.platform.DB.repository.ForaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ForaUserRegController
{
    @Autowired
    private ForaUserRepository foraUserRepository;

    @GetMapping("/fregister")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("user", new ForaUser());
        return "fora_register";
    }

    @PostMapping("/fregister")
    public String registerUser(@ModelAttribute("user") ForaUser user)
    {
        // здесь можно добавить хеширование пароля и проверку уникальности email
        foraUserRepository.save(user);
        return "redirect:/";
    }
}
