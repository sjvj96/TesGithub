package com.mcu.web.controllers;

import com.mcu.web.models.User;
import com.mcu.web.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");  // Ambil user dari session
        return user != null && "Admin".equals(user.getRole());  // Periksa apakah user ada dan memiliki role Admin
    }

    @Autowired
    private UserService userService;

    @GetMapping
    public String login() {
        return "Login/LoginPage";
    }

    @PostMapping
    public String loginProcess(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        // Autentikasi pengguna berdasarkan email dan password
        User user = userService.authenticate(email, password);

        // Jika user ditemukan
        if (user != null) {
            session.setAttribute("user", user);  // Menyimpan objek user dalam session

            // Jika role pengguna adalah "admin", arahkan ke halaman dashboard
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/dashboard";  // Arahkan ke dashboard untuk admin
            }

            // Jika bukan admin, arahkan ke halaman utama
            return "redirect:/";  // Arahkan ke halaman utama untuk pengguna biasa
        } else {
            model.addAttribute("error", "Invalid email or password.");  // Tampilkan pesan error
            return "Login/LoginPage";  // Kembali ke halaman login jika login gagal
        }
    }

// href="/login/signup"
    @GetMapping("/signup")
    public String signup() {
        return "Login/SignUpPage";
    }


    @PostMapping("/signup/post")
    public String signupProcess(@ModelAttribute User user, Model model) {
        System.out.println("Received user data: " + user.getName() + ", " + user.getEmail() + ", " + user.getRole());

        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error", "Email is already registered.");
            return "Login/SignUpPage";
        }

        userService.register(user);
        model.addAttribute("message", "Registration successful, please log in.");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // Menghapus user dari session
        session.invalidate();  // Ini akan menghapus semua data yang ada dalam session

        // Setelah logout, arahkan ke halaman login atau halaman utama
        return "redirect:/login";  // Arahkan ke halaman login
    }
}
