package org.heattech.heattech.domain.letter.controller;

import org.heattech.heattech.domain.letter.dto.LetterCancelDto;
import org.heattech.heattech.domain.letter.dto.LetterRegisterDto;
import org.heattech.heattech.domain.letter.dto.LetterReplyDto;
import org.heattech.heattech.domain.letter.service.LetterService;
import org.heattech.heattech.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;
    private final MemberService memberService;

    public LetterController(LetterService letterService, MemberService memberService) {
        this.letterService = letterService;
        this.memberService = memberService;
    }

    @PostMapping("/generate-code")
    public ResponseEntity<String> generateLetterCode(@AuthenticationPrincipal UserDetails userDetails) {
        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        String code = letterService.generateUniqueCode(senderId);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerLetter(@RequestBody LetterRegisterDto dto, @AuthenticationPrincipal UserDetails userDetails) {

        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.registerLetter(dto, senderId);
        return ResponseEntity.ok("편지 등록 완료 id: " + id);
    }

    @PostMapping("/reply")
    public ResponseEntity<String> replyLetter(@RequestBody LetterReplyDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        Long volunteerId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.replyLetter(dto, volunteerId);
        return ResponseEntity.ok("답장 상태 변경 완료" + id);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelLetter(@RequestBody LetterCancelDto dto, @AuthenticationPrincipal UserDetails userDetails ) {
        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.cancelLetter(dto, senderId);
        return ResponseEntity.ok("삭제 완료" + id);
    }

}
