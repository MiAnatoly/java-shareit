package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.valide.ValidPage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> add(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@Validated({Create.class}) @RequestBody BookingDto bookingDto
	) {
		log.info("Post booking with userId={}", userId);
		return bookingClient.add(userId, bookingDto);
	}  // добавить запрос

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approved(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable Long bookingId,
			@RequestParam Boolean approved
	) {
		log.info("Patch booking with bookingId {}, userId={}, approved={}", bookingId, userId, approved);
		return bookingClient.approved(userId, bookingId, approved);
	} // подтверждение или отклонения запроса

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> findById(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable long bookingId
	) {
		log.info("Get booking with userId={}, bookingId={}", userId, bookingId);
		return bookingClient.findById(userId, bookingId);
	} // показать запрос пользователя владельцу запроса или вещи

	@GetMapping()
	public ResponseEntity<Object> findAllForUser(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String state,
			@RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
			@RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
	) {
		State bookingState = validate(state);
		int page = ValidPage.page(from, size);
		log.info("Get booking with userId={}", userId);
		return bookingClient.findAllForUser(userId, bookingState, page, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findAllForOwner(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String state,
			@RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
			@RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(50) int size
	) {
		State bookingState = validate(state);
		int page = ValidPage.page(from, size);
		log.info("Get booking with ownerId={}", userId);
		return bookingClient.findAllForOwner(userId, bookingState, page, size);
	}

	private State validate(String state) {
		State bookingState = null;
		for (State value : State.values()) {
			if (value.name().equals(state)) {
				bookingState = value;
			}
		}
		if (bookingState == null) {
			throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
		}
		return bookingState;
	}
}
