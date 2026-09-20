package com.adilet.controllers;

import com.adilet.dtos.ActivityResponse;
import com.adilet.dtos.CreateActivityRequest;
import com.adilet.enums.ActivityCategoryStatus;
import com.adilet.security.UserPrincipal;
import com.adilet.services.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public List<ActivityResponse> findAll(@RequestParam(required = false) ActivityCategoryStatus category) {
        return activityService.findAll(category);
    }

    @GetMapping("/{id}")
    public ActivityResponse findById(@PathVariable Long id) {
        return activityService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse create(@Valid @RequestBody CreateActivityRequest request,
                                   @AuthenticationPrincipal UserPrincipal principal) {
        return activityService.create(request, principal.getId());
    }

    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void join(@PathVariable Long id,
                     @AuthenticationPrincipal UserPrincipal principal) {
        activityService.join(id, principal.getId());
    }

    @DeleteMapping("/{id}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leave(@PathVariable Long id,
                      @AuthenticationPrincipal UserPrincipal principal) {
        activityService.leave(id, principal.getId());
    }
}
