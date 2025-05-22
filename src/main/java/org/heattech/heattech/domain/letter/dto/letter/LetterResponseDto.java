package org.heattech.heattech.domain.letter.dto.letter;

import lombok.Data;
import org.heattech.heattech.domain.letter.domain.Letter;

import java.time.LocalDateTime;

@Data
public class LetterResponseDto {
    private Long id;
    private String code;
    private Long senderId;
    private Long volunteerId;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime registeredAt;
    private LocalDateTime repliedAt;
    private LocalDateTime deliveredAt;
    private ThanksNoteSimpleDto thanksNote; // thanksNote 전체가 아닌, 간단 필드만!

    public LetterResponseDto(Letter letter) {
        this.id = letter.getId();
        this.code = letter.getCode();
        this.senderId = letter.getSenderId();
        this.volunteerId = letter.getVolunteerId();
        this.status = letter.getStatus().name();
        this.issuedAt = letter.getIssuedAt();
        this.registeredAt = letter.getRegisteredAt();
        this.repliedAt = letter.getRepliedAt();
        this.deliveredAt = letter.getDeliveredAt();
        // thanksNote가 있으면 간단 필드만 추출
        if (letter.getThanksNote() != null) {
            this.thanksNote = new ThanksNoteSimpleDto(
                    letter.getThanksNote().getId(),
                    letter.getThanksNote().getContent()
            );
        }
    }
}
