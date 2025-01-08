package oop.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import oop.model.User;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private HttpServletResponse response;

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("Executing CustomOAuth2UserService loadUser method");
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // Extract user details
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Check if user exists in the database
        if (userRepository.findByEmail(email) == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            userRepository.save(newUser);
        }

        // Generate JWT token
        String token = jwtService.generateToken(name);
        System.out.println(token);

        // Set token as HTTP-only cookie
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Ensure the cookie is only sent over HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // Set cookie expiration to 1 day
        response.addCookie(cookie);

        return oAuth2User;
    }
}