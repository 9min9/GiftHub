package com.gifthub.config.jwt;

public class JwtContext {

    private static final ThreadLocal<String> jwtToken = new ThreadLocal<>();

    public static void setJwtToken(String token) {
        jwtToken.set(token);
    }

    public static String getJwtToken() {
        return jwtToken.get();
    }

    public static void clear() {
        jwtToken.remove();
    }


}
