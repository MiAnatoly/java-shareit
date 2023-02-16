package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestDao;
    private final UserRepository userDao;
    private final ItemRepository itemDao;

    @Transactional
    @Override
    public ItemRequestRefundDto add(Long userId, ItemRequestDto requestDto) {
        User user = userDao.findById(userId).orElseThrow(() -> new NotObjectException("нет пользователя"));
        ItemRequest request = RequestMapper.toRequest(user, requestDto);
        requestDao.save(request);
        return RequestMapper.toRequestRefundDto(request, List.of());
    }

    @Override
    public List<ItemRequestRefundDto> findAllOwner(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new NotObjectException("нет пользователя"));
        List<ItemRequest> requests = requestDao.findByRequester(user);
        return itemsForAllRequests(requests);
    }

    @Override
    public List<ItemRequestRefundDto> findAllNotOwner(Long userId, Integer page, Integer size) {
        User user = userDao.findById(userId).orElseThrow(() -> new NotObjectException("нет пользователя"));
        List<ItemRequest> requests = requestDao.findByNotRequester(
                user,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"))
        ).getContent();
        return itemsForAllRequests(requests);
    }

    @Override
    public ItemRequestRefundDto findById(Long userId, Long requestId) {
        userDao.findById(userId).orElseThrow(() -> new NotObjectException("нет пользователя"));
        ItemRequest request = requestDao.findById(requestId).orElseThrow(() -> new NotObjectException("нет запроса"));
        List<Item> items = itemDao.findByRequester(request);
        return RequestMapper.toRequestRefundDto(request, items);
    }

    private List<ItemRequestRefundDto> itemsForAllRequests(List<ItemRequest> requests) {
        Map<ItemRequest, List<Item>> itemsRequest = itemDao.findByRequesters(requests)
                .stream()
                .collect(Collectors.groupingBy(Item::getRequest));
        List<ItemRequestRefundDto> requestsDto = new ArrayList<>();
        for (ItemRequest request : requests) {
            List<Item> items = itemsRequest.getOrDefault(request, List.of());
            requestsDto.add(RequestMapper.toRequestRefundDto(request, items));
        }
        return requestsDto;
    }
}
