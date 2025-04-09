package org.heattech.heattech.domain.letter.dto;

import lombok.Getter;

@Getter
public class LetterReplyDto {
    private String code;

    public LetterReplyDto () {}
    public LetterReplyDto (String code) {
        this.code = code;

    }

}
