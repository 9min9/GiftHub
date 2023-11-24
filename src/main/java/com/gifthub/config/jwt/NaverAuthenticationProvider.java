package com.gifthub.config.jwt;

import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        String naverAccountId = (String) principal;

        NaverUser findUser = userRepository.findByNaverId(naverAccountId).orElse(null);

        if (findUser != null) {
            UserDetails userDetails = findUser;
            return new SocialAuthenticationToken(userDetails, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
