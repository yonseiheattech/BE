package org.heattech.heattech.domain.letter.service;


import lombok.RequiredArgsConstructor;
import org.heattech.heattech.domain.letter.domain.Letter;
import org.heattech.heattech.domain.letter.domain.ThanksNote;
import org.heattech.heattech.domain.letter.dto.thanksnote.ThanksNoteRequestDto;
import org.heattech.heattech.domain.letter.repository.LetterRepository;
import org.heattech.heattech.domain.letter.repository.ThanksNoteRepository;
import org.heattech.heattech.domain.member.domain.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThanksNoteService {

    private final LetterRepository letterRepository;
    private final ThanksNoteRepository thanksNoteRepository;

    @PreAuthorize("hasAuthority('ROLE_SENDER')")
    @Transactional
    public Long createThanksNote(ThanksNoteRequestDto dto, Long senderId ) {

        Letter letter = letterRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("편지가 없는데용"));

        if (letter.getThanksNote() != null) {
            throw new IllegalStateException("이미 감사 편지가 등록되었습니다.");
        }

        if (!(letter.getSenderId()).equals(senderId)) {
            throw new IllegalStateException("본인의 편지에만 감사 편지를 쓸 수 있습니다.");
        }



        ThanksNote thanksNote = ThanksNote.builder()
                .content(dto.getContent())
                .build();

        letter.setThanksNote(thanksNote);

        letterRepository.save(letter);

        //ThanksNOte는 저장 안 해도 됨 letter 저장하면서 자동으로 됨

        return thanksNote.getId();
    }

    //자신이 답장한 모든 편지 감사노트 모아보깅

}
