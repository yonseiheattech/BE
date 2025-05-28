package org.heattech.heattech.domain.letter.controller;


import lombok.RequiredArgsConstructor;
import org.heattech.heattech.domain.letter.dto.thanksnote.ThanksNoteRequestDto;
import org.heattech.heattech.domain.letter.dto.thanksnote.ThanksNoteResponseDto;
import org.heattech.heattech.domain.letter.service.ThanksNoteService;
import org.heattech.heattech.domain.member.domain.Role;
import org.heattech.heattech.jwt.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/thanks")
public class ThanksNoteController {
    private final ThanksNoteService thanksNoteService;

    @PostMapping("/{code}")
    public ResponseEntity<?> registerThanksNote( @RequestBody ThanksNoteRequestDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Role role = userDetails.getRole();
        Long id = thanksNoteService.createThanksNote(dto, userId);
        return ResponseEntity.ok("감사편지 등록완료" + id);

    }

    @GetMapping
    public ResponseEntity<List<ThanksNoteResponseDto>> getAllThanksNotes() {
        List<ThanksNoteResponseDto> thanksNotes = thanksNoteService.getAllThanksNotes();
        return ResponseEntity.ok(thanksNotes);
    }


}
