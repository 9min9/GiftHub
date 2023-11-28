package com.gifthub.user.controller;

import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.User;
import com.gifthub.user.service.CustomUserDetailsService;
import com.gifthub.user.service.LocalUserService;
import com.gifthub.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/local")
public class LocalUserAccountController {
    private final UserService userService;
    private final LocalUserService localUserService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping(value="/emailcheck")

    public ResponseEntity<Object> emailCheck(@RequestBody String email, HttpSession session ){

        Map<String,String> result = new HashMap<>();
        System.out.println(email);

        if(customUserDetailsService.loadUserByUsername(email)==null){
            result.put("key","1");
            return ResponseEntity.ok().body(result);
        }else {
            result.put("key","2");
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDto userDto, HttpSession session) {

        userService.saveLocalUser(userDto);

        return ResponseEntity.ok("{\"message\": \"Received\"}");
    }


}
