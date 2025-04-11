package org.heattech.heattech.domain.kakao.controller;


import org.heattech.heattech.domain.kakao.service.KakaoLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");

        String jwt = kakaoLoginService.kakaoLogin(code);

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
