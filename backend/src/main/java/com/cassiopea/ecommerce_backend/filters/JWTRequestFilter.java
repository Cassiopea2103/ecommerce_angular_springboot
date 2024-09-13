package com.cassiopea.ecommerce_backend.filters ;

import com.cassiopea.ecommerce_backend.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetialsService ;
    private final JWTUtil jwtUtil ;

    @Override
    protected void doFilterInternal (
            HttpServletRequest request , HttpServletResponse response , FilterChain filterChain
    ) throws ServletException, IOException {
        // retrieve auth headers :
        String authHeader = request.getHeader("Authorization") ;

        String token = null ;
        String username = null ;

        if ( authHeader != null && authHeader.startsWith ("Bearer ")) {
            // set the token :
            token = authHeader.substring( 7 ) ;
            // set username :
            username = jwtUtil.getTokenUsername ( token ) ;
        }


        // if username present in token , but usename not authenticated :
        if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
            // retrieve the user by its username :
            UserDetails userDetails = userDetialsService.loadUserByUsername ( username ) ;

            // if the token if valid :
            if ( jwtUtil.isTokenValid( token , userDetails) ) {

                // authenticate the user with username and password :
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(  userDetails , null ) ;

                // set the details for the authentication token :
                authToken.setDetails ( new WebAuthenticationDetailsSource ().buildDetails(request ) ) ;

                // set the token to the authentication token created :
                SecurityContextHolder.getContext().setAuthentication ( authToken ) ;
            }
        }

        filterChain.doFilter ( request , response ) ;
    }
}