package com.mcu.web.controllers;

import com.mcu.web.models.PaketMCU;
import com.mcu.web.service.PaketMCUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paket")
public class PaketMCUController {

    @Autowired
    private PaketMCUService paketMCUService;

    // Menambahkan paket MCU baru
    @PostMapping
    public ResponseEntity<PaketMCU> addPaket(@RequestBody PaketMCU paket) {
        if (paket.getNama() == null || paket.getHarga() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        PaketMCU savedPaket = paketMCUService.save(paket);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaket);
    }

    // Mengambil semua paket MCU
    @GetMapping
    public ResponseEntity<List<PaketMCU>> getAllPaket() {
        List<PaketMCU> paketList = paketMCUService.findAll();
        return ResponseEntity.ok(paketList);
    }

    // Mengambil paket MCU berdasarkan ID
    @GetMapping("/{id}")
    public ResponseEntity<PaketMCU> getPaketById(@PathVariable Long id) {
        PaketMCU paketMCU = paketMCUService.findById(id);
        return paketMCU != null ? ResponseEntity.ok(paketMCU) : ResponseEntity.notFound().build();
    }

    // Mengupdate paket MCU berdasarkan ID
    @PutMapping("/{id}")
    public ResponseEntity<PaketMCU> updatePaket(@PathVariable Long id, @RequestBody PaketMCU paketDetails) {
        PaketMCU updatedPaket = paketMCUService.update(id, paketDetails);
        return updatedPaket != null ? ResponseEntity.ok(updatedPaket) : ResponseEntity.notFound().build();
    }

    // Menghapus paket MCU berdasarkan ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaket(@PathVariable Long id) {
        if (paketMCUService.findById(id) != null) {
            paketMCUService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Mencari paket MCU berdasarkan kategori
    @GetMapping("/kategori/{kategori}")
    public ResponseEntity<List<PaketMCU>> getPaketByKategori(@PathVariable String kategori) {
        List<PaketMCU> paketList = paketMCUService.findByKategori(kategori);
        return ResponseEntity.ok(paketList);
    }

    @GetMapping("/paketMCU/form")
    public String tampilkanForm(Model model) {
        model.addAttribute("paketMCU", new PaketMCU());
        return "formPaketMCU";  // Nama view thymeleaf
    }
}
