package com.mcu.web.service;

import com.mcu.web.models.PaketMCU;
import com.mcu.web.repository.PaketMCURepository;
import com.mcu.web.repository.PendaftaranMCURepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaketMCUService {

    @Autowired
    private PaketMCURepository paketMCURepository;

    @Autowired
    private PendaftaranMCURepository pendaftaranMCURepository;

    public PaketMCU save(PaketMCU paketMCU) {
        return paketMCURepository.save(paketMCU);
    }

    public PaketMCU update(Long id, PaketMCU paketMCUDetails) {
        Optional<PaketMCU> paketMCUOptional = paketMCURepository.findById(id);
        if (paketMCUOptional.isPresent()) {
            PaketMCU paketMCU = paketMCUOptional.get();
            paketMCU.setNama(paketMCUDetails.getNama());
            paketMCU.setTipe(paketMCUDetails.getTipe());
            paketMCU.setKategori(paketMCUDetails.getKategori());
            paketMCU.setHarga(paketMCUDetails.getHarga());
            return paketMCURepository.save(paketMCU);
        }
        return null;
    }

    public void delete(Long id) {
        paketMCURepository.deleteById(id);
    }

    public List<PaketMCU> findAll() {
        return paketMCURepository.findAll();
    }

    public PaketMCU findById(Long id) {
        return paketMCURepository.findById(id).orElse(null);
    }

    public List<PaketMCU> findByKategori(String kategori) {
        return paketMCURepository.findByKategoriContainingIgnoreCase(kategori);
    }

    public PaketMCU simpanPaketMCU(PaketMCU paketMCU) {
        return paketMCURepository.save(paketMCU);
    }

    public void updatePaketMCU(PaketMCU paketMCU) {
        paketMCURepository.save(paketMCU);
    }

    public List<PaketMCU> findByTipe(String tipe) {
        return paketMCURepository.findByTipeContainingIgnoreCase(tipe);
    }

    // Menghapus PaketMCU berdasarkan ID
    @Transactional
    public void deletePaketMCU(Long id) {
        // Pastikan tidak ada PendaftaranMCU yang terkait dengan PaketMCU yang akan dihapus
        if (pendaftaranMCURepository.existsByPaketMCUId(id)) {
            throw new IllegalArgumentException("Paket MCU ini masih terkait dengan pendaftaran, tidak bisa dihapus.");
        }

        // Hapus PaketMCU
        PaketMCU paketMCU = paketMCURepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paket MCU tidak ditemukan dengan id: " + id));

        paketMCURepository.delete(paketMCU);
    }

    public Map<String, Integer> getHargaTermurahByTipe() {
        // Daftar kategori pemeriksaan
        List<String> kategoriPemeriksaan = Arrays.asList("Thorax", "Diabetes", "Abdomen", "Skoliosis", "TBC");

        // Map untuk menyimpan harga termurah per kategori
        Map<String, Integer> hargaTermurahMap = new HashMap<>();

        // Ambil harga termurah untuk setiap kategori
        for (String kategori : kategoriPemeriksaan) {
            Integer hargaTermurah = paketMCURepository.findHargaTermurahByJenisPemeriksaan(kategori);
            if (hargaTermurah != null) {
                hargaTermurahMap.put(kategori, hargaTermurah);
            }
        }

        return hargaTermurahMap;
    }
}
