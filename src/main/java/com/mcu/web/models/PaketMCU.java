package com.mcu.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paket_mcu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaketMCU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(max = 100, message = "Nama tidak boleh lebih dari 100 karakter")
    private String nama;

    @NotBlank(message = "Tipe tidak boleh kosong")
    @Size(max = 50, message = "Tipe tidak boleh lebih dari 50 karakter")
    private String tipe;

    @NotBlank(message = "Kategori tidak boleh kosong")
    @Size(max = 50, message = "Kategori tidak boleh lebih dari 50 karakter")
    private String kategori;

    @NotNull(message = "Harga tidak boleh kosong")
    @Positive(message = "Harga harus bernilai positif")
    private double harga;
}
