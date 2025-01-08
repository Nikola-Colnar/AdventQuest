package oop.service;

import oop.model.MyUserDetails;
import oop.model.User;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("loadam usera " + user.getUsername());
        return new MyUserDetails(user);
    }
}
