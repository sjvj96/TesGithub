package com.mcu.web.controllers;

import com.mcu.web.models.Pasien;
import com.mcu.web.service.PasienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pasien")
public class PasienController {

    @Autowired
    private PasienService pasienService;

    @GetMapping
    public String createPasien(Model model) {
        model.addAttribute("pasien", new Pasien());
        return "pendaftaran";
    }

    @PostMapping
    public String tambahPaketMCU(@ModelAttribute Pasien pasien) {
        pasienService.save(pasien);
        return "redirect:/";
    }

}
