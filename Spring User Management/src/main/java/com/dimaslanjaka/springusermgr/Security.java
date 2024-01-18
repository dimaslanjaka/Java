package com.dimaslanjaka.springusermgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class Security {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    DataSource dataSource;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable csrf for @PostMapping
                // fixed for 403 forbidden ajax request
                .csrf(AbstractHttpConfigurer::disable)
                // setup endpoint security
                .authorizeHttpRequests((authorize) -> authorize
                        // admin area
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/add/**").hasRole("ADMIN")
                        .requestMatchers("/delete/**").hasRole("ADMIN")
                        .requestMatchers("/edit/**").hasRole("ADMIN")

                        // need login area
                        // .requestMatchers("/dashboard").authenticated() // dashboard already have programmatic authenticated checker

                        // allow all non configured endpoint from above
                        // like css, js, and other static assets
                        .anyRequest().permitAll())
                // login endpoint
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/dashboard")
                                .permitAll())
                // logout endpoint /logout
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(
                                        new AntPathRequestMatcher("/logout"))
                                .permitAll());
        return http.build();
    }
}
