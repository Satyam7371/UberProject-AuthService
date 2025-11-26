package org.example.uberprojectauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements CommandLineRunner {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    public String createToken(Map<String, Object> payload, String email){

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry*1000L);

        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .subject(email)
                .signWith(getSignKey())
                .compact();
    }

    public String createToken(String email){
        return createToken(new HashMap<>(), email);
    }

    public Key getSignKey() {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return key;
    }


    public Claims extractAllPayloads(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllPayloads(token);
        return claimsResolver.apply(claims);
    }

    // this method will extract the exact info from the payload like expiration
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // This method checks if the token was expired before the current timestamp or not?
    public Boolean isTokenExpired(String token){
        return  extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String email){
        return  extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    public Object extractPayload(String token, String payloadKey){
        Claims claims = extractAllPayloads(token);
        return claims.get(payloadKey);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("email", "abc@gmail.com");
        payload.put("phoneNumber", "123456789");

        String token = createToken(payload,payload.get("email").toString());
        System.out.println("Generated token is : " + token);

        System.out.println((extractPayload(token, "email")));
    }
}
