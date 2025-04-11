package org.heattech.heattech.domain.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoUser {
    private Long id;
    private String nickname;

    public KakaoUser() {}
    public KakaoUser(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
