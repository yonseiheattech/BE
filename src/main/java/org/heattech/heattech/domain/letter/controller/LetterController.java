package org.heattech.heattech.domain.letter.controller;

import org.heattech.heattech.domain.letter.dto.LetterCancelDto;
import org.heattech.heattech.domain.letter.dto.LetterRegisterDto;
import org.heattech.heattech.domain.letter.dto.LetterReplyDto;
import org.heattech.heattech.domain.letter.service.LetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/letters")
public class LetterController {

    private final LetterService letterService;

    public LetterController(LetterService letterService) {
        this.letterService = letterService;
    }

    @PostMapping("/generate-code")
    public ResponseEntity<String> generateLetterCode() {
        String code = letterService.generateUniqueCode();
        return ResponseEntity.ok(code);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerLetter(@RequestBody LetterRegisterDto dto, @RequestParam("senderId") Long senderId) {
        Long id = letterService.registerLetter(dto, senderId);
        return ResponseEntity.ok("편지 등록 완료 id: " + id);
    }

    @PostMapping("/reply")
    public ResponseEntity<String> replyLetter(@RequestBody LetterReplyDto dto, @RequestParam("volunteerId") Long volunteerId) {
        Long id = letterService.replyLetter(dto, volunteerId);
        return ResponseEntity.ok("답장 상태 변경 완료" + id);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelLetter(@RequestBody LetterCancelDto dto, @RequestParam("senderId") Long senderId) {
        Long id = letterService.cancelLetter(dto, senderId);
        return ResponseEntity.ok("삭제 완료" + id);
    }

}
