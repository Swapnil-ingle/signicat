package com.swapnil.signicat.filter;

import com.swapnil.signicat.exception.JwtTokenValidationException;
import com.swapnil.signicat.exception.UserNotFoundException;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.security.JwtProvider;
import com.swapnil.signicat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtTokenVerificationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJwtTokenFromRequest(request);

        if (token == null) {
            log.info("No JWT token received from client, skipping the verification part.");
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtProvider.validateToken(token)) {
            throw new JwtTokenValidationException(request.getHeader("Authorization"));
        }

        Long userId = Long.valueOf(jwtProvider.getUsernameFromJwt(token));
        Optional<Subject> user = userService.getByUserId(userId);

        if (user.isEmpty()) {
            log.error("Could not validate the JWT Token as user({}) associated with the token's 'sub' does not exists", userId);
            throw new UserNotFoundException(userId);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getUsername());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("JWT Token successfully verified!");
        filterChain.doFilter(request, response);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
