package com.gifthub.user.service;

import com.gifthub.user.dto.LocalUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.LocalUser;
import com.gifthub.user.repository.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalUserService {
    private final UserRepository userRepository;


    public LocalUserDto getLocalUserByEmail(String email){
        LocalUser localUser =(LocalUser) userRepository.findByEmail(email).orElse(null);

        if(localUser !=null){
            return localUser.toLocalUserDto();
        }
        return null;
    }


    public UserDto getLocalUserDto(String data){


    JsonParser parser = new JsonParser();
    JsonElement element = parser.parse(data);
    String email=element.getAsJsonObject().get("email").getAsString();
    String password=element.getAsJsonObject().get("password").getAsString();
    String name= element.getAsJsonObject().get("name").getAsString();
    String gender = element.getAsJsonObject().get("gender").getAsString();
    String nickname = element.getAsJsonObject().get("nickname").getAsString();
    String tel = element.getAsJsonObject().get("tel").getAsString();
    String birth = element.getAsJsonObject().get("birth").getAsString();
    String year = birth.substring(0,4);
    String birthday = birth.substring(4);

        System.out.println("birth:"+birth);
        System.out.println("birthday"+birthday);
        System.out.println("year"+year);
        System.out.println("tel"+tel);
    UserDto userDto =UserDto.builder()
            .password(password)
            .email(email)
            .name(name)
            .gender(gender)
            .nickname(nickname)
            .tel(tel)
            .date(birthday)
            .year(year)
            .build();


    return userDto;
}
}
