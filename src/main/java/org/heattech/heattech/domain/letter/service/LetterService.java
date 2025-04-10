package org.heattech.heattech.domain.letter.service;

import org.heattech.heattech.domain.letter.domain.Letter;
import org.heattech.heattech.domain.letter.domain.Status;
import org.heattech.heattech.domain.letter.dto.LetterCancelDto;
import org.heattech.heattech.domain.letter.dto.LetterRegisterDto;
import org.heattech.heattech.domain.letter.dto.LetterReplyDto;
import org.heattech.heattech.domain.letter.repository.LetterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterService(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;

    }

    public String generateUniqueCode(Long senderId) {
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);

        Letter letter = Letter.builder()
                .code(String.valueOf(number))
                .senderId(senderId)
                .status(Status.ISSUED)
                .issuedAt(LocalDateTime.now())
                .build();

        letterRepository.save(letter);

        return String.valueOf(number);
        //추후 DB에서 중복 조회
    }

    @Transactional//save 안해도 됨?
    public Long registerLetter(LetterRegisterDto dto, Long senderIdFromAuth) {

        Letter letter = letterRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 잘못됨"));

        if(!letter.getSenderId().equals(senderIdFromAuth)) {
            throw new IllegalStateException("본인이 발급한 코드만 등록할 수 있습니다");
        }

        if (letter.getStatus() != Status.ISSUED) {
            throw new IllegalStateException("이미 등록된 코드입니다");
        }

        letter.setStatus(Status.REGISTERED);
        letter.setRegisteredAt(LocalDateTime.now());

        return letter.getId();
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

        if (!letter.getSenderId().equals(senderId)) {
            throw new IllegalStateException("본인의 편지만 취소할 수 있습니다.");
        }

        letter.setStatus(Status.CANCELED);
        return letter.getId();
    }
}
