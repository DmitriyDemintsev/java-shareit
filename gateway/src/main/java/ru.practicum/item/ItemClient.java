package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /* PostMapping */
    public ResponseEntity<Object> addItem(long userId, ItemDto requestDto) {
        return post("", userId, requestDto);
    }

    /* PatchMapping("/{id}") */
    public ResponseEntity<Object> updateItem(long userId, ItemDto requestDto, long id) {
        Map<String, Object> parameters = Map.of("id", id);
        return patch("/{id}", userId, parameters, requestDto);
    }

    /* DeleteMapping("/{itemId}") */
    public ResponseEntity<Object> deleteItem(long id) {
        Map<String, Object> parameters = Map.of("id", id);
        return delete("/{itemId}", id, parameters);    }

    /* GetMapping */
    public ResponseEntity<Object> getItems(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    /* GetMapping("/{id}") */
    public ResponseEntity<Object> getItemById(long userId, Long id) {
        Map<String, Object> parameters = Map.of("id", id);
        return get("/{id}", userId, parameters);
    }

    /* GetMapping("/{search}") */
    public ResponseEntity<Object> getSearchItem(String query, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "query", query,
                "from", from,
                "size", size
        );
        return get("/search?text={query}&from={from}&size={size}", null, parameters);
    }
}
