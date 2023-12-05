package com.gifthub.gifticon.util;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OcrUtilTest {

    /* TODO : 테스트 목적
    “2024년 11월 14일” 년월일 공백 못잡는 에러 잡기위해서
     */


    @Test
    public void dateParserHangul() {
        String testComplete = "2024년11월14일"; // 이건 잘 들어감
        String testInput = "2024년 11월 14일"; // 이건 잘 안들어감

        String resultTestComplete = OcrUtil.dateParserHangul(testComplete);
        String resultTest = OcrUtil.dateParserHangul(testInput);

        Assertions.assertThat(resultTestComplete).isEqualTo(resultTest);
    }
}
