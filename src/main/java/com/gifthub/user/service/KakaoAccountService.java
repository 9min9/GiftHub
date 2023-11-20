package com.gifthub.user.service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;

import com.gifthub.user.dto.KakaoUserDto;
import com.gifthub.user.dto.UserDto;
import com.gifthub.user.entity.KakaoUser;
import com.gifthub.user.entity.User;
import com.gifthub.user.repository.KakaoUserRepository;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAccountService {

    private final KakaoUserRepository kakaoUserRepository;
    @Value("${kakaoRestKey}")
    private String restKey;

    public KakaoUserDto save(KakaoUserDto kakaoUserDto){
        KakaoUser kakaoUser =kakaoUserRepository.save(kakaoUserDto.toEntity());

        return kakaoUser.toKakaoUserDto();
    }
    public KakaoUserDto getUser(String kakaoAccountId){
        KakaoUser kakaoUser = kakaoUserRepository.findById(kakaoAccountId).orElse(null);

        if(kakaoUser==null){
            return  null;
        }
        return kakaoUser.toKakaoUserDto();

    }
    public void delUser(String kakaoAccountId){
        kakaoUserRepository.deleteById(kakaoAccountId);
    }




    public String getKakaoAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String id_token= "";
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
            sb.append("&client_id="+restKey);
            sb.append("&redirect_uri=http://localhost:8081/api/kakao/login");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("@@@@@");
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
            // id_token
            id_token = element.getAsJsonObject().get("id_token").getAsString();

            // access token : 유효기간 하루 ( 접속용 )
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            // refresh token : 유효기간 2주~ 한달 ( 저장용 )
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
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
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String kakaoAccountId = element.getAsJsonObject().get("id").getAsString();
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
            System.out.println("id:"+ kakaoAccountId);
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            String name = kakao_account.getAsJsonObject().get("name").getAsString();
            String year = kakao_account.getAsJsonObject().get("birthyear").getAsString();
            String birthday = kakao_account.getAsJsonObject().get("birthday").getAsString();
            String gender = kakao_account.getAsJsonObject().get("gender").getAsString();
            String phone_number = kakao_account.getAsJsonObject().get("phone_number").getAsString();


            KakaoUserDto test = KakaoUserDto.builder()
                    .kakaoAccountId(kakaoAccountId)
                    .name(name)
                    .nickname(nickname)
                    .email(email)
//                    .year(LocalDate.parse(year))        //todo : LocalDate.pares() 체크하기
                    .gender(gender)
                    .phoneNumber(phone_number)
                    .build();

            System.out.println("---kakaoDto---");
            System.out.println(test.getKakaoAccountId());
            System.out.println(test.getEmail());
            System.out.println(test.getName());
            System.out.println(test.getNickname());
            System.out.println(test.getGender());


            return test;


        } catch (IOException e) {
            // TODO Auto-generated catch block

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