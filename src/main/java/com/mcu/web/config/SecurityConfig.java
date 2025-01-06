package com.mcu.web.config;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .formLogin(httpForm -> {
//                    httpForm
//                            .loginPage("/login")
//                            .usernameParameter("email")// Halaman login
//                            .loginProcessingUrl("/login")  // URL pemrosesan login
//                            .defaultSuccessUrl("/", true).permitAll();  // Redirect setelah login berhasil
//                })
//                .authorizeHttpRequests(authorize -> {
//                    authorize.requestMatchers("/login", "/login/signup", "/login/signup/post", "/logout").permitAll();
//                    authorize.requestMatchers("/**").authenticated();
//                    authorize.anyRequest().authenticated();
//                })
//                .logout(config -> config.logoutSuccessUrl("/"))
//                .build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
