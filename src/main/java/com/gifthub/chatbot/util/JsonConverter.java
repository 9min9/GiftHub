package com.gifthub.chatbot.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonConverter {

    public static List<String> kakaoChatbotConverter(Map<Object, Object> barcode) {
        Gson gson = new Gson();
        String json = gson.toJson(barcode);

        JsonParser jsonParser = new JsonParser();
        JsonElement element = jsonParser.parse(json);
        JsonElement value = element.getAsJsonObject().get("value");
        JsonElement origin = value.getAsJsonObject().get("origin");

        String stringUrls = origin.getAsString();

        Pattern pattern = Pattern.compile("List\\((.*?)\\)");
        Matcher matcher = pattern.matcher(stringUrls);

        List<String> urlList = new ArrayList<>();
        while (matcher.find()) {
            String urls = matcher.group(1);
            String[] urlArray = urls.split(", ");
            System.out.println(urls);
            urlList.addAll(Arrays.asList(urlArray));
        }

        System.out.println("urlList");
        for (String s : urlList) {
            System.out.println(s);
        }

        return urlList;
    }



}
