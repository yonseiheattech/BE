package org.heattech.heattech.domain.letter.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Letter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 8)
    private String code;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "volunteer_id")
    private Long volunteerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
    @Column(name = "replied_at")
    private LocalDateTime repliedAt;
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    private String thanksNote;

}
