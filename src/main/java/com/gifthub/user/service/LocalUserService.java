package com.gifthub.user.service;

import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalUserService {
    private final UserRepository userRepository;

    public LocalUserDto getLocalUserByEmail(String email) {
        LocalUser localUser = (LocalUser) userRepository.findByEmail(email).orElse(null);

        if (localUser != null) {
            return localUser.toLocalUserDto();
        }
        return null;
    }

}
