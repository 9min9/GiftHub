package com.gifthub.user.service;

import com.gifthub.user.dto.NaverTokenDto;
import com.gifthub.user.dto.NaverUserDto;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.apache.el.util.MessageFactory.get;

import java.util.Map;


@Service
public class NaverAccountService {

    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.redirect.url}")
    private String NAVER_REDIRECT_URL;
    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;
    private final static String naverauthurl = "https://nid.naver.com";
    private final static String naverapiurl = "https://openapi.naver.com";

    public NaverTokenDto getNaverAccessToken(String authorize_code) {
        String reqURL = "https://nid.naver.com/oauth2.0/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));     //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
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

            System.out.println("response body : " + result);
            Gson gson = new Gson();
//            String json = gson.toJson(result);
            NaverTokenDto tokenDto = gson.fromJson(result, NaverTokenDto.class);


            br.close();
            bw.close();

            return tokenDto;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public NaverUserDto getNaverInfo(String code) throws Exception {
//        if (code == null) throw new Exception("Failed get authorization code");
//        String accessToken = "";
//        String refreshToken = "";
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-type", "application/x-www-form-urlencoded");
//
//            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("grant_type"   , "authorization_code");
//            params.add("client_id"    , NAVER_CLIENT_ID);
//            params.add("client_secret", NAVER_CLIENT_SECRET);
//            params.add("code"         , code);
//            params.add("redirect_uri" , NAVER_REDIRECT_URL);
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    naverauthurl+"/oauth2.0/token",
//                    HttpMethod.POST,
//                    httpEntity,
//                    String.class
//            );
//
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
//
//            accessToken  = (String) jsonObj.get("access_token");
//            refreshToken = (String) jsonObj.get("refresh_token");
//        } catch (Exception e) {
//            throw new Exception("API call failed");
//        }
//
//        return getUserInfoWithToken(accessToken);
//    }
//    public NaverUserDto getUserInfoWithToken(String accessToken) throws Exception {
//        //HttpHeader 생성
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        //HttpHeader 담기
//        RestTemplate rt = new RestTemplate();
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
//        ResponseEntity<String> response = rt.exchange(
//                naverapiurl + "/v1/nid/me",
//                HttpMethod.POST,
//                httpEntity,
//                String.class
//        );
//
//        //Response 데이터 파싱
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
//        JSONObject account = (JSONObject) jsonObj.get("response");
//
//        String naver_id = (String) account.get("id");
//        String email = String.valueOf(account.get("email"));
//        String name = String.valueOf(account.get("name"));
//        String gender = String.valueOf(account.get("gender"));
//
//        System.out.println("email:"+email);
//        System.out.println("gender:"+gender);
//        System.out.println("aaabbb");
//        return NaverUserDto.builder()
//                .naver_id(naver_id)
//                .email(email)
//                .name(name)

//                .gender(gender)
//                .build();
//
//    }


    public NaverUserDto getNaverUserInfo(String naverAccessToken) {
        String token = naverAccessToken;
        String header = "Bearer " + token;
        String reqURL = "https://openapi.naver.com/v1/nid/me";

        Map<String , String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization",header);
        System.out.println("@@@");
        String responseBody = getNaverAPi(reqURL,requestHeaders);
        System.out.println(responseBody);

      return  null;
    }

    private String getNaverAPi(String reqURL, java.util.Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(reqURL);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }

    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
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

//    private String decodeValue(String encodeValue){
//        byte[] decodedBytes = Base64.getDecoder().decode(encodeValue);
//        return new String(decodedBytes);
//    }
//
//
//        return null;
//    }
    }
}
