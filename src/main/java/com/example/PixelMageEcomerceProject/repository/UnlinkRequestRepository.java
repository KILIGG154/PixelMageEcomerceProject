package com.example.PixelMageEcomerceProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PixelMageEcomerceProject.entity.UnlinkRequest;
import com.example.PixelMageEcomerceProject.enums.UnlinkRequestStatus;

/**
 * Repository for UnlinkRequest (TASK-05).
 */
public interface UnlinkRequestRepository extends JpaRepository<UnlinkRequest, Long> {

    Optional<UnlinkRequest> findByToken(String token);

    List<UnlinkRequest> findByStatusOrderByCreatedAtDesc(UnlinkRequestStatus status);
}
