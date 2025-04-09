package org.heattech.heattech.domain.member.dto;

public class MemberResponseDto {
    private String username;

    public MemberResponseDto() {}

    public MemberResponseDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
