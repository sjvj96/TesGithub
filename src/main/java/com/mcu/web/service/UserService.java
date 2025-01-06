package com.mcu.web.service;

import com.mcu.web.models.User;
import com.mcu.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends AbstractUserService{

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAdmins() {
        return userRepository.findByRole("Admin"); // Mencari semua pengguna dengan role 'Admin'
    }

    @Override
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public void register(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
