package com.epam.esm.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {

    private static final String CODING = "utf-8";
    private static final String MESSAGE = "message";
    private static final String CODE = "code";

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

    public boolean validateJwtToken(String authToken, HttpServletResponse response) throws IOException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException | MalformedJwtException e) {
            HashMap<String, String> map = new HashMap<>();
            map.put(CODE, HttpStatus.UNAUTHORIZED.toString());
            map.put(MESSAGE, e.getLocalizedMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding(CODING);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper objectMapper = new ObjectMapper();
            String resBody = objectMapper.writeValueAsString(map);
            PrintWriter printWriter = response.getWriter();
            printWriter.print(resBody);
            printWriter.flush();
            printWriter.close();
        }
        return false;
    }
}