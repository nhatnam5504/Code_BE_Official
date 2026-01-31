package com.example.code_be.service;

import com.example.code_be.entity.User;
import com.example.code_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public Optional<User> login(String pin) {
        return userRepository.findByPin(pin);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsernameAndPin(username, password);
    }

    public User getPartner(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getPartnerId() != null) {
            return userRepository.findById(user.getPartnerId()).orElse(null);
        }
        return null;
    }
}
