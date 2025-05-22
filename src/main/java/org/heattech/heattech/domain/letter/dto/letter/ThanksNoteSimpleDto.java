package org.heattech.heattech.domain.letter.dto.letter;

import lombok.Data;

@Data
public class ThanksNoteSimpleDto {
    private Long id;
    private String content;

    public ThanksNoteSimpleDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
