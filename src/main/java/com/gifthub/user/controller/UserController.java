package com.gifthub.user.controller;

import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    @PostMapping("/myinfo")
    public ResponseEntity<Object> myInfo(@RequestHeader HttpHeaders headers) {
        try {
            System.out.println("CONTR");
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));


            UserDto findUser = userService.getUserById(userId);


            return ResponseEntity.ok().body(findUser);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/points")
    public Long getPoint(@RequestHeader HttpHeaders headers) {
        Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

        return userService.getUserById(userId).getPoint();
    }

}
