package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService service;

    @PostMapping
    public ItemRequestRefundDto add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemRequestDto requestDto
    ) {
        log.info("Post request with userId={}", userId);
        return service.add(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestRefundDto> findAllOwner(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("Get request with ownerId={}", userId);
        return service.findAllOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestRefundDto> findAllNotOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("Get request with userId={}", userId);
        return service.findAllNotOwner(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestRefundDto findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        log.info("Get request with userId={}, requestId={}", userId, requestId);
        return service.findById(userId, requestId);
    }
}
