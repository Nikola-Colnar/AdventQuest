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
public class H2Filter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    private static final List<String> PROTECTED_PATHS = List.of("/h2-console/**");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {





        if (request.getRequestURI().startsWith("/h2-console")) {
            System.out.println("Protected path, checking authorization");


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
                    if (userDetails instanceof oop.model.User) {
                        oop.model.User user = (oop.model.User) userDetails;
                        if (user.getIsAdmin() == 1) {
                            //Postavljanje korisnika u SecurityContext
                            Authentication authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication); // Ažuriranje SecurityContext
                            // User is admin, proceed with the filter chain
                            filterChain.doFilter(request, response);
                            return;
                        } else {
                            System.out.println("User is not an admin");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            return;
                        }
                    }

                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    System.out.println("Userdetails nije instanca oop.model.user");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        } else {
            System.out.println("Public path, no authorization needed");
        }
        filterChain.doFilter(request, response);
    }



}