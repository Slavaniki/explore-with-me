package ru.practicum.explorewithme.dto.participation;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto implements Serializable {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private String status;
    private Integer participantLimit;
}
