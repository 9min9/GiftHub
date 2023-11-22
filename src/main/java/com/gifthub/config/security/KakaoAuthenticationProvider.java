package com.gifthub.config.security;

import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        String kakaoAccountId = (String) principal;
        User findUser = userRepository.findByKakaoAccountId(kakaoAccountId).orElse(null);

        if (findUser != null) {
            UserDetails userDetails = findUser;
            return new KakaoAuthenticationToken(userDetails, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return KakaoAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
