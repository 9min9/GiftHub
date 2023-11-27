package com.gifthub.user.service;

import com.gifthub.user.dto.NaverTokenDto;
import com.gifthub.user.dto.NaverUserDto;
import com.gifthub.user.entity.NaverUser;
import com.gifthub.user.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;

@Service
@RequiredArgsConstructor
public class NaverAccountService {
    private final UserRepository userRepository;

    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.redirect.url}")
    private String NAVER_REDIRECT_URL;
    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;
//    private final static String naverauthurl = "https://nid.naver.com";
//    private final static String naverapiurl = "https://openapi.naver.com";

    public NaverUserDto getNaverUserByNaverId(String naverId) {
        NaverUser naverUser = userRepository.findByNaverId(naverId).orElse(null);
        if (naverUser != null) {
            return naverUser.toNaverUserDto();
        }

        return null;
    }

    public NaverTokenDto getNaverAccessToken(String authorize_code) {
        String reqURL = "https://nid.naver.com/oauth2.0/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + NAVER_CLIENT_ID);
            sb.append("&client_secret=" + NAVER_CLIENT_SECRET);
            sb.append("&redirect_uri=" + NAVER_REDIRECT_URL);
            sb.append("&code=" + authorize_code);
            sb.append("&state=test");
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            System.out.println("code : " + authorize_code);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
//            System.out.println("response body : " + result);
            Gson gson = new Gson();
            NaverTokenDto tokenDto = gson.fromJson(result, NaverTokenDto.class);

            br.close();
            bw.close();

            return tokenDto;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public NaverUserDto getNaverUserInfo(String naverAccessToken) {
        String token = naverAccessToken;
        System.out.println(naverAccessToken+"test");
        String header = "Bearer " + token;
        System.out.println("NaverUserInfoService");
        System.out.println(token);

        String reqURL = "https://openapi.naver.com/v1/nid/me";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization",header);
        JsonParser parser = new JsonParser();

        String responseBody = getNaverAPi(reqURL, requestHeaders);
        System.out.println(responseBody);

        JsonElement naverelement = parser.parse(responseBody);
        JsonObject response = naverelement.getAsJsonObject().get("response").getAsJsonObject();

        try {
            String id = response.getAsJsonObject().get("id").getAsString();
            String email = response.getAsJsonObject().get("email").getAsString();
            String nickname = response.getAsJsonObject().get("nickname").getAsString();
            String gender = response.getAsJsonObject().get("gender").getAsString();
            String mobile = response.getAsJsonObject().get("mobile").getAsString();
            String name = response.getAsJsonObject().get("name").getAsString();
            String birthyear = response.getAsJsonObject().get("birthyear").getAsString();
            String birthday = response.getAsJsonObject().get("birthday").getAsString();
            System.out.println("id:"+id);


            String decodeStr_name = "";
            decodeStr_name = URLDecoder.decode(id, "utf-8");
//            System.out.println("decode test:" + decodeStr_name);


            NaverUserDto naverUserDto = NaverUserDto.builder()
                    .NaverId(id)
                    .name(name)
                    .nickname(nickname)
                    .email(email)
                    .gender(gender)
                    .birthyear(birthyear)
                    .birthday(birthday)
                    .tel(mobile)
                    .build();
            return naverUserDto;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return null;
    }

    private String getNaverAPi(String reqURL, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(reqURL);

        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
