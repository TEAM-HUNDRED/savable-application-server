package net.app.savable.domain.challenge;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Entity
@Getter
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Timestamp timeStamp;

    @Column(nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'WAITING'")
    private VerificationState state;

    @ManyToOne(fetch = FetchType.LAZY) // Verification N : 1 ParticipationChallenge (지연로딩)
    @JoinColumn(name = "participation_challenge_id")
    private ParticipationChallenge participationChallenge;
}
