package com.epam.esm.service.security;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            throw new ValidationException(ErrorCode.BAD_REQUEST, "Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            throw new ValidationException(ErrorCode.BAD_REQUEST, "Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ValidationException(ErrorCode.BAD_REQUEST, "JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new ValidationException(ErrorCode.BAD_REQUEST, "JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(ErrorCode.BAD_REQUEST, "JWT claims string is empty: {}", e.getMessage());
        }
    }
}