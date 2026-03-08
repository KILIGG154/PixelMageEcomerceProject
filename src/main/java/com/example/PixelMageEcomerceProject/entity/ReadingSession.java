package com.example.PixelMageEcomerceProject.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.PixelMageEcomerceProject.entity.Account;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reading_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference("account-readingSessions")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spread_id", nullable = false)
    @JsonBackReference("spread-readingSessions")
    private Spread spread;

    @Column(name = "main_question", columnDefinition = "NVARCHAR(500)")
    private String mainQuestion;

    @Column(name = "mode", length = 50)
    private String mode; // EXPLORE or YOUR_DECK

    @Column(name = "status", length = 50)
    private String status; // PENDING, INTERPRETING, COMPLETED

    @Column(name = "ai_interpretation", columnDefinition = "NVARCHAR(MAX)")
    private String aiInterpretation;

    @OneToMany(mappedBy = "readingSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("readingSession-readingCards")
    private List<ReadingCard> readingCards;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
