package com.gifthub.config.security;

import com.gifthub.user.UserJwtTokenProvider;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserJwtTokenProvider jwtTokenProvider;

    private final CorsConfig corsConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    //TEST
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin()
                .loginPage("/login")
                .and()

                .authorizeHttpRequests((authz) -> authz
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .anyRequest().permitAll()
                );

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //	.httpBasic();
        return http.build();
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .formLogin()
//                .loginPage("/login")
//                .and()
//                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers(
//                                new AntPathRequestMatcher("/css/**"),
//                                new AntPathRequestMatcher("/js/**"),
//                                new AntPathRequestMatcher("/images/**"),
//                                new AntPathRequestMatcher("/scss/**"),
//                                new AntPathRequestMatcher("/webfonts/**"),
//                                new AntPathRequestMatcher("/static/**")
//                        ).permitAll()
//
//                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
//                        .requestMatchers("/", "/signup", "/error", "/login/**", "/api/kakao/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilter(corsConfig.corsFilter())
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        //	.httpBasic();
//        return http.build();
//    }


}
