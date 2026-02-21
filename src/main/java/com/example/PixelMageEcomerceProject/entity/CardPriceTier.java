package com.example.PixelMageEcomerceProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CARD_PRICE_TIER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardPriceTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Integer tierId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "card_template_id")
    @JsonBackReference("cardTemplate-cardPriceTiers")
    private CardTemplate cardTemplate;

    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity;

    @Column(name = "max_quantity")
    private Integer maxQuantity;

    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
