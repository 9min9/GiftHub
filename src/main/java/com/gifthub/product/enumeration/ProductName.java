package com.gifthub.product.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum ProductName {

    ALL("전체", "all"),
    CAFFE("카페", "cafe"),
    BAKERY_DONUT_DDEOK("베이커리/도넛/떡", "bakery/donut/ddeock"),
    DEPARTMEN_MARK("백화점/마트", "department/mart"),
    ICECREAM_ICE("아이스크림/빙수", "icecream/ice"),
    CONVENIENT("편의점", "convenient"),
    BURGER_PIZZA("버거/피자", "burger/pizza"),
    CHICKEN("치킨", "chicken"),
    KOR_CH_BUN("한식/중식/분식", "kor/ch/bun"),
    ROAST_PORKFEET("구이/족발", "roast/porkfeet"),
    RESTAURANT_BUFFET("레스토랑/뷔페", "restaurant/buffet"),
    FOREIGN_FUSION_ETC("외국/퓨전/기타", "foreign/fusion/etc"),
    MOVIE_MUSIC_BOOKS("영화/음악/도서", "movie/music/books"),
    KT_COMMUNICATION("kt/통신", "kt/communication"),
    BEAUTY_HAIR_BODY("뷰티/헤어/바디", "beauty/hair/body"),
    HEALTH_LIVING_FOODS("건강/리빙/식품관", "health/living/foods"),
    LIFE_ELECTRONIC_ENTERTAIN("생활/가전/엔터", "life/electronic/entertain");

    private String korName;
    private String engName;

    ProductName(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }

    public static ProductName ofKor(final String korName) {
        List<ProductName> collect = Arrays.stream(ProductName.values()).filter(productName -> {
            return productName.korName.equals(korName);
        }).collect(Collectors.toList());

        return collect.get(0);
    }

    public static ProductName ofEng(final String engName) {
        List<ProductName> collect = Arrays.stream(ProductName.values()).filter(productName -> {
            return productName.engName.equals(engName);
        }).collect(Collectors.toList());

        return collect.get(0);
    }

}
