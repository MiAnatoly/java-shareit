package ru.practicum.shareit.user.servece;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.exception.NotObjectException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRefundDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
     private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAll_ReturnAllUsers() {
        User user = new User(1L, "34@mail.ru", "Maksim");
        List<User> users = new ArrayList<>();
        users.add(user);
        Pageable pageable = PageRequest.ofSize(10);
        PageImpl<User> page = new PageImpl<>(users, pageable, 10);
        when(repository.findAll(PageRequest.ofSize(10))).thenReturn(page);

        List<UserRefundDto> usersRefundDto = userService.findAll();

        assertEquals(usersRefundDto.size(), 1);
        verify(repository).findAll(PageRequest.ofSize(10));
    }

    //------------------------------------------------------------
    @Test
    void findById_whenUserFound_thenReturnUser() {
        long id = 1L;
        Optional<User> user = Optional.of(new User(1L, "34@mail.ru", "Maksim"));
        when(repository.findById(id)).thenReturn(user);

        UserRefundDto usersDto = userService.findById(id);

        assertEquals(usersDto.getId(), 1);
        assertEquals(usersDto.getEmail(), "34@mail.ru");
        assertEquals(usersDto.getName(), "Maksim");
    }

    @Test
    void findById_whenUserNotFound_thenReturnNotObjectException() {
        long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> userService.findById(id),
                "нет пользователя");
    }

    //-------------------------------------------------------------
    @Test
    void save_thenReturnUser() {
        long id = 1L;
        UserDto userDto = new UserDto("34@mail.ru", "Maksim");
        User user = new User(id, "34@mail.ru", "Maksim");
        when(repository.save(Mockito.any())).thenReturn(user);

        UserRefundDto userRefundDto = userService.save(userDto);
        assertEquals(userRefundDto.getId(), 1);
        assertEquals(userRefundDto.getEmail(), "34@mail.ru");
        assertEquals(userRefundDto.getName(), "Maksim");
    }

    //----------------------------------------------------------------
    @Test
    void edit_allDataUser_thenReturnUser() {
        long id = 1L;
        UserDto userDto = new UserDto("36@mail.ru", "Maksim2");
        User user = new User(id, "34@mail.ru", "Maksim");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserRefundDto usersDto = userService.edit(id, userDto);

        assertEquals(usersDto.getId(), 1);
        assertEquals(usersDto.getEmail(), "36@mail.ru");
        assertEquals(usersDto.getName(), "Maksim2");
    }

    @Test
    void edit_mailUser_thenReturnUser() {
        long id = 1L;
        UserDto userDto = new UserDto("36@mail.ru", null);
        User user = new User(id, "34@mail.ru", "Maksim");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserRefundDto usersDto = userService.edit(id, userDto);

        assertEquals(usersDto.getId(), 1);
        assertEquals(usersDto.getEmail(), "36@mail.ru");
        assertEquals(usersDto.getName(), "Maksim");
    }

    @Test
    void edit_nameUser_thenReturnUser() {
        long id = 1L;
        UserDto userDto = new UserDto(null, "Maksim2");
        User user = new User(id, "34@mail.ru", "Maksim");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserRefundDto usersDto = userService.edit(id, userDto);

        assertEquals(usersDto.getId(), 1);
        assertEquals(usersDto.getEmail(), "34@mail.ru");
        assertEquals(usersDto.getName(), "Maksim2");
    }

    @Test
    void edit_nameUser_thenNotObjectException() {
        long id = 1L;
        UserDto userDto = new UserDto(null, "Maksim2");
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotObjectException.class, () -> userService.edit(id, userDto),
                "нет пользователя");
    }

    //--------------------------------------------------------------
    @Test
    void delete_NotReturn() {
        Long id = 1L;

        userService.delete(id);

        verify(repository).deleteById(id);
    }
}