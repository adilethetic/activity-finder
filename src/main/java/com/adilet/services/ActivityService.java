package com.adilet.services;

import com.adilet.dtos.ActivityResponse;
import com.adilet.dtos.CreateActivityRequest;
import com.adilet.entities.Activity;
import com.adilet.entities.ActivityParticipant;
import com.adilet.entities.User;
import com.adilet.enums.ActivityCategoryStatus;
import com.adilet.exception.ConflictException;
import com.adilet.exception.NotFoundException;
import com.adilet.repositories.ActivityParticipantRepository;
import com.adilet.repositories.ActivityRepository;
import com.adilet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Transactional
    public ActivityResponse create(CreateActivityRequest request, Long userId) {
        User creator = userRepository.getReferenceById(userId);

        Activity activity = new Activity();
        activity.setTitle(request.title());
        activity.setDescription(request.description());
        activity.setCategory(request.category());
        activity.setLevel(request.level());
        activity.setStartTime(request.startTime());
        activity.setCity(request.city());
        activity.setLocationName(request.locationName());
        activity.setMaxParticipants(request.maxParticipants());
        activity.setCreator(creator);

        activityRepository.save(activity);
        addParticipant(activity, creator);

        return toResponse(activity);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> findAll(ActivityCategoryStatus category) {
        List<Activity> activities = (category == null) ? activityRepository.findAllByOrderByStartTimeAsc()
                : activityRepository.findByCategoryOrderByStartTimeAsc(category);

        return activities.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ActivityResponse findById(Long activityId) {
        return toResponse(getActivity(activityId));
    }

    @Transactional
    public void join(Long activityId, Long userId) {
        Activity activity = getActivity(activityId);

        if (participantRepository.existsByActivityIdAndUserId(activityId, userId)) {
            throw new ConflictException("You are already joined this activity");
        }
        if (participantRepository.countByActivityId(activityId) >= activity.getMaxParticipants()) {
            throw new ConflictException("No empty seats left for this activity");
        }

        addParticipant(activity, userRepository.getReferenceById(userId));
    }

    @Transactional
    public void leave(Long activityId, Long userId) {
        Activity activity = getActivity(activityId);

        if (activity.getCreator().getId().equals(userId)) {
            throw new ConflictException("The organizer cannot log out of his activity");
        }

        ActivityParticipant participant = participantRepository
                .findByActivityIdAndUserId(activityId, userId)
                .orElseThrow(() -> new ConflictException("You are not in this activity"));

        participantRepository.delete(participant);
    }

    private void addParticipant(Activity activity, User user) {
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivity(activity);
        participant.setUser(user);
        participantRepository.save(participant);
    }

    private Activity getActivity(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found " + activityId));
    }

    private ActivityResponse toResponse(Activity activity) {
        List<ActivityResponse.Participant> participants = participantRepository
                .findByActivityId(activity.getId())
                .stream()
                .map(p -> new ActivityResponse.Participant(p.getUser().getId(), p.getUser().getUsername()))
                .toList();

        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getCategory(),
                activity.getLevel(),
                activity.getStartTime(),
                activity.getCity(),
                activity.getLocationName(),
                activity.getMaxParticipants(),
                participants.size(),
                activity.getCreator().getUsername(),
                participants
        );
    }


}
