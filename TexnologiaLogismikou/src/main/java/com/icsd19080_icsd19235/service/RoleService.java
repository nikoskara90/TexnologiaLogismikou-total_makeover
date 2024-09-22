package com.icsd19080_icsd19235.service;

import com.icsd19080_icsd19235.model.*;
import com.icsd19080_icsd19235.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role createRole(Role role) {
        User user = userRepository.findById(role.getUser().getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        role.setUser(user);
        return roleRepository.save(role);
    }
    
    public Role updateRole(Long id, Role roleDetails) {
        return roleRepository.findById(id).map(role -> {
            role.setRoleName(roleDetails.getRoleName());
            role.setUser(roleDetails.getUser());
            return roleRepository.save(role);
        }).orElse(null);
    }
    
    public void assignRoleToUser(Long userId, Long conferenceId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    
        Role.RoleName roleEnum;
        try {
            roleEnum = Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }
    
        Role role = new Role(user, roleEnum);
        roleRepository.save(role);
    }
    
    

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
