package com.mcu.web.controllers;

import com.mcu.web.models.PaketMCU;
import com.mcu.web.models.Pasien;
import com.mcu.web.models.PendaftaranMCU;
import com.mcu.web.models.User;
import com.mcu.web.service.PaketMCUService;
import com.mcu.web.service.PasienService;
import com.mcu.web.service.PendaftaranMCUService;
import com.mcu.web.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardPetugas {

    @Autowired
    private PaketMCUService paketMCUService;

    @Autowired
    private PendaftaranMCUService pendaftaranMCUService;

    @Autowired
    private PasienService pasienService;

    @Autowired
    private UserService userService;

    // Cek Role Admin di setiap metode
    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "Admin".equals(user.getRole());
    }

    // Dashboard Petugas
    @GetMapping("dashboard")
    public String dashboardAdmin(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        List<PendaftaranMCU> pendaftaranProgres = pendaftaranMCUService.getProgresOrderedByTanggal();

        model.addAttribute("user", user);
        model.addAttribute("pendafratanProgres", pendaftaranProgres);
        model.addAttribute("jumlahProgres", pendaftaranMCUService.getJumlahProgres());
        model.addAttribute("jumlahSelesai", pendaftaranMCUService.getJumlahSelesai());
        model.addAttribute("totalPemasukan", pendaftaranMCUService.getTotalHargaSelesai());
        return "index";
    }

    // Reservasi
    @GetMapping("/dashboard/reservasi")
    public String reservasi(@RequestParam(required = false) String namaPasien,
                            @RequestParam(required = false) Long paketMCU,
                            @RequestParam(required = false) String tanggalPendaftaran, Model model,
                            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("paketMCUs", paketMCUService.findAll());
        model.addAttribute("pendaftaranMCU", pendaftaranMCUService.cariReservasi(namaPasien, paketMCU, tanggalPendaftaran));
        return "petugas/reservasi";
    }

    @GetMapping("/dashboard/reservasi/edit/{id}")
    public String editReservasi(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("pendaftaranMCU", pendaftaranMCUService.findById(id));
        return "petugas/edit_reservasi";
    }

    @PostMapping("/dashboard/reservasi/update")
    public String updateReservasi(@RequestParam Long id,
                                  @RequestParam Long paket_mcu_id,
                                  @RequestParam String tanggalPendaftaran,
                                  @RequestParam String status,
                                  @RequestParam Long pasien_id) {

        // Cari data reservasi berdasarkan ID
        PendaftaranMCU pendaftaranMCU = pendaftaranMCUService.findById(id);
        if (pendaftaranMCU == null) {
            return "redirect:/dashboard/reservasi";  // Jika data tidak ditemukan, redirect ke halaman lain
        }

        // Cari data Pasien dan PaketMCU berdasarkan ID
        Pasien pasien = pasienService.findById(pasien_id);
        PaketMCU paketMCU = paketMCUService.findById(paket_mcu_id);

        if (pasien == null || paketMCU == null) {
            return "redirect:/dashboard/reservasi";  // Jika salah satu data tidak ditemukan, redirect ke halaman lain
        }

        // Update data reservasi dengan data yang diterima dari form
        pendaftaranMCU.setPaketMCU(paketMCU);
        pendaftaranMCU.setPasien(pasien); // Set Pasien menggunakan objek yang ditemukan
        pendaftaranMCU.setTanggalPendaftaran(LocalDate.parse(tanggalPendaftaran));
        pendaftaranMCU.setStatus(status);

        // Simpan perubahan ke database
        pendaftaranMCUService.save(pendaftaranMCU);

        // Redirect ke halaman daftar reservasi setelah update berhasil
        return "redirect:/dashboard/reservasi";
    }

    @PostMapping("/deletePendaftaranMCU/{id}")
    public String deletePendaftaranMCU(@PathVariable Long id) {
        // Cari entitas PendaftaranMCU berdasarkan ID
        PendaftaranMCU pendaftaranMCU = pendaftaranMCUService.findById(id);

        if (pendaftaranMCU != null) {
            // Memutuskan hubungan dengan PaketMCU jika ada
            PaketMCU paketMCU = pendaftaranMCU.getPaketMCU();
            if (paketMCU != null) {
                // Putuskan hubungan dengan PaketMCU
                pendaftaranMCU.setPaketMCU(null);
            }

            // Memutuskan hubungan dengan Pasien jika ada
            Pasien pasien = pendaftaranMCU.getPasien();
            if (pasien != null) {
                // Putuskan hubungan dengan Pasien
                pendaftaranMCU.setPasien(null);
            }

            // Hapus PendaftaranMCU dari database
            pendaftaranMCUService.delete(pendaftaranMCU);
        }

        return "redirect:/dashboard/reservasi";  // Kembali ke halaman yang sesuai setelah penghapusan
    }

    // Paket MCU
    @GetMapping("dashboard/paketmcu")
    public String paketMCU(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("paketMCU", paketMCUService.findAll());
        return "paket_mcu/index";
    }

    @GetMapping("dashboard/paketmcu/create")
    public String createPaketMCU(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("paketMCU", new PaketMCU());
        return "paket_mcu/create";
    }

    @PostMapping("dashboard/paketmcu/post")
    public String tambahPaketMCU(@ModelAttribute PaketMCU paketMCU, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        paketMCUService.simpanPaketMCU(paketMCU);
        return "redirect:/dashboard/paketmcu";
    }

    @GetMapping("/dashboard/paketmcu/edit/{id}")
    public String editPaketMCU(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        PaketMCU paketMCU = paketMCUService.findById(id);
        model.addAttribute("paketMCU", paketMCU);
        return "paket_mcu/edit";
    }

    @PostMapping("/dashboard/paketmcu/update")
    public String updatePaketMCU(@ModelAttribute PaketMCU paketMCU, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        paketMCUService.updatePaketMCU(paketMCU);
        redirectAttributes.addFlashAttribute("message", "Paket MCU Berhasil ditambahkan");
        return "redirect:/dashboard/paketmcu";
    }

    @PostMapping("/deletePaketMCU/{id}")
    public String deletePaketMCU(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paketMCUService.deletePaketMCU(id);
            // Menambahkan pesan sukses yang akan ditampilkan pada halaman redirect
            redirectAttributes.addFlashAttribute("message", "Paket MCU berhasil dihapus");
            return "redirect:/dashboard/reservasi";
        } catch (IllegalArgumentException e) {
            // Menambahkan pesan error jika penghapusan gagal
            redirectAttributes.addFlashAttribute("message", "Gagal menghapus paket MCU: " + e.getMessage());
            return "redirect:/dashboard/reservasi";
        }
    }

//  Petugas/Admin
    @GetMapping("/dashboard/petugas")
    public String showPetugas(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("petugas", userService.findAdmins());

        return "petugas/showPetugas";
    }

    @GetMapping("/dashboard/petugas/edit/{id}")
    public String editPetugas(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("petugas", userService.findById(id));
        return "petugas/edit_petugas";
    }

    @PostMapping("/dashboard/petugas/update")
    public String updatePetugas(@RequestParam Long id, @RequestParam String name, @RequestParam String email, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }

        User petugas = userService.findById(id);
        if (petugas != null) {
            // Perbarui data petugas
            petugas.setName(name);
            petugas.setEmail(email);
            petugas.setRole("Admin");

            // Simpan perubahan ke database
            userService.save(petugas);

            // Redirect ke halaman petugas setelah update
            return "redirect:/dashboard/petugas";
        }

        return "redirect:/dashboard/petugas/edit/" + id;
    }

    @GetMapping("/dashboard/petugas/add")
    public String addPetugas(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "petugas/add_petugas";
    }

    @PostMapping("/dashboard/petugas/store")
    public String storePetugas(@RequestParam String name, @RequestParam String email, @RequestParam String password,Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }

        if (userService.emailExists(email)) {
            // Jika email sudah ada, kirimkan pesan error ke form pendaftaran
            model.addAttribute("error", "Email sudah terdaftar! Silakan gunakan email lain.");
            return "petugas/add_petugas";  // Kembali ke form pendaftaran
        }

        User newPetugas = new User();
        newPetugas.setName(name);
        newPetugas.setEmail(email);
        newPetugas.setPassword(password);
        newPetugas.setRole("Admin");
        userService.save(newPetugas);

        return "redirect:/dashboard/petugas";
    }

    @PostMapping("/dashboard/petugas/delete/{id}")
    public String deletePetugas(@PathVariable Long id, HttpSession session) {
        // Cari petugas berdasarkan ID
        if (!isAdmin(session)) {
            return "redirect:/";  // Jika bukan admin, arahkan ke halaman login
        }
        userService.delete(id);
        // Redirect ke halaman daftar petugas setelah penghapusan
        return "redirect:/dashboard/petugas"; // Ganti dengan URL yang sesuai
    }
}
