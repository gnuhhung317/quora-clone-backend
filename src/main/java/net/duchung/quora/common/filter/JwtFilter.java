package net.duchung.quora.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.duchung.quora.common.exception.JwtAuthenticationException;
import net.duchung.quora.common.security.CustomUserDetailsService;
import net.duchung.quora.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if(isByPassUrl(request.getRequestURI())) {
                filterChain.doFilter(request,response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                String token = authHeader.substring(7);

                String phoneNumber = jwtUtil.extractEmail(token);
                if (phoneNumber !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService
                            .loadUserByUsername(phoneNumber);
                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                    }
                }
                filterChain.doFilter(request,response);

            }else {
                throw new JwtAuthenticationException("Token's invalid");
            }
        } catch (JwtAuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
    public boolean isByPassUrl(String url) {
        return url.contains("/auth/login") || url.contains("/auth/register")||url.contains("/swagger-ui")||url.contains("/v3/api-docs");
    }
}
