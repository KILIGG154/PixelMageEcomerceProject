package com.example.PixelMageEcomerceProject.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.PixelMageEcomerceProject.entity.CardTemplate;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "divine_helpers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DivineHelper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "divine_helper_id")
    private Integer divineHelperId;

    @OneToOne
    @JoinColumn(name = "card_template_id", nullable = false)
    @JsonBackReference("cardTemplate-divineHelper")
    private CardTemplate cardTemplate;

    @Column(name = "upright_meaning", columnDefinition = "NVARCHAR(MAX)")
    private String uprightMeaning;

    @Column(name = "reversed_meaning", columnDefinition = "NVARCHAR(MAX)")
    private String reversedMeaning;

    @Column(name = "zodiac_sign", length = 50)
    private String zodiacSign;

    @Column(name = "element", length = 50)
    private String element;

    @Column(name = "keywords", columnDefinition = "NVARCHAR(500)")
    private String keywords;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
