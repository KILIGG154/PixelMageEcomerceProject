package com.example.PixelMageEcomerceProject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "CARD_TEMPLATES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_template_id")
    private Integer cardTemplateId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "design_path", length = 255)
    private String designPath;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship: CardTemplate 1-N Card
    @OneToMany(mappedBy = "cardTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("cardTemplate-cards")
    private List<Card> cards;

    // Relationship: CardTemplate 1-N CardPriceTier
    @OneToMany(mappedBy = "cardTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("cardTemplate-cardPriceTiers")
    private List<CardPriceTier> cardPriceTiers;
}
