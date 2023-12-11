package com.fridgerator.ginormitron.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import de.codecentric.boot.admin.server.config.AdminServerProperties;
// import io.netty.handler.codec.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

    // private final AdminServerProperties adminServer;

    // public SecurityConfig(AdminServerProperties adminServer) {
    //     this.adminServer = adminServer;
    // }

    private static final String[] AUTH_WHITELIST = {
        "/public",
        "/instances",
        "/instances/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // .requestMatchers("/public").permitAll()
            .requestMatchers(AUTH_WHITELIST).permitAll()
            // .requestMatchers("/instances/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login();

        // http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests.anyRequest().permitAll())
        //     .csrf().disable();

        return http.build();
    }
}
