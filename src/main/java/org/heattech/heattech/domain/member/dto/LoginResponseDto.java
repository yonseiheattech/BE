package org.heattech.heattech.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String username;
    private String accessToken;

    public LoginResponseDto(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }
}
