package com.adilet.repositories;

import com.adilet.entities.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {

    boolean existsByActivityIdAndUserId(Long activityId, Long userId);

    int countByActivityId(Long activityId);

    Optional<ActivityParticipant> findByActivityIdAndUserId(Long activityId, Long userId);

    List<ActivityParticipant> findByActivityId(Long activityId);
}
