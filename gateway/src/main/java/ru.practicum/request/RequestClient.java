package ru.practicum.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /* PostMapping */
    public ResponseEntity<Object> itemRequest(long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    /* GetMapping */
    public ResponseEntity<Object> getMineRequests(long userId) {
        return get("", userId);
    }

    /* GetMapping("/all") */
    public ResponseEntity<Object> getAll(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    /* GetMapping("/{requestId}") */
    public ResponseEntity<Object> getItemRequestById(long userId, Long requestId) {
        Map<String, Object> parameters = Map.of("requestId", requestId);
        return get("/{requestId}", userId, parameters);
    }
}
