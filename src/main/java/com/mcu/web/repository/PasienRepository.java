package com.mcu.web.repository;

import com.mcu.web.models.Pasien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasienRepository extends JpaRepository<Pasien, Long> {

    List<Pasien> findByNama(String nama);

    @Query("SELECT p FROM Pasien p WHERE LOWER(p.nama) LIKE LOWER(CONCAT('%', :nama, '%'))")
    List<Pasien> findByNamaContainingIgnoreCase(@Param("nama") String nama);

    Pasien findByNomorTelepon(String nomorTelepon);

    @Query("SELECT p FROM Pasien p WHERE LOWER(p.alamat) LIKE LOWER(CONCAT('%', :alamat, '%'))")
    List<Pasien> findByAlamatContainingIgnoreCase(@Param("alamat") String alamat);

    @Query("SELECT COUNT(p) FROM Pasien p WHERE LOWER(p.alamat) LIKE LOWER(CONCAT('%', :alamat, '%'))")
    Long countByAlamatContainingIgnoreCase(@Param("alamat") String alamat);

    @Query("SELECT p FROM Pasien p WHERE p.id = :id OR LOWER(p.nama) LIKE LOWER(CONCAT('%', :nama, '%'))")
    List<Pasien> findByIdOrNama(@Param("id") Long id, @Param("nama") String nama);

}
