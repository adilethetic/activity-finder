package com.adilet.controllers;

import com.adilet.dtos.MessageResponse;
import com.adilet.dtos.SendMessageRequest;
import com.adilet.security.UserPrincipal;
import com.adilet.services.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities/{activityId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public List<MessageResponse> history(@PathVariable Long activityId,
                                         @AuthenticationPrincipal UserPrincipal principal) {
        return messageService.history(activityId, principal.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse send(@PathVariable Long activityId,
                                @Valid @RequestBody SendMessageRequest request,
                                @AuthenticationPrincipal UserPrincipal principal) {
        return messageService.send(activityId, request, principal.getId());
    }
}
