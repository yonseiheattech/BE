package org.heattech.heattech.domain.member.service;

import org.heattech.heattech.domain.member.domain.Member;
import org.heattech.heattech.domain.member.dto.LoginRequestDto;
import org.heattech.heattech.domain.member.dto.MemberResponseDto;
import org.heattech.heattech.domain.member.dto.SignupRequestDto;
import org.heattech.heattech.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    //생성자 주입
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    //사인업
    public MemberResponseDto signup(SignupRequestDto dto) {

        //Dto => Entity
        Member member = new Member(dto.getUsername(), dto.getPassword());

        Member savedMember = memberRepository.save(member);

        return new MemberResponseDto(savedMember.getUsername());

    }

    //로그인 user 조회
    public MemberResponseDto login(LoginRequestDto dto) {

        Member member = memberRepository.findByUsername(dto.getUsername()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));//타입이 Optional이면 orElseThrow!!

        if (! member.getPassword().equals(dto.getPassword())){
            throw new IllegalArgumentException("비밀번호 일치하지 않음");
        }
        return new MemberResponseDto(member.getUsername());
    }
}
