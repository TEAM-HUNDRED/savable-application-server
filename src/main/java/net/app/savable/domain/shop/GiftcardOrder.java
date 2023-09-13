package net.app.savable.domain.shop;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Entity
@Getter
public class GiftcardOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String PositivePoint;

    @Column(nullable = false)
    private String negativePoint;

    @Column(nullable = false)
    private Long Quantity;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    @ColumnDefault("'WAITING'")
    private SendState sendState;
}
