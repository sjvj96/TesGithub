package com.mcu.web.controllers;

import com.mcu.web.models.PaketMCU;
import com.mcu.web.models.User;
import com.mcu.web.service.PaketMCUService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private PaketMCUService paketMCUService;

    @GetMapping
    public String landingPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        Map<String, Integer> hargaTermurahMap = paketMCUService.getHargaTermurahByTipe();

        model.addAttribute("hargaTermurah", hargaTermurahMap);
        model.addAttribute("user", user);
        return "layouts/app";
    }

    @GetMapping("/mcu/{id}")
    public String mcuPage(@PathVariable Long id, Model model) {
        model.addAttribute("paketMCU", paketMCUService.findById(id));
        return "mcu_pasien/mcu_detail";
    }

    @GetMapping("/thorax")
    public String thoraxPage(Model model) {
        model.addAttribute("thoraxList",paketMCUService.findByTipe("Thorax"));
        return "mcu_pasien/thorax";
    }

    @GetMapping("/abdomen")
    public String abdomenPage(Model model) {
        model.addAttribute("abdomenList",paketMCUService.findByTipe("Abdomen"));
        return "mcu_pasien/abdomen";
    }

    @GetMapping("/diabetes")
    public String diabetesPage(Model model) {
        model.addAttribute("diabetesList",paketMCUService.findByTipe("Diabetes"));
        return "mcu_pasien/diabetes";
    }

    @GetMapping("/tbc")
    public String tbcPage(Model model) {
        model.addAttribute("tbcList",paketMCUService.findByTipe("TBC"));
        return "mcu_pasien/TBC";
    }

    @GetMapping("/skoliosis")
    public String skoliosisPage(Model model) {
        model.addAttribute("skoliosisList",paketMCUService.findByTipe("Skoliosis"));
        return "mcu_pasien/Skoliosis";
    }
}
