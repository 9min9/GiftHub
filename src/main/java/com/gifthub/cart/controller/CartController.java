package com.gifthub.cart.controller;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.cart.service.CartService;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;

    @GetMapping
    public ResponseEntity<Object> list(@RequestHeader HttpHeaders headers) {
        Long userId = null;
        try {
            userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인을 해주세요.");
        }

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(cartService.findByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Object> addToCart(Long gifticonId, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = null;
            UserDto userDto = null;

            try {
                userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));

                userDto = userService.getUserById(userId);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("로그인을 해주세요.");
            }

            GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

            CartDto cartDto = CartDto.builder()
                    .userDto(userDto)
                    .gifticonDto(gifticonDto)
                    .build();

            Long cartId = cartService.addToCart(cartDto);

            return ResponseEntity.ok().body(cartId);
        } catch (Exception exception) {
            exception.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> removeFromCart(@PathVariable("id") Long gifticonId,
                                                 @RequestHeader HttpHeaders headers
                                                 ) {
        try {
            // TODO 유저의 id와 카트에 있는 기프티콘의 주인의 아이디 비교
            GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));

            if (!gifticonDto.getUser().getId().equals(userId)) {
                ResponseEntity.badRequest().build();
            }

            cartService.removeFromCart(gifticonId);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> removeAllFromCart(@RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("token").get(0));

            cartService.removeAllFromCart(userId);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
