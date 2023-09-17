package ru.practicum.explorewithme.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.client.EndpointHit;
import ru.practicum.explorewithme.client.ViewsStats;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Component
public class Client {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${stats-service.url}")
    private String serverUrl;

    public List<ViewsStats> getViews(String start, String end, List<String> uris, Boolean unique) {
        String uri = serverUrl + "/stats?start={start}&end={end}";
        Map<String, Object> parameters;
        if (uris == null) {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique
            );
            uri = uri + "&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", uris,
                    "unique", unique
            );
            uri = uri + "&uris={uris}&unique={unique}";
        }
        ResponseEntity<List<ViewsStats>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, parameters);
        return response.getBody();
    }

    public void postEndpointHit(EndpointHit hit) {
        String uri = serverUrl + "/hit";
        try {
            restTemplate.postForEntity(new URI(uri), hit, ViewsStats.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
