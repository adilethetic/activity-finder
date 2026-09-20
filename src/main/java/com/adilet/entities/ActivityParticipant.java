package com.adilet.entities;

import com.adilet.enums.ParticipantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "activity_participants",
uniqueConstraints = @UniqueConstraint(name = "uk_participant_user", columnNames = {"activity_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class ActivityParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_participants_activity"))
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_participants_user"))
    private User user;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ParticipantStatus status;

    @PrePersist
    void onCreate(){
        this.joinedAt = Instant.now();
        if(this.status == null){
            this.status = ParticipantStatus.JOINED;
        }
    }
}
