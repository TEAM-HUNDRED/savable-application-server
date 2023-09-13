package net.app.savable.domain.history;

import jakarta.persistence.*;
import lombok.Getter;

import java.security.Timestamp;

@Entity
@Getter
public class SavingsHistory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long savings;

    @Column(nullable = false)
    private Long totalSavings; // 누적 적립금

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private String description;
}
