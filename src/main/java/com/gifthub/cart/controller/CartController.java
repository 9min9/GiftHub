package com.gifthub.cart.controller;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.cart.dto.CartRequestDto;
import com.gifthub.cart.service.CartService;
import com.gifthub.gifticon.entity.Gifticon;
import com.gifthub.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService service;

    @GetMapping
    public ResponseEntity<?> list() {
        Long userId = 1L; //TODO jwt에서 가져옴
        
        // TODO MemberService에서 검색
        Optional<User> searched = Optional.empty();

//        User user = searched.get(); // TODO 추후 주석 해제
        User user = User.builder()
                .id(userId)
                .build();


        if (/*searched.isEmpty()*/ user == null) { // TODO 추후 주석 제거 예정
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(service.findByUser(user));
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@ModelAttribute CartRequestDto dto, BindingResult bindingResult) {
        if (dto.getGifticonId() == null) {
            bindingResult.reject("400", "값이 들어오지 않았습니다. 값을 입력해주세요");
        }

        try {
            // TODO jwt에서 유저 정보 가져옴
            User user = User.builder()
                    .id(1L)
                    .build();

            // TODO 기프티콘 검색
            Gifticon gifticon = Gifticon.builder()
                    .id(dto.getGifticonId())
                    .build();

            CartDto cartDto = CartDto.builder()
                    .user(user)
                    .gifticon(gifticon)
                    .build();

            Long cartId = service.addToCart(cartDto);

            return ResponseEntity.ok().body(cartId);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable("id") Long id) {
        try {
            // TODO 유저의 id와 카트에 있는 기프티콘의 주인의 아이디 비교
            
            service.removeFromCart(id);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
