package com.mcu.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "pasien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pasien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Nama harus antara 2 hingga 100 karakter")
    private String nama;

    @NotBlank(message = "Alamat tidak boleh kosong")
    @Size(max = 255, message = "Alamat tidak boleh lebih dari 255 karakter")
    private String alamat;

    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    @Pattern(regexp = "\\d{10,15}", message = "Nomor telepon harus berupa angka dan memiliki panjang 10-15 karakter")
    private String nomorTelepon;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Kolom untuk menyimpan ID User
    private User user;
}
