package com.icsd19080_icsd19235.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.icsd19080_icsd19235.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(Role.RoleName roleName);
}

