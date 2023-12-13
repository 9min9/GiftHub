package com.gifthub.point.controller;

import com.gifthub.gifticon.dto.GifticonDto;
import com.gifthub.gifticon.service.GifticonService;
import com.gifthub.global.exception.ExceptionResponse;
import com.gifthub.movement.service.MovementService;
import com.gifthub.point.exception.NotEnoughPointException;
import com.gifthub.point.exception.NotFoundGifticonException;
import com.gifthub.point.service.PointService;
import com.gifthub.product.dto.ProductDto;
import com.gifthub.product.service.ProductService;
import com.gifthub.user.UserJwtTokenProvider;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.exception.NotLoginedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Objects.isNull;

@RequestMapping("/api/points")
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;
    private final GifticonService gifticonService;
    private final MovementService movementService;
    private final ProductService productService;
    private final UserJwtTokenProvider userJwtTokenProvider;
    private final ExceptionResponse exceptionResponse;

    @PostMapping
    public ResponseEntity<Object> addPoint(@RequestParam("point") Long point,
                                           @RequestHeader HttpHeaders headers
                                           ) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));

            UserDto userDto = pointService.plusPoint(point, userId);

            return ResponseEntity.ok(userDto);
        } catch (NotLoginedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/buy")
    @Transactional
    public ResponseEntity<Object> usePoint(@RequestParam("point") Long point,
                                           @RequestParam("gifticonIds") Long[] gifticonIds,
                                           @RequestHeader HttpHeaders headers) {
        try {
            Long userId = userJwtTokenProvider.getUserIdFromToken(headers.get("Authorization").get(0));
            if (isNull(userId)) {
                throw new NotLoginedException();
            }

            UserDto toUser = pointService.usePoint(point, userId);

            if (isNull(toUser)) {
                throw new NotEnoughPointException();
            }

            for (Long gifticonId : gifticonIds) {
                GifticonDto gifticonDto = gifticonService.findGifticon(gifticonId);

                if (isNull(gifticonDto)) {
                    throw new NotFoundGifticonException();
                }

                UserDto fromUser = gifticonDto.getUser();
                pointService.plusPoint(point, fromUser.getId());

                ProductDto productByGifticonId = productService.getProductByGifticonId(gifticonId);
                gifticonDto.setProductDto(productByGifticonId);

                movementService.move(fromUser, toUser, gifticonDto);

                gifticonDto.setUser(toUser);

                gifticonService.saveGifticonWithKorCategoryName(gifticonDto);
            }

            return ResponseEntity.ok(toUser);
        } catch (NotEnoughPointException | NotLoginedException e) {
            Map<String, String> exception = exceptionResponse.getException(e.getField(), e.getCode(), e.getMessage());

            return ResponseEntity.badRequest().body(exception);
        }
    }

}
