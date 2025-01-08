package oop.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oop.service.JWTService;
import oop.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    private static final List<String> PROTECTED_PATHS = new ArrayList<>(List.of("/students"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Apply filter only to protected endpoints
        if (!PROTECTED_PATHS.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Unauthorized access, initiating authorization");

        String token = null;
        String username = null;

        // Extract token from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            username = jwtService.extractUserName(token);
            System.out.println("Username: " + username);
        }

        if (username != null) {
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return; // Stop the filter chain
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // Stop the filter chain
        }

        filterChain.doFilter(request, response);
    }

    public static void addProtectedPath(String path) {
        PROTECTED_PATHS.add(path);
    }
}