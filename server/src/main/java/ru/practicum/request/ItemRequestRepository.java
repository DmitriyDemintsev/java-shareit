package ru.practicum.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    ItemRequest save(ItemRequest itemRequest);

    List<ItemRequest> findByRequestor(User requestor, Sort sort);

    ItemRequest getItemRequestById(long id);

    Page<ItemRequest> findAllByRequestorNot(User requestor, Pageable pageable);
}
