package com.mcu.web.repository;

import com.mcu.web.models.PendaftaranMCU;
import com.mcu.web.models.Pasien;
import com.mcu.web.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PendaftaranMCURepository extends JpaRepository<PendaftaranMCU, Long> {

    // 1. Temukan semua pendaftaran berdasarkan pasien tertentu
    List<PendaftaranMCU> findByPasien(Pasien pasien);

    // 2. Temukan semua pendaftaran berdasarkan tanggal tertentu
    List<PendaftaranMCU> findByTanggalPendaftaran(LocalDate tanggalPendaftaran);

    // 3. Temukan semua pendaftaran dalam rentang tanggal tertentu
    @Query("SELECT p FROM PendaftaranMCU p WHERE p.tanggalPendaftaran BETWEEN :startDate AND :endDate")
    List<PendaftaranMCU> findByTanggalPendaftaranBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 4. Temukan semua pendaftaran berdasarkan nama pasien (menggunakan nama dari entitas Pasien)
    @Query("SELECT p FROM PendaftaranMCU p WHERE LOWER(p.pasien.nama) LIKE LOWER(CONCAT('%', :nama, '%'))")
    List<PendaftaranMCU> findByNamaPasien(@Param("nama") String nama);

    // 5. Temukan semua pendaftaran berdasarkan ID paket MCU tertentu
    @Query("SELECT p FROM PendaftaranMCU p WHERE p.paketMCU.id = :paketId")
    List<PendaftaranMCU> findByPaketMCU(@Param("paketId") Long paketId);

    // 6. Hitung jumlah pendaftaran dalam rentang waktu tertentu
    @Query("SELECT COUNT(p) FROM PendaftaranMCU p WHERE p.tanggalPendaftaran BETWEEN :startDate AND :endDate")
    Long countByTanggalPendaftaranBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Method untuk menghitung jumlah pendaftaran dengan status 'Progress'
    @Query("SELECT COUNT(p) FROM PendaftaranMCU p WHERE p.status = 'Progres'")
    long countByStatusProgres();

    // Method untuk menghitung jumlah pendaftaran dengan status 'Selesai'
    @Query("SELECT COUNT(p) FROM PendaftaranMCU p WHERE p.status = 'Selesai'")
    long countByStatusSelesai();

    // Method untuk menghitung jumlah harga dari pendaftaran dengan status 'Selesai'
    @Query("SELECT SUM(p.paketMCU.harga) FROM PendaftaranMCU p WHERE p.status = 'Selesai'")
    Double sumHargaByStatusSelesai();

    List<PendaftaranMCU> findByPasienNamaContaining(String namaPasien);

    List<PendaftaranMCU> findByPaketMCUId(Long paketMCU);

    public boolean existsByPaketMCUId(Long paketMCUId);

    @Query("SELECT p FROM PendaftaranMCU p WHERE p.pasien.user = :user")
    List<PendaftaranMCU> findByUser(@Param("user") User user);

    @Query("SELECT p FROM PendaftaranMCU p " +
            "WHERE (:namaPasien IS NULL OR LOWER(p.pasien.nama) LIKE LOWER(CONCAT('%', :namaPasien, '%'))) " +
            "AND (:paketMCU IS NULL OR p.paketMCU.id = :paketMCU) " +
            "AND (:tanggalPendaftaran IS NULL OR p.tanggalPendaftaran = :tanggalPendaftaran)")
    List<PendaftaranMCU> cariReservasi(@Param("namaPasien") String namaPasien,
                                       @Param("paketMCU") Long paketMCU,
                                       @Param("tanggalPendaftaran") LocalDate tanggalPendaftaran);

    @Query("SELECT p FROM PendaftaranMCU p WHERE p.status = 'Progres' ORDER BY p.tanggalPendaftaran DESC")
    List<PendaftaranMCU> findProgresOrderedByTanggal();
}
