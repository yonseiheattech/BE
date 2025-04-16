package org.heattech.heattech.domain.letter.domain;




import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="thanks_note_id")
    private ThanksNote thanksNote;

}
