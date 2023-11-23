package com.gifthub.gifticon.util;


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

    public static String[] parseString(String s) {
        return s.split("\n");
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
            e.printStackTrace();
            return false;
        }
        //완전히 똑같은지 아닌지 나눠야함!

    }

    public static void checkBrandInDb(String input) {
        if (input == null) {

        }
    }

    public static LocalDate localDateFormatter(String input) {
        try{
            return LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA));

        } catch (DateTimeParseException e){
            e.printStackTrace();
            System.out.println("yyyyMMdd 파싱에러");
            return null;
        }
    }

    public static String dateParserTilde(String input) {
        try{
            Matcher matcher = Pattern.compile("~\\s*(\\d{4}\\.\\d{2}\\.\\d{2})").matcher(input);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e){
            e.printStackTrace();
            System.out.println("~ 파싱 에러");
            return null;
        }
    }

    public static String dateParserHangul(String input) {
        try{
            Matcher matcher = Pattern.compile("(\\d{4}년\\s?\\d{2}월\\s?\\d{2}일)").matcher(input);
            return matcher.find() ? matcher.group(1) : null;

        } catch (PatternSyntaxException e){
            e.printStackTrace();
            System.out.println("년월일 파싱 에러");
            return null;
        }
    }

    public static String dateReplaceFromSpotToHyphen(String inputDateString){
        String outputDateString = inputDateString.replace(".","-");
        if(outputDateString == null){
            System.out.println(". -> - 파싱에러");
            return null;
        }
        return outputDateString;
    }

}
