package org.heattech.heattech.domain.member.dto;

public class SignupRequestDto {
    private String username;
    private String password;

    public SignupRequestDto() {} //@RequestBody에서 꼭 필요함.

    public SignupRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //setter는 받은 정보를 바꿀 필요가 없으니까 필요 없음.
}
