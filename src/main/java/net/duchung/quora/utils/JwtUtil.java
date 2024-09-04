package net.duchung.quora.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.duchung.quora.data.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private int expirationTime;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email",user.getEmail());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Objects.requireNonNull(secretKey).getBytes());
    }
    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
//        System.out.println(claims.get("email"));
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateToken(String token, UserDetails user) {
        final String email = extractEmail(token);
        return (email.equals(user.getUsername()) && !isTokenExpired(token));
    }
}



