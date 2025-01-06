package com.mcu.web.service;

import com.mcu.web.models.User;
import com.mcu.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractUserService implements UserServiceInterface {
    @Autowired
    protected UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user); // Implementasi dasar untuk menyimpan user
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id); // Implementasi dasar untuk menghapus user
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll(); // Menghapus semua data pengguna
    }
}
