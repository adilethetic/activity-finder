package com.adilet.entities;

import com.adilet.enums.ActivityCategoryStatus;
import com.adilet.enums.ActivityLevelStatus;
import com.adilet.enums.ActivityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ActivityCategoryStatus category;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 20)
    private ActivityLevelStatus level;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "city", nullable =false, length = 100)
    private String city;

    @Column(name = "location_name", nullable =false, length = 200)
    private String locationName;

    @Column(name = "max_participants", nullable =false)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActivityStatus  activityStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "creator_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_activities_creator")
    )
    private User creator;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate(){
        this.createdAt = Instant.now();
    }



}
