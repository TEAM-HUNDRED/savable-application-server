package net.app.savable.domain.shop;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Getter
public class GiftcardProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Boolean inOnSale; // 판매 여부 (true: 판매중, false: 판매중지)
}