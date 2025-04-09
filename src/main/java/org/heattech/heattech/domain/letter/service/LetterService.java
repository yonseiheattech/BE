package org.heattech.heattech.domain.letter.service;

import org.heattech.heattech.domain.letter.domain.Letter;
import org.heattech.heattech.domain.letter.domain.Status;
import org.heattech.heattech.domain.letter.dto.LetterCancelDto;
import org.heattech.heattech.domain.letter.dto.LetterRegisterDto;
import org.heattech.heattech.domain.letter.dto.LetterReplyDto;
import org.heattech.heattech.domain.letter.repository.LetterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterService(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;

    }

    public String generateUniqueCode() {
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);

        Letter letter = Letter.builder()
                .code(String.valueOf(number))
                .status(Status.ISSUED)
                .issuedAt(LocalDateTime.now())
                .build();

        letterRepository.save(letter);

        return String.valueOf(number);
        //추후 DB에서 중복 조회
    }

    public Long registerLetter(LetterRegisterDto dto, Long senderIdFromAuth) {
        //유효한 코드인지 조회도 해야함.
        Letter letter = Letter.builder()
                .code(dto.getCode())
                .senderId(senderIdFromAuth)
                .status(Status.REGISTERED)
                .registeredAt(LocalDateTime.now())
                .build();

        return letterRepository.save(letter).getId();
    }

    public Long replyLetter(LetterReplyDto dto, Long volunteerIdFromAuth) {
        Letter letter = letterRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 없는데요?"));

        letter.setVolunteerId(volunteerIdFromAuth);
        letter.setStatus(Status.REPLIED);
        letter.setRepliedAt(LocalDateTime.now());

        letterRepository.save(letter);
        return letter.getId();
    }

    public Long cancelLetter(LetterCancelDto dto, Long senderId) {
        Letter letter = letterRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 없네"));

        if (letter.getSenderId().equals(senderId)) {
            throw new IllegalStateException("본인의 편지만 취소할 수 있습니다.");
        }

        letter.setStatus(Status.CANCELED);
        return letter.getId();
    }
}
