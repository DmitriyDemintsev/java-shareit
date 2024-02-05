package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.exception.ItemRequestValidationException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequest create(Long userId, ItemRequest itemRequest) {
        if (itemRequest.getDescription() == null || itemRequest.getDescription().isEmpty()) {
            throw new ItemRequestValidationException("Отсутствует описание запроса");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user не найден"));
        itemRequest.setRequestor(user);
        itemRequest = itemRequestRepository.save(itemRequest);
        return itemRequest;
    }

    @Override
    public List<ItemRequest> getAllMineRequests(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user не найден"));
        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        return itemRequestRepository.findByRequestor(user, sortByCreated);
    }

    @Override
    public List<ItemRequest> getAllRequest(long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user не найден"));
        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from, size, sortByCreated);
        return itemRequestRepository.findAllByRequestorNot(user, pageable).getContent();
    }

    @Override
    public ItemRequest getRequestById(long userId) {
        return itemRequestRepository.findById(userId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Запррос не найден"));
    }
}
