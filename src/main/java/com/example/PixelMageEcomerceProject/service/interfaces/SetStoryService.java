package com.example.PixelMageEcomerceProject.service.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.PixelMageEcomerceProject.dto.response.SetStoryResponse;
import com.example.PixelMageEcomerceProject.entity.SetStory;

@Service
public interface SetStoryService {

    void checkAndUnlockStories(Integer userId);

    void revokeStoriesIfConditionNotMet(Integer userId);

    List<SetStory> getAllStories();

    List<SetStory> getUnlockedStories(Integer userId);

    SetStory createStory(SetStory story);

    /**
     * Get story by id — no unlock gate. Used by Admin CRUD and Staff bypass endpoint.
     */
    SetStory getStoryById(Integer id);

    /**
     * Get story by id with unlock gate. Throws StoryNotUnlockedException (→ 403) if
     * the customer has no active UserStoryUnlock for this story.
     */
    SetStoryResponse getStoryById(Integer storyId, Integer userId);

    /**
     * Get story by id with no unlock gate, returns DTO. Used by Staff/Admin endpoint.
     */
    SetStoryResponse getStoryByIdNoGate(Integer storyId);

    SetStory updateStory(SetStory story);

    void deleteStory(Integer id);
}

