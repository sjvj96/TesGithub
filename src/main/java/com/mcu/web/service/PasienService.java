package com.mcu.web.service;

import com.mcu.web.models.PaketMCU;
import com.mcu.web.models.Pasien;
import com.mcu.web.repository.PasienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasienService {

    @Autowired
    private PasienRepository pasienRepository;

    public Pasien save(Pasien pasien) {
        return pasienRepository.save(pasien);
    }

    public Pasien findById(Long id) {
        return pasienRepository.findById(id).orElse(null);
    }

    public List<Pasien> searchByNama(String nama) {
        return pasienRepository.findByNamaContainingIgnoreCase(nama);
    }

    public Pasien update(Long id, Pasien pasienDetails) {
        Optional<Pasien> pasienOptional = pasienRepository.findById(id);
        if (pasienOptional.isPresent()) {
            Pasien pasien = pasienOptional.get();
            if (pasienDetails.getNama() != null) pasien.setNama(pasienDetails.getNama());
            if (pasienDetails.getAlamat() != null) pasien.setAlamat(pasienDetails.getAlamat());
            if (pasienDetails.getNomorTelepon() != null) pasien.setNomorTelepon(pasienDetails.getNomorTelepon());
            return pasienRepository.save(pasien);
        }
        return null;
    }

    public void delete(Long id) {
        pasienRepository.deleteById(id);
    }

    public Pasien registerPasien(Long id, Pasien pasienDetails) {
        Optional<Pasien> pasienOptional = pasienRepository.findById(id);
        return pasienOptional.orElseGet(() -> save(pasienDetails));
    }

    public Pasien findByNomorTelepon(String nomorTelepon) {
        return pasienRepository.findByNomorTelepon(nomorTelepon);
    }
    public void update(Pasien pasien) {
        pasienRepository.save(pasien); // Save perubahan
    }

}
