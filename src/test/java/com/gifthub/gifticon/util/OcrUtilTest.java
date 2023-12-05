package com.gifthub.gifticon.util;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OcrUtilTest {



    @Test
    @DisplayName("년월일 공백 못잡는 에러 잡는 테스트")
    public void dateParserHangul() {
        String testComplete = "2024년11월14일"; // 이건 잘 들어감
        String testInput = "2024년 11월 14일"; // 이건 잘 안들어감

        String resultTestComplete = OcrUtil.dateParserHangul(testComplete);
        String resultTest = OcrUtil.dateParserHangul(testInput);

        Assertions.assertThat(resultTestComplete).isEqualTo(resultTest);
    }

    @Test
    @DisplayName("년월일 공백으로 Replace가 어떻게 테스트 되는지 확인")
    public void dateHangulReplacerTest(){
        String testComplete = "2024년11월14일";
        String testInput = "2024년 11월 14일";

        String resultTestComplete = OcrUtil.dateParserHangul(testComplete);
        resultTestComplete = OcrUtil.removeSpaces(resultTestComplete);

        String resultTest = OcrUtil.dateParserHangul(testInput);
        resultTest = OcrUtil.removeSpaces(resultTest);

        Assertions.assertThat(resultTestComplete).isEqualTo(resultTest);

    }
}
