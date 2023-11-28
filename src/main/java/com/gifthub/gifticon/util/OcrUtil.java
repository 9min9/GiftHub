package com.gifthub.gifticon.util;


import com.gifthub.exception.InvalidDueDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class OcrUtil {

    public static String[] parseStringByNewline(String s) {
        return s.split("\n");
    }

    public static String parseStringByColon(String s){
        return s.split(":")[1];
    }

    public static Map<String, String> parseArrayToMap(String[] tokens) {
        Map<String, String> map = new HashMap<>();
        for (String token : tokens) {
            String[] splitedToken = token.split(":");
            map.put(splitedToken[0], splitedToken[1]);
        }
        return map;
    }

    public static String findByMap(Map<String, String> map, String field) {
        if (map.containsKey(field)) {
            return map.get(field);
        }
        return null;
    }

    public static boolean findMatchString(String parsedString, String pattern) {
        try {
            return Pattern.compile(pattern).matcher(parsedString).find();

        } catch (PatternSyntaxException e) {
//            e.printStackTrace();
            return false;
        }
        //완전히 똑같은지 아닌지 나눠야함!

    }


    public static LocalDate localDateFormatterHyphen(String input) {
//        System.out.println(input);
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA));

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            System.out.println("yyyyMMdd 파싱에러");
            return null;
        } catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String dateParserTilde(String input) {
        try {
            Matcher matcher = Pattern.compile("~\\s*(\\d{4}\\.\\d{2}\\.\\d{2})").matcher(input);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            System.out.println("~ 파싱 에러");
            return null;
        } catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String dateParserHangul(String input) {
        try {
            Matcher matcher = Pattern.compile("(\\d{4}년\\s?\\d{2}월\\s?\\d{2}일)").matcher(input);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            System.out.println("regex년월일 파싱 에러");
            return null;
        } catch (RuntimeException e){
            e.printStackTrace();
            System.out.println("hangul");
            return null;
        }
    }

    public static String dateReplaceFromSpotToHyphen(String inputDateString) {
        String outputDateString = null;
        if(inputDateString != null){
            outputDateString = inputDateString.replace(".", "-");

        }
        if (outputDateString == null) {
            System.out.println(". -> - 파싱에러");
            return null;
        }
        return outputDateString;
    }

    public static String dateReplaceFromHangulToHyphen(String inputDateHangul) {
        String outputDateString = null;
        if(inputDateHangul != null){
            outputDateString = inputDateHangul
                    .replaceAll("년", "-")
                    .replaceAll("월", "-")
                    .replaceAll("일", "");
        }
        if (outputDateString == null) {
            System.out.println("년월일 -> - 파싱에러");
            return null;
        }
        return outputDateString;
    }

    // 유효기간 지났는지  check
    public static void checkDueDate(LocalDate inputDueDate){
        if(inputDueDate.isAfter(LocalDate.now())){
            throw new InvalidDueDate("유효기간이 지났습니다");
            // 사용자 예외처리
        }

    }


}
