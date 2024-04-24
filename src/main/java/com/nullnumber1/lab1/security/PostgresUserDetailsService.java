package com.nullnumber1.lab1.security;

import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class PostgresUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public PostgresUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
    }
}
