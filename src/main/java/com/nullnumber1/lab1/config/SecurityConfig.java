package com.nullnumber1.lab1.config;

import com.nullnumber1.lab1.security.MyBasicAuthenticationEntryPoint;
import com.nullnumber1.lab1.util.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final MyBasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          MyBasicAuthenticationEntryPoint basicAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.basicAuthenticationEntryPoint = basicAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint).and()
                .authorizeRequests()
                .antMatchers("/payments/**").hasAnyAuthority(RoleType.CUSTOMER.name())
                .antMatchers("/admin/**").hasAnyAuthority(RoleType.ADMIN.name())
                .and().httpBasic()
                .and().authorizeRequests().antMatchers("/register").permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
