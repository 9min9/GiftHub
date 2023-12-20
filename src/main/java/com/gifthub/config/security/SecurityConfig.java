package com.gifthub.config.security;

import com.gifthub.config.jwt.JwtAuthenticationFilter;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.entity.enumeration.UserType;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin()
                .loginPage("/login")
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/images/**"),
                                new AntPathRequestMatcher("/scss/**"),
                                new AntPathRequestMatcher("/webfonts/**"),
                                new AntPathRequestMatcher("/static/**")
                        ).permitAll()

                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/", "/signup/**", "/error/**", "/login/**", "/api/local/login**", "api/local/signup/**", "/api/kakao/**", "/api/naver/**", "/logout", "/api/product/**", "/gifticons").permitAll()
                        .requestMatchers("/admin/**").hasRole(UserType.ADMIN.getRole())
                        .anyRequest().authenticated()
                )
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.logout()
                .logoutUrl("/logout")   // 로그아웃 처리 URL (= form action url)
                .logoutSuccessUrl("/")  // 로그아웃 성공 후 targetUrl,
                .addLogoutHandler((request, response, authentication) -> {})
                .logoutSuccessHandler((request, response, authentication) -> {response.sendRedirect("/login");});
        //	.httpBasic();
        return http.build();
    }

}
