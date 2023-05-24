package com.metrix.webportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String USERQUERY = "select usrcode, password, isactive from myusers where usrcode = ? and isactive = 1";
    private static final String ROLEQUERY = "select usrcode, roles from myroles where usrcode = ?";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource,
            PasswordEncoder passwordEncoder) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder)
                .dataSource(dataSource)
                .usersByUsernameQuery(USERQUERY)
                .authoritiesByUsernameQuery(ROLEQUERY);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService users) throws Exception {
        http
                .logout(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/logout", "/resources/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .sessionManagement(sessions -> sessions
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1)
                                .expiredUrl("/login?expired")))
                .rememberMe(rememberMe -> rememberMe.userDetailsService(users))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/logout", "/resources/**", "/h2-console/**"));
        return http.build();
    }
}
