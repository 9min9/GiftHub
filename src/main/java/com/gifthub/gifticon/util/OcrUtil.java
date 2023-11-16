package com.gifthub.gifticon.util;


import java.util.HashMap;
import java.util.Map;

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
public static String find(Map<String, String> map, String field) {
    if (map.containsKey(field)) {
        return map.get(field);
    }
    return null;
}
}
