package com.gifthub.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NaverTokenDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;

}
