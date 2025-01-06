package com.mcu.web.controllers;

import com.mcu.web.models.Pasien;
import com.mcu.web.models.PendaftaranMCU;
import com.mcu.web.models.User;
import com.mcu.web.service.PaketMCUService;
import com.mcu.web.service.PasienService;
import com.mcu.web.service.PendaftaranMCUService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/pendaftaran")
public class PendaftaranMCUController {

    @Autowired
    private PaketMCUService paketMCUService;

    @Autowired
    private PasienService pasienService;

    @Autowired
    private PendaftaranMCUService pendaftaranMCUService;

    @GetMapping("/{id}")
    public String showForm(@PathVariable Long id, Model model) {
        model.addAttribute("paketMCU", paketMCUService.findById(id)); // Ambil paket berdasarkan ID
        return "pendaftaran_mcu/index"; // Nama template untuk form pendaftaran
    }

    @PostMapping
    public String submitForm(HttpSession session, @RequestParam String nama,
                             @RequestParam String alamat,
                             @RequestParam String nomorTelepon,
                             @RequestParam String statusPasien,
                             @RequestParam String paket,
                             @RequestParam String jadwal,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !"User".equalsIgnoreCase(user.getRole())) {
            // Redirect ke halaman login atau halaman lain yang diinginkan
            redirectAttributes.addFlashAttribute("errorMessage", "Anda harus login terlebih dahulu.");
            return "redirect:/login";  // Arahkan ke halaman login
        }

        model.addAttribute("user", user);

        Pasien pasien;

        if ("User".equalsIgnoreCase(user.getRole())) {
            // Jika pasien baru
            if ("baru".equals(statusPasien)) {
                // Periksa apakah pasien dengan nomor telepon yang sama sudah ada
                pasien = pasienService.findByNomorTelepon(nomorTelepon);

                if (pasien != null) {
                    // Jika pasien sudah ada, tampilkan pesan error dan arahkan kembali
                    redirectAttributes.addFlashAttribute("errorMessage", "Pasien dengan nomor telepon " + nomorTelepon + " sudah terdaftar.");
                    return "redirect:/pendaftaran/" + paket;  // Arahkan kembali ke halaman pendaftaran
                } else {
                    // Jika pasien baru, simpan pasien baru
                    pasien = new Pasien();
                    pasien.setNama(nama);
                    pasien.setAlamat(alamat);
                    pasien.setNomorTelepon(nomorTelepon);
                    pasien.setUser(user);
                    pasienService.save(pasien); // Simpan pasien baru
                }
            } else {
                // Jika pasien lama, cari pasien berdasarkan nomor telepon
                pasien = pasienService.findByNomorTelepon(nomorTelepon);

                if (pasien == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Pasien dengan nomor telepon " + nomorTelepon + " tidak ditemukan.");
                    return "redirect:/pendaftaran/" + paket;
                }
            }

            // Setelah mendapatkan pasien, simpan pendaftaran MCU
            PendaftaranMCU pendaftaranMCU = new PendaftaranMCU();
            pendaftaranMCU.setPasien(pasien);
            pendaftaranMCU.setPaketMCU(paketMCUService.findById(Long.parseLong(paket))); // Cari paket berdasarkan ID
            pendaftaranMCU.setTanggalPendaftaran(LocalDate.parse(jadwal)); // Set jadwal pemeriksaan
            pendaftaranMCU.setStatus("Progres"); // Set status pendaftaran
            pendaftaranMCUService.save(pendaftaranMCU); // Simpan pendaftaran MCU
        }
        return "redirect:/"; // Redirect ke halaman utama setelah proses berhasil
    }
}
