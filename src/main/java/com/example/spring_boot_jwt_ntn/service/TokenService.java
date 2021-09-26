package com.example.spring_boot_jwt_ntn.service;

import com.example.spring_boot_jwt_ntn.entities.Token;

public interface TokenService {
    Token createToken(Token token);

    Token findByToken(String token);
}
