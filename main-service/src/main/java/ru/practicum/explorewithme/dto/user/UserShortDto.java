package ru.practicum.explorewithme.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserShortDto implements Serializable {
    private final Long id;
    private final String name;
}