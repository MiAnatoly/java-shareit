package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerItTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BookingService service;

    // метод add
    @Test
    void add_ok() throws Exception {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusMinutes(2));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setItemId(1L);



        when(service.add(anyLong(), any())).thenReturn(new BookingItemDto());

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).add(anyLong(), any());
    }

    @Test
    void add_whenStartNotValid_Past_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                1L
        );

        when(service.add(anyLong(), any())).thenReturn(new BookingItemDto());

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    @Test
    void add_whenNotValid_DateNull_thanReturnBadRequest() throws Exception {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto(
                null,
                null,
                1L
        );

        when(service.add(anyLong(), any())).thenReturn(new BookingItemDto());

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest());

        verify(service, never()).add(anyLong(), any());
    }

    // метод approved
    @Test
    void approved_ok() throws Exception {
        Long userId = 1L;
        Boolean approved = true;
        Long bookingId = 1L;

        when(service.approved(anyLong(), anyLong(), anyBoolean())).thenReturn(new BookingItemDto());

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk());

        verify(service).approved(anyLong(), anyLong(), anyBoolean());
    }

    // метод findById
    @Test
    void findById_ok() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        when(service.findById(anyLong(), anyLong())).thenReturn(new BookingItemDto());

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(service).findById(anyLong(), anyLong());
    }

    // метод findAllForUser
    @Test
    void findAllForUser_ok() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        when(service.findAllForUser(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/bookings", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk());

        verify(service).findAllForUser(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void findAllForUser_whenNotValid_State_thanReturnInternalServerError() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        when(service.findAllForUser(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/bookings", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "sdf")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isInternalServerError());

        verify(service, never()).findAllForUser(anyLong(), any(), anyInt(), anyInt());
    }

    // метод findAllForOwner
    @Test
    void findAllForOwner_ok() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        when(service.findAllForOwner(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk());

        verify(service).findAllForOwner(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void findAllForOwner_whenNotValid_State_thanReturnInternalServerError() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;

        when(service.findAllForOwner(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "sdf")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isInternalServerError());

        verify(service, never()).findAllForOwner(anyLong(), any(), anyInt(), anyInt());
    }
}