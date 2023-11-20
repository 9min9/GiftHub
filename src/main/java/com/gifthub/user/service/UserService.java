package com.gifthub.user.service;

import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto save(UserDto userdto){
        User user = userRepository.save(userdto.toEntity());

        return user.toDto();
    }

    public UserDto getUser(Long id){
     User user = userRepository.findById(id).orElse(null);

     if(user==null){
         return null;
     }

     return user.toDto();
    }

    public void delUser(Long id){
     userRepository.deleteById(id);

    }

}
