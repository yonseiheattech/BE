package org.heattech.heattech.domain.letter.service;

import org.heattech.heattech.domain.letter.domain.Letter;
import org.heattech.heattech.domain.letter.domain.Status;
import org.heattech.heattech.domain.letter.dto.letter.LetterCancelDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterDeliverDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterRegisterDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterReplyDto;
import org.heattech.heattech.domain.letter.dto.letter.LetterResponseDto;
import org.heattech.heattech.domain.letter.repository.LetterRepository;
import org.heattech.heattech.domain.member.domain.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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


        letterRepository.save(letter);
        return letter.getId();
    }

    public Long deliverLetter(LetterDeliverDto dto, Long senderId) {
        Letter letter = letterRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new IllegalArgumentException("코드가 없네"));

        if (!letter.getSenderId().equals(senderId)) {
            throw new IllegalStateException("본인의 편지만 취소할 수 있습니다.");
        }

        letter.setStatus(Status.DELIVERED);
        letter.setDeliveredAt(LocalDateTime.now());

        letterRepository.save(letter);
        return letter.getId();
    }


    //전체편지 가져오기
    public List<LetterResponseDto> getMyLetters(Long userId, Role role) {
        List<Letter> letters;

        if (role == Role.SENDER) {
            letters =letterRepository.findAllBySenderId(userId);
        } else if (role == Role.VOLUNTEER) {
            letters = letterRepository.findAllByVolunteerId(userId);
        } else {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        return letters.stream().map(LetterResponseDto::new).collect(Collectors.toList());

    }

    //봉사자 편지 하나 가져오기
    public Letter volGetMyLetterByCode(String code, Long userId, Role role) {
        Letter letter = letterRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("편지를 찾을 수 없습니다."));

        if (role == Role.VOLUNTEER) {
            if (!letter.getVolunteerId().equals(userId)) {
                throw new AccessDeniedException("해당 편지를 조회할 권한이 없습니다.");
            }
        } else {
            throw new AccessDeniedException("권한이 없는 사용자입니다.");
        }
        return letter;
    }


    //편지 하나 가져오기
    public Letter getMyLetterByCode(String code, Long userId, Role role) {

        // 편지 조회
        Letter letter = letterRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("편지를 찾을 수 없습니다."));

        // 역할에 따라 검증
        if (role == Role.SENDER) {
            if (!letter.getSenderId().equals(userId)) {
                throw new AccessDeniedException("해당 편지를 조회할 권한이 없습니다.");
            }
        } else if (role == Role.VOLUNTEER) {
            if (!letter.getVolunteerId().equals(userId)) {
                throw new AccessDeniedException("해당 편지를 조회할 권한이 없습니다.");
            }
        } else {
            throw new AccessDeniedException("권한이 없는 사용자입니다.");
        }

        return letter;
    }


}
