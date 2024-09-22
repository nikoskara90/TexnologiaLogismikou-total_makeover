package com.icsd19080_icsd19235.service;

import com.icsd19080_icsd19235.model.User;
import com.icsd19080_icsd19235.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if (user.getUserId() == null || !userRepository.existsById(user.getUserId())) {
            throw new RuntimeException("User not found");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

 @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user by username: " + username); //DEBUG

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User not found with username: " + username); //DEBUG
                    return new UsernameNotFoundException("User not found");
                });

        System.out.println("User found: " + user.getUsername() + ", proceeding with authentication"); //DEBUG

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public User login(User user) {
        System.out.println("Attempting login for user: " + user.getUsername()); //DEBUG

        User foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    System.out.println("User not found in database for username: " + user.getUsername()); //DEBUG
                    return new RuntimeException("User not found");
                });

        if (user.getPassword().equals(foundUser.getPassword())) {
            System.out.println("Password match for user: " + foundUser.getUsername()); //DEBUG
            return foundUser;
        } else {
            System.out.println("Password mismatch for user: " + foundUser.getUsername()); //DEBUG
            return null;
        }
    }
}

