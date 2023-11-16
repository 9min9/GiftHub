package com.gifthub.point.service;


import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PointService {

    private final UserRepository userRepository;

    public UserDto plusPoint(Long price, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.plusPoint(price);

        userRepository.save(user);

        return user.toDto();
    }

    public UserDto usePoint(Long price, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        if (user.getPoint() < price) {
            return null;
        }

        user.usePoint(price);

        userRepository.save(user);

        return user.toDto();
    }

}
