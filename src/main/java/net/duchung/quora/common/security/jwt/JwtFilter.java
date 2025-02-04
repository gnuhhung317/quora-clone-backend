package net.duchung.quora.common.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.duchung.quora.common.exception.JwtAuthenticationException;
import net.duchung.quora.common.security.CustomUserDetailsService;
import net.duchung.quora.common.security.JwtBlacklistService;
import net.duchung.quora.data.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtBlacklistService jwtBlacklistService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if(isByPassUrl(request.getRequestURI())) {
                filterChain.doFilter(request,response);
                return;
            }
            try {
                final String authHeader = request.getHeader("Authorization");
                if(authHeader!=null && authHeader.startsWith("Bearer ")){
                    String token = authHeader.substring(7);

                    String email = jwtUtil.extractEmail(token);
                    if (email !=null && !jwtBlacklistService.isBlackListed(token)) {
                        UserDetails userDetails = customUserDetailsService
                                .loadUserByUsername(email);
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
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT expired: " + e.getMessage());
                return; // Stop further processing
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(BaseResponse.error(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage()).toString());
                return; // Stop further processing
            }

    }
    public boolean isByPassUrl(String url) {
        return url.contains("/auth/login") || url.contains("/auth/register")||url.contains("/swagger-ui")||url.contains("/v3/api-docs");
    }
}
