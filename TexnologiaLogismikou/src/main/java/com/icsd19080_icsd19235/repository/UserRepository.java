package com.icsd19080_icsd19235.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.icsd19080_icsd19235.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFullName(String fullName);
    
    Optional<User> findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
