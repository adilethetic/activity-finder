package com.adilet.dtos;

import com.adilet.enums.ActivityCategoryStatus;
import com.adilet.enums.ActivityLevelStatus;
import jakarta.validation.constraints.*;
import java.time.Instant;

public record CreateActivityRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 150)
        String title,

        @Size(max = 4000)
        String description,

        @NotNull(message = "Choose category")
        ActivityCategoryStatus category,

        @NotNull(message = "Choose level")
        ActivityLevelStatus level,

        @NotNull
        @Future(message = "Start time can not be in past")
        Instant startTime,

        @NotBlank
        @Size(max = 100, message = "City can not be empty")
        String city,

        @NotBlank(message = "Show location")
        @Size(max = 200)
        String locationName,

        @NotNull
        @Min(value = 2, message = "min 2 people")
        @Max(value = 50, message = "max 50 people")
        Integer maxParticipants
) {
}
