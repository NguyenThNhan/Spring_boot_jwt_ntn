package com.example.spring_boot_jwt_ntn.service;

import com.example.spring_boot_jwt_ntn.authen.UserPrincipal;
import com.example.spring_boot_jwt_ntn.entities.User;

public interface UserService {
    User createUser(User user);
    UserPrincipal findByUsername(String username);
}
