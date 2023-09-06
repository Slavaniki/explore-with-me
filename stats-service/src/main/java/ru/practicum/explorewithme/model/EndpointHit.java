package ru.practicum.explorewithme.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "endpoint_hit")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
