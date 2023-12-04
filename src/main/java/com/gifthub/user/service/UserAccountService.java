package com.gifthub.user.service;

import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;

    public boolean duplicateEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElse(null);
        if (findUser == null) {
            return false;
        }
        return true;
    }


    public boolean validatePassword(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return false;
        }
            return true;

    }

    public boolean validateNickname(String nickname){
        User findNickname = userRepository.findByNickname(nickname).orElse(null);
        if(findNickname == null){
            return false;
        }
        return  true;

    }
    public boolean validateTel(String tel){
        User findTel = userRepository.findByTel(tel).orElse(null);
        if(findTel == null){
            return false;
        }
        return  true;

    }

    public boolean validateAccount(String email, String password){
        User findaccountemail = userRepository.findByEmail(email).orElse(null);
       String pwd= findaccountemail.getPassword();
       String email2= findaccountemail.getEmail();
        System.out.println("test:"+pwd);
        System.out.println("test:" +email2);

        // email, pwd가 서버에 있고 , email , pwd 로 가져온 정보가 서로 일치하면

        if(findaccountemail !=null  && pwd.equals(password)){
            System.out.println("true");

            System.out.println("findaccount:"+findaccountemail);

            return true;
        }
        System.out.println("false");

        System.out.println("findaccount:"+findaccountemail);
        return false;
    }

}

