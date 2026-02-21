package com.example.PixelMageEcomerceProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CARD_CONTENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "card_id")
    @JsonBackReference("card-cardContents")
    private Card card;

    @Column(name = "content_type", length = 50)
    private String contentType;

    @Column(name = "content_data", columnDefinition = "TEXT")
    private String contentData;

    @Column(name = "position", length = 50)
    private String position;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
