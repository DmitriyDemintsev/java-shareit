package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.user.dto.UserRequestDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /* PostMapping */
    public ResponseEntity<Object> newUser(UserRequestDto requestDto) {
        return post("", requestDto);
    }

    //PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(UserRequestDto requestDto, long userId) {
        Map<String, Object> parameters = Map.of("id", userId);
        return patch("/{id}", userId, parameters, requestDto);
    }

    /* DeleteMapping("/{id}") */
    public ResponseEntity<Object> deleteUser(long id) {
        Map<String, Object> parameters = Map.of("id", id);
        return delete("/{id}", id, parameters);
    }

    /* GetMapping */
    public ResponseEntity<Object> getUsers() {
        return get("", null, null);
    }

    /* GetMapping("/{id}") */
    public ResponseEntity<Object> getUserById(long userId) {
        Map<String, Object> parameters = Map.of("id", userId);
        return get("/{id}", userId, parameters);
    }
}
