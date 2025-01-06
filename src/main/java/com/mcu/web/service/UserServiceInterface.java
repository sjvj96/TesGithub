package com.mcu.web.service;

import com.mcu.web.models.User;

import java.util.List;

public interface UserServiceInterface {
    User findById(Long id); // Mencari user berdasarkan ID

    User save(User user); // Menyimpan user

    void delete(Long id); // Menghapus user berdasarkan ID

    void deleteAll();

    List<User> findAdmins(); // Mencari admin

    User authenticate(String email, String password); // Autentikasi user berdasarkan email dan password

    void register(User user); // Mendaftarkan user baru

    boolean emailExists(String email); // Mengecek apakah email sudah terdaftar
}
