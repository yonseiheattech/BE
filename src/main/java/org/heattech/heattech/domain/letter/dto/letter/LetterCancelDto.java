package org.heattech.heattech.domain.letter.dto.letter;

import lombok.Getter;

@Getter
public class LetterCancelDto {
    private String code;

    public LetterCancelDto(String code) {
        this.code = code;
    }
}
