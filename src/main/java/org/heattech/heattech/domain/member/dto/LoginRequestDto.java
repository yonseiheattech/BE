package org.heattech.heattech.domain.member.dto;

import lombok.Getter;
import org.heattech.heattech.domain.member.domain.Role;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;
    private Role role;

    public LoginRequestDto() {}

    public LoginRequestDto(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
