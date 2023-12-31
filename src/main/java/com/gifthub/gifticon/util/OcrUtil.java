package com.gifthub.gifticon.util;

import com.gifthub.gifticon.exception.NotExpiredDueException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class OcrUtil {

    public static String[] parseStringByNewline(String s) {
        return s.split("\n");
    }

    public static String parseStringByColon(String s){
        String[] parts = s.split(":");
        if(parts.length >= 2) {
            return parts[1].strip();
        } else {
            return "";
        }
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
            log.error("OcrUtil | findMatchString | " +e);
            return false;
        }
        //완전히 똑같은지 아닌지 나눠야함!
    }


    public static LocalDate localDateFormatterHyphen(String input) {
        try {
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA));

        } catch (DateTimeParseException e) {
            log.error("OcrUtil | localDateFormatterHyphen | yyyy-MM-dd 파싱 에러 | " +e);
            return null;
        } catch (RuntimeException e){
            log.error("OcrUtil | localDateFormatterHyphen | yyyy-MM-dd 파싱 에러 | " +e);
            return null;
        }
    }

    public static String dateParserTilde(String input) {
        try {
            Matcher matcher = Pattern.compile("~\\s*(\\d{4}\\.\\d{2}\\.\\d{2})").matcher(input);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e) {
            log.error("OcrUtil | dateParserTilde | ~ 파싱 에러 | " +e);
            return null;
        } catch (RuntimeException e){
            log.error("OcrUtil | dateParserTilde | ~ 파싱 에러 | " +e);
            return null;
        }
    }

    public static String dateParserHangul(String input) {
        try {
            Matcher matcher = Pattern.compile("(\\d{4}년\\s*\\d{2}월\\s*\\d{2}일)").matcher(input);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e) {
            log.error("OcrUtil | dateParserHangul | regex년월일 파싱 에러 | " +e);
            return null;
        } catch (RuntimeException e){
            log.error("OcrUtil | dateParserHangul | regex년월일 파싱 에러 | " +e);
            return null;
        }
    }

    public static String dateReplaceFromSpotToHyphen(String inputDateString) {
        String outputDateString = null;
        if(inputDateString != null){
            outputDateString = inputDateString.replace(".", "-");

        }
        if (outputDateString == null) {
            log.error("OcrUtil | dateReplaceFromSpotToHyphen | . -> - 파싱에러 | ");
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
            log.error("OcrUtil | dateReplaceFromHangulToHyphen | 년월일 -> - 파싱에러 | ");
            return null;
        }
        return outputDateString;
    }

    // 유효기간 지났는지  check
    public static void checkDueDate(LocalDate inputDueDate){
        if(inputDueDate.isBefore(LocalDate.now())){
            throw new NotExpiredDueException();
        }
    }

    // 년월일 사이의 공백을 제거
    public static String removeSpaces(String input) {
        if(input != null){
            return input.replaceAll("\\s", "");
        }
        return null;
    }

}
