package com.gifthub.user.service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.*;
import org.springframework.stereotype.Service;

@Service
public class KakaoAccountService {

    public String getAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=f4f9e94f11938c27a2bba3f932a0d2b1");
            sb.append("&redirect_uri=http://localhost:8081/api/kakao/login");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);


            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
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

            // access token : 유효기간 하루 ( 접속용 )
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            // refresh token : 유효기간 2주~ 한달 ( 저장용 )
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public HashMap<String, Object> getUserInfo (String access_Token) {
        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //    요청에 필요한 Header에 포함될 내용
            // Bearer => JWT 혹은 OAuth에 대한 토큰을 사용한다. (RFC 6750)
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            int responseCode = conn.getResponseCode();
            System.out.println("access_token :" + access_Token );
            System.out.println("responseCode : " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String id = element.getAsJsonObject().get("id").getAsString();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();


//            Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
////            System.out.println("@@@");
//            for (Map.Entry<String, JsonElement> entry : entries) {
//                System.out.println(entry.getKey());
////                JsonObject test = element.getAsJsonObject().get(entry.getKey()).getAsJsonObject();
////                System.out.println(test);
//            }
//            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
//            for (Map.Entry<String, JsonElement> entry : properties.entrySet()) {
//                String test = properties.getAsJsonObject().get(entry.getKey()).getAsString();
//                System.out.println(test);
//            }
//            //객체Object는 키-값 쌍의 집합이다.
//            //todo : Map 객체를 만들어서 아래의 jsonElement를 저장하고 한번 더 iter
//            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
//
//            for (Map.Entry<String, JsonElement> entry : kakao_account.entrySet()) {
//                JsonElement test2 = kakao_account.getAsJsonObject().get(entry.getKey());
//                System.out.println(entry.getKey()+ " | " +test2);
//            }

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);
            System.out.println(nickname);
            System.out.println(email);
            System.out.println(id);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }

    public void kakaoLogout(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            System.out.println("access_Token:"+access_Token);
            int responseCode = conn.getResponseCode();
            System.out.println("로그아웃 성공");

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


    public void kakaoLogoutV2(String access_Token) throws IOException {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";

        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
        System.out.println("access_Token:"+access_Token);
        int responseCode = conn.getResponseCode();
        System.out.println("로그아웃 성공");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String result = "";
        String line = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println(result);
    }


}