package pt.ua.deti.tqs.roadrunnerbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.roadrunnerbackend.config.JwtConfig;
import pt.ua.deti.tqs.roadrunnerbackend.model.User;
import pt.ua.deti.tqs.roadrunnerbackend.model.enums.Roles;

import java.security.Key;
import java.util.Date;

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
        log.info("JwtTokenService -- generateToken -- Request received");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration() * 1000L);

        log.info("JwtTokenService -- generateToken -- Request Success");
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", user.getRole())
                .signWith(jwtKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String tokenRequest) {
        log.info("JwtTokenService -- validateToken -- Request received");
        String token = tokenRequest.replace("Bearer ", "");
        try {
            log.info("JwtTokenService -- validateToken -- Request Success");
            Date expiration = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(token).getBody().getExpiration();
            return !expiration.before(new Date());
        } catch (Exception e) {
            log.info("JwtTokenService -- validateToken -- Request Failed");
            return false;
        }
    }
    

    public String getEmailFromToken(String tokenRequest) {
        log.info("JwtTokenService -- getEmailFromToken -- Request received");
        String token = tokenRequest.replace("Bearer ", "");
        try {
            log.info("JwtTokenService -- getEmailFromToken -- Request Success");
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch (Exception e) {
            log.info("JwtTokenService -- getEmailFromToken -- Request Failed");
            return null;
        }
    }

    public Roles getRoleFromToken(String tokenRequest) {
        log.info("JwtTokenService -- getRoleFromToken -- Request received");
        String token = tokenRequest.replace("Bearer ", "");
        try {
            log.info("JwtTokenService -- getRoleFromToken -- Request Success");
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(token);
            return Roles.valueOf(claims.getBody().get("role", String.class));
        } catch (Exception e) {
            log.info("JwtTokenService -- getRoleFromToken -- Request Failed");
            return null;
        }
    }

}

