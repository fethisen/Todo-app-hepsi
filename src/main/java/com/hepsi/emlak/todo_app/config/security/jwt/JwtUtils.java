package com.hepsi.emlak.todo_app.config.security.jwt;

import com.hepsi.emlak.todo_app.service.UserDetailsImpl;
import com.hepsi.emlak.todo_app.service.exception.JwtTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${security.authentication.jwt.base64-secret}")
    private String jwtSecret;

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;


    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    public String generateJwtToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date exp = new Date((new Date()).getTime() + tokenValidityInSeconds*1000);// Saniyeyi milisaniyeye çevirme

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .claim("auth",authorities)
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    public String getEmailFromJwtToken(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(key()).build()
                    .parseClaimsJws(token).getBody().getSubject();
        }catch (ExpiredJwtException e) {
            logger.error("JWT token has expired");
            String expiredEmail = e.getClaims().getSubject();
            throw new JwtTokenExpiredException("JWT token has expired",expiredEmail);
        }

    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key()).parseClaimsJws(token).getBody();
    }

}