package com.votingSystem.voting.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String Token) {
        return extractClaim(Token, Claims::getSubject);
    }

    public <T> T extractClaim(String Token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(Token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String Token) {
        return Jwts
                .parserBuilder()                // Creates a new JWT parser (from the jjwt library).
                .setSigningKey(getSigningKey()) // Sets the secret key used to verify / validate the token’s signature.
                .build()                        // Builds the actual parser object.
                .parseClaimsJws(Token)          // Parses the provided token. It will verify the signature using the key you set. If the signature is invalid, it throws an exception.
                .getBody()                      // Returns the Claims, i.e. the payload of the token (things like sub, username, roles, exp, etc.).
                ;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // No claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Extra Claims
    public String generateToken(
            Map<String, Object> extraClaims, // More Claims to insert
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
