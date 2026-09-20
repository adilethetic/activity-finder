package com.adilet.services;

import com.adilet.dtos.MessageResponse;
import com.adilet.dtos.SendMessageRequest;
import com.adilet.entities.Activity;
import com.adilet.entities.Message;
import com.adilet.exception.ForbiddenException;
import com.adilet.exception.NotFoundException;
import com.adilet.repositories.ActivityParticipantRepository;
import com.adilet.repositories.ActivityRepository;
import com.adilet.repositories.MessageRepository;
import com.adilet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ActivityRepository activityRepository;
    private final ActivityParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse send(Long activityId, SendMessageRequest request, Long userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(("Activity not found " + activityId)));

        requireParticipant(activityId, userId);

        Message message = new Message();
        message.setActivity(activity);
        message.setSender(userRepository.getReferenceById(userId));
        message.setText(request.text());

        return MessageResponse.from(messageRepository.save(message));
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> history(Long activityId, Long userId) {
        requireParticipant(activityId, userId);

        return messageRepository.findTop50ByActivityIdOrderByCreatedAtDesc(activityId)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    private void requireParticipant(Long activityId, Long userId) {
        if (!participantRepository.existsByActivityIdAndUserId(activityId, userId)) {
            throw new ForbiddenException("Chat is only available for participants");
        }
    }


}
