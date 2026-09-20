package com.adilet.dtos;

import com.adilet.entities.Message;
import java.time.Instant;

public record MessageResponse(
        Long id,
        String text,
        Long senderId,
        String senderName,
        Instant createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getText(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getCreatedAt()
        );
    }
}
