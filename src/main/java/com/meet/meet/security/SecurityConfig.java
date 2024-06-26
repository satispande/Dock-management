package com.meet.meet.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                authorize -> authorize
                            .requestMatchers("/login", "/register", "/allocateDock", "/undock/**", "/css/**", "/js/**", "/uploads/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
            )
            .formLogin(form -> form.loginPage("/login")
                                    .defaultSuccessUrl("/dockTag",true)
                                    .loginProcessingUrl("/login")
                                    .failureUrl("/login?fail=true")
                                    .permitAll()
            ).rememberMe(
                rememberMe -> rememberMe.rememberMeParameter("remember-me")
                                        .tokenValiditySeconds(43200) // 1 day
            )
            .logout(logout -> logout.logoutRequestMatcher(
                new AntPathRequestMatcher("/logout")
            ).permitAll());
        
        http.csrf().disable();


        return http.build();
    }
    
    

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

}
