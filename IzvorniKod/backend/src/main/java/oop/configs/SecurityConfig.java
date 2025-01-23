package oop.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oop.filter.H2Filter;
import oop.filter.JwtFilter;
import oop.service.CustomOAuth2UserService;
import oop.service.JWTService;
import oop.service.MyUserDetailsService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

//    @Autowired
//    public SecurityConfig(MyUserDetailsService myUserDetailsService){
//        this.myUserDetailsService = myUserDetailsService;
//    }

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private H2Filter h2Filter;


    @Autowired
    private CustomOAuth2UserService customOauth2UserService;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable());
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/h2-console/**", "/register", "/login").permitAll()
//                .anyRequest().authenticated());
//        http.oauth2Login(oauth2 -> oauth2
//                .userInfoEndpoint(userInfo -> userInfo
//                        .userService(customOauth2UserService)));
//        http.httpBasic(Customizer.withDefaults());
//        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));   //za h2
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/logout", "login?logout", "/api/login/google").permitAll()
                        .requestMatchers("/h2-console/**").authenticated()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("http://localhost:5173/userinfo");
                        }))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("authenticationEntryPoint error -> nemas token");
                            response.sendRedirect("http://localhost:5173/logout"); //ako nisi autoriziran
                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));   //za h2
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(h2Filter, jwtFilter.getClass());
        http.cors(withDefaults()); // Enable CORS with default configuration
        http.logout(logout -> logout
                .permitAll()
                .deleteCookies("jwtToken")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                })
        );

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // Specify allowed origins
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){  //ovo je setup da ce spring boot usporedivati passworde sa nase baze podataka
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(myUserDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }
}