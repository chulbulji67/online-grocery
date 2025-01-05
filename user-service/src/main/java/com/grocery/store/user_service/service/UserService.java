package com.grocery.store.user_service.service;



import com.grocery.store.user_service.entity.User;
import com.grocery.store.user_service.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists");
        }
        // Save user
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User Not Found Exception"));
    }

    public Optional<User> getUserProfile(Long id) {
        return userRepository.findById(id);
    }
}
