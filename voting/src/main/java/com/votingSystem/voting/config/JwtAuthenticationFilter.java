package com.votingSystem.voting.config;

import com.votingSystem.voting.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* By extending OncePerRequestFilter,
* Spring Security places your JwtAuthenticationFilter in the filter chain,
* so every incoming HTTP request goes through this filter
* before reaching your controllers.
*/
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /*
    * request  : the request coming into server
    * response : the response coming out from server
    * filterChain.doFilter(request, response) simply means:
    *   “I’m done with my part, now pass this request to
    *   the next filter in the chain
    *   (or the controller if there are no more filters).”
    */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        // Get Bearer JWT Token from header
        final String authHeader = request.getHeader("Authorization");
        final String authToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // this request is unauthenticated
            // Move to the next in line (maybe it is a public that doesn't need a token)
            // path security is handled in the Security config
            filterChain.doFilter(request, response);
            return;
        }
        // Extract Token from Header
        authToken = authHeader.substring(7); // "Bearer  <Token>"

        // Extract Username from token
        userEmail = jwtService.extractUsername(authToken); // Extract Email (username) of jwt tpken

        // Check if user not yet authenticated (is not connected yet)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isValidToken(authToken, userDetails)) {
                // This object tells Spring Security:
                //  - Who the user is (userDetails)
                //  - Credentials (null because JWT is already trusted)
                //  - Authorities / roles (from userDetails.getAuthorities())
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Adds extra info to the authentication object, like:
                // - Remote IP address
                // - Session ID
                // - Useful for logging or advanced security checks.
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Store authentication in the Security Manager context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // if authenticated continue to next filter
        filterChain.doFilter(request, response);
    }

}
