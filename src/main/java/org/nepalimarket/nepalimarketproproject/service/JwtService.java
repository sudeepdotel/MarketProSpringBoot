package org.nepalimarket.nepalimarketproproject.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    public static final String SECRET = "867wr78276r87r68rwBBB-08-0-09-FHH9089080980F097890870kjdshkjsdh";

    public String generateToken ( String userName ) {
        Map<String, Object> claims = new HashMap<> ( );
        return createToken ( claims, userName );
    }

    private String createToken ( Map<String, Object> claims, String userName ) {
        return Jwts.builder ( )
                .setClaims ( claims )
                .setSubject ( userName )
                .setIssuedAt ( new Date ( System.currentTimeMillis ( ) ) )
                .setExpiration ( new Date ( System.currentTimeMillis ( ) + 1000 * 60 * 30 ) )
                .signWith ( getSignKey ( ), SignatureAlgorithm.HS256 ).compact ( );
    }

    private Key getSignKey ( ) {
        byte[] keyBytes = Decoders.BASE64URL.decode ( SECRET );
        return Keys.hmacShaKeyFor ( keyBytes );
    }

    public String extractUserName ( String token ) {
        return extractClaims ( token, Claims::getSubject );
    }

    public Date extractExpirationDate ( String token ) {
        return extractClaims ( token, Claims::getExpiration );
    }

    public <T> T extractClaims ( String token, Function<Claims, T> claimResolver ) {
        final Claims claims = this.extratcAllClaims ( token );
        return claimResolver.apply ( claims );
    }

    private Claims extratcAllClaims ( String token ) {
        return Jwts.parserBuilder ( )
                .setSigningKey ( getSignKey ( ) )
                .build ( )
                .parseClaimsJws ( token )
                .getBody ( );
    }

    private Boolean isTokenExpired ( String token ) {
        return extractExpirationDate ( token ).before ( new Date ( ) );
    }

    public Boolean validateToken ( String token, UserDetails userDetails ) {
        final String userName = extractUserName ( token );
        return (userName.equals ( userDetails.getUsername ( ) ) && !this.isTokenExpired ( token ));
    }
}