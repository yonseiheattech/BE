package org.heattech.heattech.domain.letter.controller;

import lombok.Getter;
import org.heattech.heattech.domain.letter.domain.Letter;
import org.heattech.heattech.domain.letter.dto.letter.LetterCancelDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterDeliverDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterRegisterDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterReplyDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterResponseDto;
import org.heattech.heattech.domain.letter.service.LetterService;
import org.heattech.heattech.domain.member.domain.Role;
import org.heattech.heattech.domain.member.service.MemberService;
import org.heattech.heattech.jwt.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;
    private final MemberService memberService;

    public LetterController(LetterService letterService, MemberService memberService) {
        this.letterService = letterService;
        this.memberService = memberService;
    }

    @GetMapping("/generate-code")
    public ResponseEntity<String> generateLetterCode(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        String code = letterService.generateUniqueCode(senderId);
        return ResponseEntity.ok(code);
    }

    @PreAuthorize("hasRole('SENDER')")
    @PostMapping("/register")
    public ResponseEntity<String> registerLetter(@RequestBody LetterRegisterDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println("컨트롤러 진입!");


        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.registerLetter(dto, senderId);


        return ResponseEntity.ok("편지 등록 완료 id: " +id );


    }

    @PostMapping("/reply")
    public ResponseEntity<String> replyLetter(@RequestBody LetterReplyDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        Long volunteerId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.replyLetter(dto, volunteerId);
        return ResponseEntity.ok("답장 상태 변경 완료" + id);
    }

    @PostMapping("/deliver")
    public ResponseEntity<String> deliverLetter(@RequestBody LetterDeliverDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.deliverLetter(dto, senderId);
        return ResponseEntity.ok("답장 수령으로 변경" + id);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelLetter(@RequestBody LetterCancelDto dto, @AuthenticationPrincipal UserDetails userDetails ) {
        Long senderId = memberService.findIdByUsername(userDetails.getUsername());
        Long id = letterService.cancelLetter(dto, senderId);
        return ResponseEntity.ok("삭제 완료" + id);
    }



    @GetMapping("/my")
    public ResponseEntity<List<LetterResponseDto>> getMyAllLetters(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();
        Role role = userDetails.getRole();

        return ResponseEntity.ok(letterService.getMyLetters(userId));
    }

    @GetMapping("/my/{code}")
    public ResponseEntity<Letter> getMyLetterByCode(@PathVariable String code, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();
        Role role = userDetails.getRole();

        return ResponseEntity.ok(letterService.getMyLetterByCode(code, userId, role));
    }

    @GetMapping("/vol/my/{code}")
    public ResponseEntity<Letter> volGetMyLetterByCode(@PathVariable String code, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getId();
        Role role = userDetails.getRole();

        return ResponseEntity.ok(letterService.volGetMyLetterByCode(code, userId, role));

    }

    @GetMapping("/vol/my")
    public ResponseEntity<List<LetterResponseDto>> volGetMyAllLetters(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Role role = userDetails.getRole();

        return ResponseEntity.ok(letterService.volGetMyAllLetters(userId, role));
    }
}
