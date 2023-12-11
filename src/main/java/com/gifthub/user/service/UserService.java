package com.gifthub.user.service;

import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.dto.NaverUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.gifthub.user.entity.enumeration.UserType.ADMIN;
import static com.gifthub.user.entity.enumeration.UserType.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public LocalUserDto saveLocalUser(LocalUserDto localUserDto) {
        User findUser = userRepository.findByEmail(localUserDto.getEmail()).orElse(null);
        LocalUserDto resultDto;

        if (findUser != null) {
            findUser.changeNickname(localUserDto.getNickname());
            findUser.changeTel(localUserDto.getTel());
            resultDto = userRepository.save((LocalUser) findUser).toLocalUserDto();

        } else {
            if (localUserDto.getEmail().equals("admin")) {
                localUserDto.setUserType(ADMIN);
            }

            localUserDto.setUserType(USER);
            localUserDto.setPoint(0L);

            resultDto = userRepository.save(localUserDto.toLocalUserEntity()).toLocalUserDto();
        }
        return resultDto;
    }

    public KakaoUserDto saveKakaoUser(KakaoUserDto kakaoUserDto) {
        kakaoUserDto.setPoint(0L);
        KakaoUser saveUser = userRepository.save(kakaoUserDto.toEntity());
        return saveUser.toKakaoUserDto();
    }

    public NaverUserDto saveNaverUser(NaverUserDto naverUserDto) {
        naverUserDto.setPoint(0L);
        NaverUser save = userRepository.save(naverUserDto.toNaverEntity());
        return save.toNaverUserDto();
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        return user.toDto();
    }

    public List<UserDto> getUserByNickname(String nickname) {
        return userRepository.findUserByNickname(nickname).stream().map(User::toDto).toList();
    }

    public void delUserById(Long userId) {
        userRepository.deleteById(userId);
    }


}
