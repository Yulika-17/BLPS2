package com.nullnumber1.lab1.security;

import com.nullnumber1.lab1.exception.not_found.UserNotFoundException;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PostgresUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public PostgresUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        log.error("user logged in");
        log.error(user.getRole().name());
        return new User(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
}