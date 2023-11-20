package com.gifthub.cart.controller;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.cart.service.CartService;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.user.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Object> list() {
        Long userId = 1L; //TODO jwt에서 가져옴

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(cartService.findByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Object> addToCart(Long gifticonId) {
        try {
            // TODO jwt에서 유저 정보 가져옴 DTO
            UserDto userDto = UserDto.builder()
                    .id(1L)
                    .build();

            // TODO 기프티콘 검색
            GifticonDto gifticonDto = GifticonDto.builder()
                    .id(gifticonId)
                    .user(UserDto.builder().id(1L).build())
                    .build();

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeFromCart(@PathVariable("id") Long id) {
        try {
            // TODO 유저의 id와 카트에 있는 기프티콘의 주인의 아이디 비교

            cartService.removeFromCart(id);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> removeAllFromCart() {
        try {
            // TODO 유저의 id와 카트에 있는 기프티콘의 주인의 아이디 비교
            Long userId = 1L;

            cartService.removeAllFromCart(userId);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
