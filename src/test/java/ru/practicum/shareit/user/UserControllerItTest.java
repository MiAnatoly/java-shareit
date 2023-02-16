package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.servece.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerItTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    //метод findAll
    @Test
    void findAll_ok() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(service).findAll();
    }

    //метод findById
    @Test
    void findById_ok() throws Exception {
        UserRefundDto userRefundDto = new UserRefundDto(
                1L,
                "34@mail.ru",
                "Bob"
        );

        when(service.findById(anyLong())).thenReturn(userRefundDto);

        mockMvc.perform(get("/users/{userId}", anyLong()))
                .andExpect(status().isOk());

        verify(service).findById(anyLong());
    }

    //метод edit
    @Test
    void edit_ok() throws Exception {
        UserRefundDto userRefundDto = new UserRefundDto(1L, "34@mail.ru", "Bob");
        UserDto userDto = new UserDto("34@mail.ru", "Bob");

        when(service.edit(anyLong(), any())).thenReturn(userRefundDto);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("34@mail.ru"))
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void edit_whenUserNotValidEmail_thanReturnBadRequest() throws Exception {
        UserRefundDto userRefundDto = new UserRefundDto(1L, "34@mail.ru", "Bob");
        UserDto userDto = new UserDto("34ail.ru", "Bob");

        when(service.edit(anyLong(), any())).thenReturn(userRefundDto);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(service, never()).edit(anyLong(), any());
    }

    //метод add
    @Test
    void add_ok() throws Exception {
        UserRefundDto userRefundDto = new UserRefundDto(1L, "34@mail.ru", "Bob");
        UserDto userDto = new UserDto("34@mail.ru", "Bob");

        when(service.save(any())).thenReturn(userRefundDto);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("34@mail.ru"))
                .andExpect(jsonPath("$.name").value("Bob"));
    }

    @Test
    void add_whenUserNotValidEmail_thanReturnBadRequest() throws Exception {
        UserRefundDto userRefundDto = new UserRefundDto(1L, "34@mail.ru", "Bob");
        UserDto userDto = new UserDto("", "Bob");

        when(service.save(any())).thenReturn(userRefundDto);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    @Test
    void add_whenUserNotValidUser_thanReturnBadRequest() throws Exception {
        UserRefundDto userRefundDto = new UserRefundDto(1L, "34@mail.ru", "Bob");
        UserDto userDto = new UserDto("34@mail.ru", "");

        when(service.save(any())).thenReturn(userRefundDto);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    //метод delete
    @Test
    void delete_ok() throws Exception {
        mockMvc.perform(delete("/users/{userId}", anyLong()))
                .andExpect(status().isOk());

        verify(service).delete(anyLong());
    }
}