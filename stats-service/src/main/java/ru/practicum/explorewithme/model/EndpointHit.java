package ru.practicum.explorewithme.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "endpoint_hit")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "app")
    private String app;
    @Column(name = "uri")
    private String uri;
    @Column(name = "ip")
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
