package com.gifthub.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PUBLIC;

@Builder
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PUBLIC)
@Getter
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String userRole;
}
