package com.mcu.web.controllers;

import com.mcu.web.models.PendaftaranMCU;
import com.mcu.web.models.User;
import com.mcu.web.service.PendaftaranMCUService;
import com.mcu.web.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PendaftaranMCUService pendaftaranMCUService;

    private boolean isUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "User".equals(user.getRole());
    }

    @GetMapping
    public String dashboardUser(HttpSession session, Model model) {
        if (!isUser(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");

        List<PendaftaranMCU> daftarPendaftaran = pendaftaranMCUService.findByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("daftarPendaftaran", daftarPendaftaran);
        return "User/index";
    }
}
