package com.example.PixelMageEcomerceProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "CARDS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "NFC_UUID", nullable = false, unique = true)
    private String nfcUuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_template_id", nullable = false, referencedColumnName = "card_template_id")
    @JsonBackReference("cardTemplate-cards")
    private CardTemplate cardTemplate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "product_id")
    @JsonBackReference("product-cards")
    private Product product;

    @Column(name = "custom_text", columnDefinition = "TEXT")
    private String customText;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship: Card 1-N CardContent
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("card-cardContents")
    private List<CardContent> cardContents;

    // Relationship: Card 1-N OrderItem
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("card-orderItems")
    private List<OrderItem> orderItems;
}
