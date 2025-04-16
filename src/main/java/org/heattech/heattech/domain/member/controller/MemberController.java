package org.heattech.heattech.domain.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.heattech.heattech.domain.member.domain.Member;
import org.heattech.heattech.domain.member.dto.LoginRequestDto;
import org.heattech.heattech.domain.member.dto.LoginResponseDto;
import org.heattech.heattech.domain.member.dto.MemberResponseDto;
import org.heattech.heattech.domain.member.dto.SignupRequestDto;
import org.heattech.heattech.domain.member.repository.MemberRepository;
import org.heattech.heattech.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;//왜 이걸 final로 하지?

    public MemberController(MemberService memberService, MemberRepository memberRepository ) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignupRequestDto dto) {
        MemberResponseDto response = memberService.signup(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        LoginResponseDto loginResponse = memberService.login(dto,response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        memberService.logout(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        String username = userDetails.getUsername();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return ResponseEntity.ok(new MemberResponseDto(member.getUsername()));
    }


}
