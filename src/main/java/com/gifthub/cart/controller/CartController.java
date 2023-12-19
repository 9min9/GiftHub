package com.gifthub.cart.controller;

import com.gifthub.cart.dto.CartDto;
import com.gifthub.cart.service.CartService;
import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.point.exception.NotFoundGifticonException;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.exception.IdMismatchException;
import com.gifthub.user.exception.NotFoundUserException;
import com.gifthub.user.exception.NotLoginedException;
import com.gifthub.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final GifticonService gifticonService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final ExceptionResponse exceptionResponse;

    @GetMapping
    public ResponseEntity<Object> list(@RequestHeader HttpHeaders headers) {
        Long userId = null;

        try {
            if (!isNull(headers.get("Authorization"))) {
                userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            }

            if (isNull(userId)) {
                throw new NotLoginedException();
            }

        } catch (NotLoginedException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

        return ResponseEntity.ok(cartService.findByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Object> addToCart(Long gifticonId, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = null;
            UserDto userDto = null;

            userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (isNull(userId)) {
                throw new NotLoginedException();
            }

            userDto = userService.getUserById(userId);

            if (isNull(userDto)) {
                throw new NotFoundUserException();
            }

            GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

            if (isNull(gifticonDto)) {
                throw new NotFoundGifticonException();
            }

            CartDto cartDto = CartDto.builder()
                    .userDto(userDto)
                    .gifticonDto(gifticonDto)
                    .build();

            Long cartId = cartService.addToCart(cartDto);

            return ResponseEntity.ok().body(cartId);
        } catch (NotLoginedException | NotFoundUserException | NotFoundGifticonException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> removeFromCart(@PathVariable("id") Long cartId,
                                                 @RequestHeader HttpHeaders headers
                                                 ) {
        try {
            CartDto searched = cartService.getById(cartId);

            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (!searched.getUserDto().getId().equals(userId)) {
                throw new IdMismatchException();
            }

            cartService.removeFromCart(cartId);
        } catch (IdMismatchException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> removeAllFromCart(@RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            if (isNull(userId)) {
                throw new NotLoginedException();
            }

            cartService.removeAllFromCart(userId);
        } catch (NotLoginedException e) {
            return ResponseEntity.badRequest().body(exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage()));
        }

        return ResponseEntity.ok().build();
    }

}
