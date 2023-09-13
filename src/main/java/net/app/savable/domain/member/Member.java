package net.app.savable.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import net.app.savable.domain.challenge.ParticipationChallenge;

import java.util.List;

@Entity
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private Long reward;

    @Column(nullable = false)
    private Long savings;
    private String phoneNumber;

    @Column(nullable = false)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountState accountState;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL) // Member 1 : N ParticipationChallenge
    private List<ParticipationChallenge> participationChallengeList;
}
