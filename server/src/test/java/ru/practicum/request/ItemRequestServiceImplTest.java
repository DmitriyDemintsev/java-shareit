package ru.practicum.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.exception.ItemRequestValidationException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.request.dto.ItemRequestMapper;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private ItemRequestMapper itemRequestMapper;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void create_whenItemRequestValid_thenSavedItemRequest() {
        User requestor = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));

        ItemRequest savedRequest = new ItemRequest(0L, "срочно нужен аккумуляторный шуруповерт",
                requestor, LocalDateTime.now());
        when(itemRequestRepository.save(savedRequest)).thenReturn(savedRequest);

        ItemRequest actualRequest = itemRequestService.create(requestor.getId(), savedRequest);

        assertEquals(savedRequest, actualRequest);
        verify(itemRequestRepository).save(savedRequest);
    }

    @Test
    void create_whenItemRequestIsEmpty_thenItemRequestValidationException() {
        User requestor = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        ItemRequest savedRequest = new ItemRequest(0L, "",
                requestor, LocalDateTime.now());

        assertThrows(ItemRequestValidationException.class, () -> itemRequestService.create(requestor.getId(), savedRequest));
        verify(itemRequestRepository, never()).save(savedRequest);
    }

    @Test
    void getAllMineRequests_whenUserFound_thenRequestsReturn() {
        User requestor = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));

        ItemRequest festRequest = new ItemRequest(0L, "срочно нужен аккумуляторный шуруповерт",
                requestor, LocalDateTime.now().minusDays(3));
        ItemRequest secondRequest = new ItemRequest(1L, "ищу перфоратор",
                requestor, LocalDateTime.now().minusDays(1));
        ItemRequest thirdRequest = new ItemRequest(2L, "кто готов поделиться стремянкой?",
                requestor, LocalDateTime.now());

        List<ItemRequest> expectedItemRequests = new ArrayList<>();
        expectedItemRequests.add(festRequest);
        expectedItemRequests.add(secondRequest);
        expectedItemRequests.add(thirdRequest);

        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        when(itemRequestRepository.findByRequestor(requestor, sortByCreated)).thenReturn(expectedItemRequests);
        List<ItemRequest> actualItemRequests = itemRequestService.getAllMineRequests(requestor.getId());
        assertEquals(expectedItemRequests, actualItemRequests);
    }

    @Test
    void getAllMineRequests_whenUserNotFound_thenUserNotFoundException() {
        long id = 0L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> itemRequestService.getAllMineRequests(id));
    }

    @Test
    void getAllRequest_whenUserFound_thenListRequestsReturn() {
        User requestor = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));

        ItemRequest festRequest = new ItemRequest(0L, "срочно нужен аккумуляторный шуруповерт",
                requestor, LocalDateTime.now());
        ItemRequest secondRequest = new ItemRequest(1L, "ищу перфоратор",
                requestor, LocalDateTime.now());

        List<ItemRequest> expectedItemRequests = new ArrayList<>();
        expectedItemRequests.add(festRequest);
        expectedItemRequests.add(secondRequest);

        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(0, 20, sortByCreated);
        when((itemRequestRepository.findAllByRequestorNot(requestor, pageable)))
                .thenReturn(new PageImpl<>(expectedItemRequests));

        List<ItemRequest> actualRequests = itemRequestService.getAllRequest(requestor.getId(), 0, 20);

        assertEquals(expectedItemRequests, actualRequests);
    }

    @Test
    void getAllRequest_whenUserNotFound_thenUserNotFoundException() {
        long id = 0L;
        when(itemRequestRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.getRequestById(id));

    }

    @Test
    void getRequestById_whenRequestFound_thenReturnedRequest() {
        long id = 0L;
        ItemRequest expectedRequest = new ItemRequest();
        when(itemRequestRepository.findById(id)).thenReturn(Optional.of(expectedRequest));

        ItemRequest actualRequest = itemRequestService.getRequestById(id);

        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void getRequestById_whenRequestNotFound_thenItemRequestNotFoundException() {
        long id = 0L;
        when(itemRequestRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.getRequestById(id));
    }
}
