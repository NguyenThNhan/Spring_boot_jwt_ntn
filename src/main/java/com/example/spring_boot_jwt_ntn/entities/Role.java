package com.example.spring_boot_jwt_ntn.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.Permission;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table()
@Getter
@Setter
public class Role extends BaseEntity{
    private String roleName;
    private String roleKey;

    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",joinColumns = {
            @JoinColumn(name = "role_id")
    },inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<Permission> permissions = new HashSet<>();
}
