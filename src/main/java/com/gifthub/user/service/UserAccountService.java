package com.gifthub.user.service;

import com.gifthub.user.entity.User;
import com.gifthub.user.exception.*;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;

    public boolean isDuplicateEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElse(null);
        if (findUser == null) {
            return false;
        }
        return true;
    }

    public boolean isMatchPasswordAndConfirmPassword(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    public boolean isDuplicateNickname(String nickname) {
        User findNickname = userRepository.findByNickname(nickname).orElse(null);
        if (findNickname == null) {
            return false;
        }
        return true;
    }

    public boolean isDuplicateTel(String tel) {
        User findTel = userRepository.findByTel(tel).orElse(null);
        if (findTel == null) {
            return false;
        }
        return true;

    }

    public boolean isLogin(String inputEmail, String inputPassword) throws NotFoundUserException{
        User findUser = userRepository.findByEmail(inputEmail).orElse(null);

        if(findUser == null) {
            throw new NotFoundUserException();      //아이디 또는 비밀번호가 일치하지 않습니다.
        }

        if(!findUser.getPassword().equals(inputPassword)) {
            throw new MismatchPasswordException();    //아이디 또는 비밀번호가 일치하지 않습니다.
        }

        return true;
    }

    public boolean validateDuplicateEmail(String email) throws DuplicateEmailException{
        User findUser = userRepository.findByEmail(email).orElse(null);
        if (findUser == null) {
            return true;
        } else {
            throw new DuplicateEmailException();
        }
    }

    public boolean validatePassword(String email, String password) throws NotFoundUserException, MismatchPasswordException{
        User findUser = userRepository.findByEmail(email).orElse(null);

        if (findUser == null) {
            throw new NotFoundUserException();
        }
        //todo: Encoding
        if (!findUser.getPassword().equals(password)) {
            throw new MismatchPasswordException();
        }
        return true;
    }

    public boolean validateMatchPasswordAndConfirmPassword(String password, String confirmPassword) throws MismatchPasswordAndConfirmPassword{
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            throw new MismatchPasswordAndConfirmPassword();
        }
    }

    public boolean validateDuplicateNickname(String nickname) throws DuplicateNicknameException{
        User findUser = userRepository.findByNickname(nickname).orElse(null);
        if (findUser == null) {
            return true;
        } else {
            throw new DuplicateNicknameException();
        }
    }

    public boolean validateDuplicateTel(String tel) throws DuplicateTelException {
        User findUser = userRepository.findByTel(tel).orElse(null);
        if (findUser == null) {
            return true;
        } else {
            throw new DuplicateTelException();
        }
    }


}

