package com.example.PixelMageEcomerceProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PixelMageEcomerceProject.entity.ReadingSession;

@Repository
public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Integer> {
    List<ReadingSession> findByAccount_AccountId(Integer accountId);
}
