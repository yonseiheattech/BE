package org.heattech.heattech.domain.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; //클라이언트에게 응답 보낼 때 사용되ㄴ는 객체
import org.heattech.heattech.domain.letter.domain.Letter;
import org.heattech.heattech.domain.member.domain.Member;
import org.heattech.heattech.domain.member.domain.Role;
import org.heattech.heattech.domain.member.dto.LoginRequestDto;
import org.heattech.heattech.domain.member.dto.LoginResponseDto;
import org.heattech.heattech.domain.member.dto.MemberResponseDto;
import org.heattech.heattech.domain.member.dto.SignupRequestDto;
import org.heattech.heattech.domain.member.repository.MemberRepository;
import org.heattech.heattech.jwt.JwtUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil; //이렇게 만해도 됨 new 안해도됨 => @Component 때문에

    //생성자 주입
    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }


    //사인업
    public MemberResponseDto signup(SignupRequestDto dto) {

        //Dto => Entity
        Member member = new Member(dto.getUsername(), dto.getPassword(), dto.getRole());

        Member savedMember = memberRepository.save(member);

        return new MemberResponseDto(savedMember.getUsername(),savedMember.getRole());

    }

    //로그인
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response) {

        Member member = memberRepository.findByUsername(dto.getUsername()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));//타입이 Optional이면 orElseThrow!!

        if (! member.getPassword().equals(dto.getPassword())){
            throw new IllegalArgumentException("비밀번호 일치하지 않음");
        }

        Role role = member.getRole();
        String roleString = role.name();

        String accessToken = jwtUtil.generateAccessToken(member.getId(), member.getUsername(), roleString);
        String refreshToken = jwtUtil.generateRefreshToken(member.getId(), member.getUsername(), roleString);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofHours(1))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofHours(24))
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());//이럼 헤더에 달림
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return new LoginResponseDto(member.getUsername(), accessToken, refreshToken);
    }

    public void logout(HttpServletResponse response) {
        //삭제용 쿠키
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    public Long findIdByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("유저 없음"))
                .getId();
    }
}
