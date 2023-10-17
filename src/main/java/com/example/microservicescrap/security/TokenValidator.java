package com.example.microservicescrap.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class TokenValidator {

    @Value("${jwt.secret}")
    private String secret;

    public boolean validarToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            // Verificar tiempo de expiración
            Date expirationDate = claims.getBody().getExpiration();
            Date now = new Date();

            if (expirationDate != null && now.before(expirationDate)) {
                // El token no ha expirado
                return true;
            } else {
                // El token ha expirado
                return false;
            }

        } catch (Exception e) {
            // La firma no es válida o ha ocurrido un error.
            return false;
        }
    }


    public Claims getClaim(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        Claims data = claims.getBody();
        return data;
    }

}
