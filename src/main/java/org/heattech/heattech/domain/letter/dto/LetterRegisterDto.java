package org.heattech.heattech.domain.letter.dto;

import lombok.Getter;

@Getter
public class LetterRegisterDto {
    private String code;

    public LetterRegisterDto() {}
    public LetterRegisterDto(String code) {
        this.code = code;
    }
}
