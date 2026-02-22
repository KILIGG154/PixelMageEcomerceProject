package com.example.PixelMageEcomerceProject.repository;

import com.example.PixelMageEcomerceProject.entity.CollectionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionItemRepository extends JpaRepository<CollectionItem, Integer> {

    List<CollectionItem> findByCardCollectionCollectionId(Integer collectionId);

    Optional<CollectionItem> findByCardCollectionCollectionIdAndCardCardId(Integer collectionId, Integer cardId);

    boolean existsByCardCollectionCollectionIdAndCardCardId(Integer collectionId, Integer cardId);

    void deleteByCardCollectionCollectionIdAndCardCardId(Integer collectionId, Integer cardId);

    @Query("SELECT COUNT(ci) FROM CollectionItem ci WHERE ci.cardCollection.collectionId = :collectionId")
    Long countByCollectionId(@Param("collectionId") Integer collectionId);
}
