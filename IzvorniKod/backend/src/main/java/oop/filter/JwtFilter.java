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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    private static final List<String> UNPROTECTED_PATHS = List.of("/", "/register", "/login", "/logout", "login?logout", "/api/login/google", "/h2-console");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!UNPROTECTED_PATHS.contains(request.getRequestURI())) {
            System.out.println("Protected path, checking authorization");

            String token = null;
            String username = null;
            System.out.println("Checking authorization of endpoint: " + request.getRequestURI());
            // Extract token from cookies
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("jwtToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if(token == null){
                System.out.println("No token");
            }

            if (token != null) {;
                username = jwtService.extractUserName(token);
                System.out.println("Username: " + username);
            }

            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: No token provided");
                return;
            }

            if (username != null) {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    // Postavljanje korisnika u SecurityContext
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Invalid token");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: No username extracted from token");
                return;
            }
        } else {
            System.out.println("Public path, no authorization needed");
        }
        filterChain.doFilter(request, response);
    }



}