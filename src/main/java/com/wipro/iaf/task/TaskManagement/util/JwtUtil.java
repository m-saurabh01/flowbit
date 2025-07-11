package com.wipro.iaf.task.TaskManagement.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;


@Component
public class JwtUtil {

    private final String SECRET_KEY = "secretkey"; 
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 1; 

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("TaskManagerApp")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String userName) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(userName) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
