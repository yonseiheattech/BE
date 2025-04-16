package org.heattech.heattech.domain.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.heattech.heattech.domain.kakao.dto.KakaoUser;
import org.heattech.heattech.domain.member.domain.Member;
import org.heattech.heattech.domain.member.domain.Role;
import org.heattech.heattech.jwt.JwtTokenDto;
import org.heattech.heattech.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.heattech.heattech.domain.member.repository.MemberRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final String tokenUri = "https://kauth.kakao.com/oauth/token";
    private final String userInfoUri = "https://kapi.kakao.com/v2/user/me";

    public JwtTokenDto kakaoLogin(String code) {
        String kakaoAccessToken = getAccessToken(code);
        KakaoUser kakaoUser = getUserInfo(kakaoAccessToken);

        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoUser.getId());

        Member member;
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
        } else {
            member = new Member(
                    kakaoUser.getId(),
                    kakaoUser.getNickname(),
                    Role.VOLUNTEER
            );
            memberRepository.save(member);
        }

        String accessToken = jwtUtil.generateAccessToken(member.getId(), member.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId(), member.getUsername());

        return new JwtTokenDto(accessToken, refreshToken);
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(java.util.Collections.singletonList(StandardCharsets.UTF_8));

        MultiValueMap<String, String> params = new org.springframework.util.LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        try {
            JsonNode node = objectMapper.readTree(response.getBody());
            return node.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 토큰 파싱 실패", e);
        }
    }


    private KakaoUser getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, String.class);

        try {
            JsonNode node = objectMapper.readTree(response.getBody());
            Long id = node.get("id").asLong();
            String nickname = node.get("properties").get("nickname").asText();
            return new KakaoUser(id, nickname);
        } catch (Exception e) {
            throw new RuntimeException("카카오 유저 정보 파싱 실패", e);
        }
    }
}