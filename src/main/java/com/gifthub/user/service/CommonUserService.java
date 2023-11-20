package com.gifthub.user.service;

import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.User;
import com.gifthub.user.exception.DuplicateEmailException;
import com.gifthub.user.repository.KakaoUserRepository;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonUserService {

    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;


    public boolean duplicateEmailValidate(String email) throws DuplicateEmailException{
        KakaoUser findKakaoUser = kakaoUserRepository.findByEmail(email).orElse(null);
        User findUser = userRepository.findByEmail(email).orElse(null);

        if(findKakaoUser != null && findUser != null) {
            throw new DuplicateEmailException();
        }


//        KakaoUser kakaoUser = kakaoUserRepository.findByEmail(email).orElseThrow(() -> new DuplicateEmailException());
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new DuplicateEmailException());

        return true;
    }


}
