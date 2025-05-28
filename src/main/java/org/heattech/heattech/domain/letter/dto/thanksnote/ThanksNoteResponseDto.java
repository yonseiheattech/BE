package org.heattech.heattech.domain.letter.dto.thanksnote;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.heattech.heattech.domain.letter.domain.ThanksNote;

@Getter
@AllArgsConstructor
public class ThanksNoteResponseDto {
    private Long id;
    private String content;

    public static ThanksNoteResponseDto fromEntity(ThanksNote note) {
        return new ThanksNoteResponseDto(
                note.getId(),
                note.getContent()
        );
    }
}
