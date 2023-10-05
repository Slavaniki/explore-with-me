package ru.practicum.explorewithme.dto.location;

import com.sun.istack.NotNull;
import lombok.Data;

import java.io.Serializable;
@Data
public class NewLocationDto implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
    @NotNull
    private Integer radius;
}