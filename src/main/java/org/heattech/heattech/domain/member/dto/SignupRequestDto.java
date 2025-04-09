package org.heattech.heattech.domain.member.dto;

import lombok.Getter;
import org.heattech.heattech.domain.member.domain.Role;

@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private Role role;

    public SignupRequestDto() {} //@RequestBody에서 꼭 필요함.

    public SignupRequestDto(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    //setter는 받은 정보를 바꿀 필요가 없으니까 필요 없음.
}
