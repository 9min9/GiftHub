package com.gifthub.user.service;

import com.gifthub.user.dto.KakaoUserDto;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoAccountService {
    @Value("${kakaoRestKey}")
    private String restKey;

    public String getKakaoAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String id_token= "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));     //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" +restKey);
            sb.append("&redirect_uri=http://localhost:8081/login");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            id_token = element.getAsJsonObject().get("id_token").getAsString();
            access_Token = element.getAsJsonObject().get("access_token").getAsString();     // access token : 유효기간 하루 ( 접속용 )
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();   // refresh token : 유효기간 2주~ 한달 ( 저장용 )

            System.out.println("id_token: " + id_token);
            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    public KakaoUserDto getKakaoUserInfo (String access_Token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //요청에 필요한 Header에 포함될 내용
            //Bearer => JWT 혹은 OAuth에 대한 토큰을 사용한다. (RFC 6750)
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("access_token :" + access_Token );
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String kakaoAccountId = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            String name = kakao_account.getAsJsonObject().get("name").getAsString();
            String year = kakao_account.getAsJsonObject().get("birthyear").getAsString();
            String birthday = kakao_account.getAsJsonObject().get("birthday").getAsString();
            String gender = kakao_account.getAsJsonObject().get("gender").getAsString();
            String phone_number = kakao_account.getAsJsonObject().get("phone_number").getAsString();

            KakaoUserDto kakaoUserDto = KakaoUserDto.builder()
                    .kakaoAccountId(kakaoAccountId)
                    .name(name)
                    .nickname(nickname)
                    .email(email)
//                    .year(LocalDate.parse(year))        //todo : LocalDate.pares() 체크하기
                    .gender(gender)
                    .phoneNumber(phone_number)
                    .build();

            return kakaoUserDto;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("getKakaoUserInfo error");
            return null;
        }
    }

    public void kakaoLogout(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            int responseCode = conn.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}