package com.gifthub.product.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum CategoryName {

    ALL("전체", "all"),
    CAFE("카페", "cafe"),
    BAKERY_DONUT("베이커리/도넛", "bakery-donut"),
    DEPARTMENT_MART("백화점/마트", "department-mart"),
    ICECREAM_ICE("아이스크림", "icecream-ice"),
    CONVENIENT("편의점", "convenient"),
    BURGER_PIZZA("버거/피자", "burger-pizza"),
    CHICKEN("치킨", "chicken"),
    KOR_CH_BUN("외식/분식/배달", "kor-ch-bun"),
    ROAST_PORKFEET("구이/족발", "roast-porkfeet"),
    RESTAURANT_BUFFET("레스토랑/뷔페", "restaurant-buffet"),
    FOREIGN_FUSION_ETC("기타", "foreign-fusion-etc"),
    MOVIE_MUSIC_BOOKS("영화/음악/도서", "movie-music-books"),
    KT_COMMUNICATION("주유권", "kt-communication"),
    BEAUTY_HAIR_BODY("뷰티/헤어/바디", "beauty-hair-body"),
    HEALTH_LIVING_FOODS("건강/리빙/식품관", "health-living-foods"),
    LIFE_ELECTRONIC_ENTERTAIN("생활/가전/엔터", "life-electronic-entertain");

    private final String korName;
    private final String engName;

    CategoryName(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }

    public static CategoryName valueOfKorName(String korName) {
        return Arrays.stream(values()).filter((categoryName -> categoryName.korName.equals(korName))).findFirst().orElseThrow();
    }

    public static CategoryName ofKor(final String korName) {
        List<CategoryName> collect = Arrays.stream(CategoryName.values()).filter(productName -> {
            return productName.korName.equals(korName);
        }).collect(Collectors.toList());

        if (collect.size() == 0) {
            return null;
        }

        return collect.get(0);
    }

    public static CategoryName ofEng(final String engName) {
        List<CategoryName> collect = Arrays.stream(CategoryName.values()).filter(productName -> {
            return productName.engName.equals(engName);
        }).collect(Collectors.toList());

        if (collect.size() == 0) {
            return null;
        }

        return collect.get(0);
    }

    public static String getEngNameFromKor(String korName) {
        return Arrays.stream(values())
                .filter(categoryName -> categoryName.korName.equals(korName))
                .findFirst()
                .map(CategoryName::getEngName)
                .orElse(null);
    }

    public static String getKorNameFromEng(String engName) {
        return Arrays.stream(values())
                .filter(categoryName -> categoryName.engName.equals(engName))
                .findFirst()
                .map(CategoryName::getKorName)
                .orElse(null);
    }

}
