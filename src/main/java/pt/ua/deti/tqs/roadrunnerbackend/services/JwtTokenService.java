package pt.ua.deti.tqs.roadrunnerbackend.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.config.JwtConfig;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

import java.security.Key;

@Slf4j
@Service
public class JwtTokenService {

    private final JwtConfig jwtConfig;
    private final Key jwtKey;

    @Autowired
    public JwtTokenService(JwtConfig jwtConfig, Key jwtKey) {
        this.jwtConfig = jwtConfig;
        this.jwtKey = jwtKey;
    }

    public String generateToken(User user) {
        return null;
    }

    public boolean validateToken(String tokenRequest) {
        return false;
    }
    

    public String getEmailFromToken(String tokenRequest) {
        return null;
    }

    public Roles getRoleFromToken(String tokenRequest) {
        return null;
    }

}

