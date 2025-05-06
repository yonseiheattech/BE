package org.heattech.heattech.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.heattech.heattech.domain.member.domain.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private String username;
    private Role role;
}
