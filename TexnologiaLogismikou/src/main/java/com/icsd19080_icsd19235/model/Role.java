package com.icsd19080_icsd19235.model;

import javax.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;

    public Role() {
    }

    public Role(User user, RoleName roleName) {
        this.user = user;
        this.roleName = roleName;
    }

    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public RoleName getRoleName() { return roleName; }
    public void setRoleName(RoleName roleName) { this.roleName = roleName; }

    public enum RoleName {
        VISITOR, AUTHOR, PC_MEMBER, PC_CHAIR
    }
}
