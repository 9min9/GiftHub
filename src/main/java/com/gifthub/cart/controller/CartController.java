package com.gifthub.cart.controller;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.cart.dto.CartRequestDto;
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
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Object> list() {
        Long userId = 1L; //TODO jwt에서 가져옴

        if (/*searched.isEmpty()*/ userId == null) { // TODO 추후 주석 제거 예정
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(cartService.findByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Object> addToCart(@ModelAttribute CartRequestDto dto, BindingResult bindingResult) {
        if (dto.getGifticonId() == null) {
            bindingResult.reject("400", "값이 들어오지 않았습니다. 값을 입력해주세요");
        }

        try {
            // TODO jwt에서 유저 정보 가져옴 DTO
            UserDto userDto = UserDto.builder()
                    .id(1L)
                    .build();

            // TODO 기프티콘 검색
            GifticonDto gifticonDto = GifticonDto.builder()
                    .id(dto.getGifticonId())
                    .build();

            CartDto cartDto = CartDto.builder()
                    .userDto(userDto)
                    .gifticonDto(gifticonDto)
                    .build();

            Long cartId = cartService.addToCart(cartDto);

            return ResponseEntity.ok().body(cartId);
        } catch (Exception exception) {
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

}
