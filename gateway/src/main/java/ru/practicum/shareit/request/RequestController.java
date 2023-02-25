package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.valide.ValidPage;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> add(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody ItemRequestDto requestDto
    ) {
        log.info("Post request with userId={}", userId);
        return requestClient.add(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllOwner(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        log.info("Get request with ownerId={}", userId);
        return requestClient.findAllOwner(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllNotOwner(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
    ) {
        int page = ValidPage.page(from, size);
        log.info("Get request with userId={}", userId);
        return requestClient.findAllNotOwner(userId, page, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> findById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        log.info("Get request with userId={}, requestId={}", userId, requestId);
        return requestClient.findById(userId, requestId);
    }
}
