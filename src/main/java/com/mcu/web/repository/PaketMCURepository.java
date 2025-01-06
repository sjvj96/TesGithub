package com.mcu.web.repository;

import com.mcu.web.models.PaketMCU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaketMCURepository extends JpaRepository<PaketMCU, Long> {

    List<PaketMCU> findByNama(String nama);

    @Query("SELECT p FROM PaketMCU p WHERE LOWER(p.nama) LIKE LOWER(CONCAT('%', :nama, '%'))")
    List<PaketMCU> findByNamaContainingIgnoreCase(@Param("nama") String nama);

    List<PaketMCU> findByKategori(String kategori);

    @Query("SELECT p FROM PaketMCU p WHERE LOWER(p.kategori) LIKE LOWER(CONCAT('%', :kategori, '%'))")
    List<PaketMCU> findByKategoriContainingIgnoreCase(@Param("kategori") String kategori);

    List<PaketMCU> findByHargaLessThanEqual(Double harga);

    @Query("SELECT p FROM PaketMCU p WHERE p.harga BETWEEN :minHarga AND :maxHarga")
    List<PaketMCU> findByHargaBetween(@Param("minHarga") Double minHarga, @Param("maxHarga") Double maxHarga);

    @Query("SELECT COUNT(p) FROM PaketMCU p WHERE LOWER(p.kategori) LIKE LOWER(CONCAT('%', :kategori, '%'))")
    Long countByKategoriContainingIgnoreCase(@Param("kategori") String kategori);

    List<PaketMCU> findByTipeContainingIgnoreCase(String tipe);

    @Query("SELECT MIN(p.harga) FROM PaketMCU p WHERE p.tipe = :tipe")
    Integer findHargaTermurahByJenisPemeriksaan(@Param("tipe") String tipe);

}
