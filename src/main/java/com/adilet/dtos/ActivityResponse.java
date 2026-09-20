package com.adilet.dtos;

import com.adilet.enums.ActivityCategoryStatus;
import com.adilet.enums.ActivityLevelStatus;
import java.time.Instant;
import java.util.List;

public record ActivityResponse(
        Long id,
        String title,
        String description,
        ActivityCategoryStatus category,
        ActivityLevelStatus level,
        Instant startTime,
        String city,
        String locationName,
        int maxParticipants,
        int joinedCount,
        String creatorName,
        List<Participant> participants

) {

    public record Participant(Long id, String username){
    }
}
