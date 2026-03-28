package ma.ens.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Je récupère la clé depuis application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // La clé signée qu'on utilisera pour signer et vérifier les tokens
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Génère un token JWT pour un utilisateur donné
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                    // L'identité de l'utilisateur
                .setIssuedAt(new Date())                 // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature
                .compact();                              // Génère la chaîne finale
    }

    // Extrait le nom d'utilisateur depuis un token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Vérifie si le token est valide (signature correcte + pas expiré)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token invalide: signature erronée, expiré, ou mal formé
            System.out.println("Token invalide: " + e.getMessage());
            return false;
        }
    }
}