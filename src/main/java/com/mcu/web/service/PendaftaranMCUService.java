package com.mcu.web.service;

import com.mcu.web.models.PendaftaranMCU;
import com.mcu.web.models.Pasien;
import com.mcu.web.models.PaketMCU;
import com.mcu.web.models.User;
import com.mcu.web.repository.PendaftaranMCURepository;
import com.mcu.web.repository.PasienRepository;
import com.mcu.web.repository.PaketMCURepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PendaftaranMCUService {

    @Autowired
    private PendaftaranMCURepository pendaftaranMCURepository;

    @Autowired
    private PasienRepository pasienRepository;

    @Autowired
    private PaketMCURepository paketMCURepository;

    public PendaftaranMCU save(PendaftaranMCU pendaftaranMCU) {
        return pendaftaranMCURepository.save(pendaftaranMCU);
    }

    // Mendaftarkan pasien untuk MCU
    public PendaftaranMCU registerForMCU(Long pasienId, Long paketMCUId) {
        Pasien pasien = pasienRepository.findById(pasienId).orElse(null);
        PaketMCU paketMCU = paketMCURepository.findById(paketMCUId).orElse(null);
        if (pasien == null || paketMCU == null) {
            throw new IllegalArgumentException("Pasien atau Paket MCU tidak valid.");
        }
        PendaftaranMCU pendaftaranMCU = new PendaftaranMCU();
        pendaftaranMCU.setPasien(pasien);
        pendaftaranMCU.setPaketMCU(paketMCU);
        pendaftaranMCU.setTanggalPendaftaran(LocalDate.now());
        return pendaftaranMCURepository.save(pendaftaranMCU);
    }

    public void updatePendaftaranMCU(PendaftaranMCU pendaftaranMCU) {
        Optional<PendaftaranMCU> existingPendaftaranMCU = pendaftaranMCURepository.findById(pendaftaranMCU.getId());

        if (existingPendaftaranMCU.isPresent()) {
            PendaftaranMCU updatedPendaftaranMCU = existingPendaftaranMCU.get();

            // Update status and tanggalPendaftaran
            updatedPendaftaranMCU.setStatus(pendaftaranMCU.getStatus());
            updatedPendaftaranMCU.setTanggalPendaftaran(pendaftaranMCU.getTanggalPendaftaran());

            // Check if Pasien is set in the request, if not, retain the existing Pasien
            if (pendaftaranMCU.getPasien() != null && pendaftaranMCU.getPasien().getId() != null) {
                Pasien pasien = pasienRepository.findById(pendaftaranMCU.getPasien().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Pasien not found with id: " + pendaftaranMCU.getPasien().getId()));
                updatedPendaftaranMCU.setPasien(pasien);
            }

            // Check if PaketMCU is set in the request, if not, retain the existing PaketMCU
            if (pendaftaranMCU.getPaketMCU() != null && pendaftaranMCU.getPaketMCU().getId() != null) {
                PaketMCU paketMCU = paketMCURepository.findById(pendaftaranMCU.getPaketMCU().getId())
                        .orElseThrow(() -> new IllegalArgumentException("PaketMCU not found with id: " + pendaftaranMCU.getPaketMCU().getId()));
                updatedPendaftaranMCU.setPaketMCU(paketMCU);
            }

            // Save the updated PendaftaranMCU entity
            pendaftaranMCURepository.save(updatedPendaftaranMCU);
        } else {
            throw new IllegalArgumentException("PendaftaranMCU not found with id: " + pendaftaranMCU.getId());
        }
    }

    // Mencari pasien yang mendaftar pada periode tertentu
    public List<PendaftaranMCU> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return pendaftaranMCURepository.findByTanggalPendaftaranBetween(startDate, endDate);
    }

    public List<PendaftaranMCU> getPendaftaranByPasien(Long pasienId) {
        Pasien pasien = pasienRepository.findById(pasienId).orElse(null);
        if (pasien != null) {
            return pendaftaranMCURepository.findByPasien(pasien);
        }
        return null;
    }

    public List<PendaftaranMCU> getPendaftaranByPaket(Long paketId) {
        return pendaftaranMCURepository.findByPaketMCU(paketId);
    }

    public List<PendaftaranMCU> getPendaftaranByPeriode(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return pendaftaranMCURepository.findByTanggalPendaftaranBetween(start, end);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format tanggal tidak valid, pastikan menggunakan format yyyy-MM-dd.");
        }
    }

    public List<PendaftaranMCU> findAll() {
        return pendaftaranMCURepository.findAll();
    }

    public List<PendaftaranMCU> cariReservasi(String namaPasien, Long paketMCU, String tanggalPendaftaran) {
        LocalDate tanggal = (tanggalPendaftaran != null && !tanggalPendaftaran.isEmpty())
                ? LocalDate.parse(tanggalPendaftaran)
                : null;
        return pendaftaranMCURepository.cariReservasi(namaPasien, paketMCU, tanggal);
    }

    public PendaftaranMCU findById(Long id) {
        return pendaftaranMCURepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PendaftaranMCU not found with id: " + id));
    }

    public long getJumlahProgres() {
        return pendaftaranMCURepository.countByStatusProgres();
    }

    public long getJumlahSelesai() {
        return pendaftaranMCURepository.countByStatusSelesai();
    }

    public String getTotalHargaSelesai() {
        Double totalHarga = pendaftaranMCURepository.sumHargaByStatusSelesai();
        // Jika totalHarga null, anggap 0.0
        totalHarga = (totalHarga != null) ? totalHarga : 0.0;
        // Format angka tanpa desimal jika nilai bulat
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(totalHarga);
    }

    // Menghapus PendaftaranMCU berdasarkan ID
    public void delete(PendaftaranMCU pendaftaranMCU) {
        pendaftaranMCURepository.delete(pendaftaranMCU);
    }

    // Atau Anda bisa menggunakan method deleteById
    public void deleteById(Long id) {
        pendaftaranMCURepository.deleteById(id);
    }

    public void update(PendaftaranMCU pendaftaranMCU) {
        pendaftaranMCURepository.save(pendaftaranMCU); // Save perubahan
    }

    public List<PendaftaranMCU> findByUser(User user) {
        return pendaftaranMCURepository.findByUser(user);
    }

    public List<PendaftaranMCU> getProgresOrderedByTanggal() {
        return pendaftaranMCURepository.findProgresOrderedByTanggal();
    }
}
