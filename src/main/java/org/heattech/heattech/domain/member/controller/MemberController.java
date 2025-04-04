package org.heattech.heattech.domain.member.controller;

import org.heattech.heattech.domain.member.dto.LoginRequestDto;
import org.heattech.heattech.domain.member.dto.MemberResponseDto;
import org.heattech.heattech.domain.member.dto.SignupRequestDto;
import org.heattech.heattech.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService; //왜 이걸 final로 하지?

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignupRequestDto dto) {
        MemberResponseDto response = memberService.signup(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody LoginRequestDto dto) {
        MemberResponseDto response = memberService.login(dto);
        return ResponseEntity.ok(response);
    }
}
