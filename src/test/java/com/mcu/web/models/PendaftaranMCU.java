package com.mcu.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pendaftaran_mcu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendaftaranMCU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull(message = "Pasien tidak boleh null")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pasien_id", nullable = false)
    private Pasien pasien;

//    @NotNull(message = "Paket MCU tidak boleh null")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "paket_mcu_id", nullable = false)
    private PaketMCU paketMCU;

    @NotNull(message = "Tanggal pendaftaran tidak boleh null")
    private LocalDate tanggalPendaftaran;

    @NotNull(message = "Status tidak boleh null")
    private String status;  // Add status field (Progres, Selesai, etc.)

}
