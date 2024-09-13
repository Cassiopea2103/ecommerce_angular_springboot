package com.cassiopea.ecommerce_backend.utils ;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map ;
import java.util.function.Function;

@Component
public class JWTUtil  {

    public static final String TOKEN_SECRET = "092uanonsaoij209309sosoijawu209328" ;

    // generate token :
    public String generateToken ( String username ) {
        Map < String , Object > claims = new HashMap<>() ;
        return createToken ( claims , username ) ;
    }

    // create token function :
    public String createToken ( Map < String , Object > claims , String username ) {
        // invoke JWT builder to create a token :
        return Jwts.builder()
                .claims ( claims )
                .subject( username )
                .issuedAt ( new Date ( System.currentTimeMillis() ))
                .expiration ( new Date ( System.currentTimeMillis() + 1000*60*30 ) )
                .signWith( getSigningKey () , Jwts.SIG.HS256 )
                .compact() ;
    }

    // get sign key function :
    public SecretKey getSigningKey () {
        return Keys.hmacShaKeyFor( TOKEN_SECRET.getBytes( StandardCharsets.UTF_8 ) );
    }

    // extract claim function :
    public <T> T extractClaim (String token , Function <Claims , T> claimsResolver ) {
        final Claims claims = extractAllClaims ( token ) ;
        return claimsResolver.apply ( claims ) ;
    }

    // extract all claims function :
    private Claims extractAllClaims ( String token ) {
        return Jwts.parser()
                .verifyWith( getSigningKey() )
                .build()
                .parseSignedClaims( token )
                .getPayload() ;
    }

    // extract tokeninfos functions :
    public Date getTokenExpirationDate ( String token ) {
        return extractClaim ( token , Claims::getExpiration ) ;
    }
    public String getTokenUsername ( String token ) {
        return extractClaim ( token , Claims::getSubject ) ;
    }

    // check functions :
    public Boolean isTokenExpired ( String token ) {
        return getTokenExpirationDate( token ).before ( new Date() ) ;
    }

    public Boolean isTokenValid ( String token , UserDetails userDetails ) {
        final String username = getTokenUsername ( token ) ;
        return ( username.equals ( userDetails.getUsername() ) && !isTokenExpired ( token ) ) ;
    }
}