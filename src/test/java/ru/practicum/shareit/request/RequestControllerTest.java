package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.InvalidValueException;
import ru.practicum.shareit.request.dto.ItemRequestRefundDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {
    @Mock
    RequestService service;
    @InjectMocks
    RequestController controller;

    @Test
    void findAllNotOwner() {
        List<ItemRequestRefundDto> requests = new ArrayList<>();
        requests.add(new ItemRequestRefundDto());
        requests.add(new ItemRequestRefundDto());
        when(service.findAllNotOwner(anyLong(), anyInt(), anyInt())).thenReturn(requests);

        List<ItemRequestRefundDto> refundDto = controller.findAllNotOwner(1L, 4, 2);

        assertEquals(refundDto.size(), 2);
    }

    @Test
    void findAllNotOwner_whenWithParamNotValidPage_thanReturnInvalidValueException() {
        assertThrows(InvalidValueException.class, () -> controller.findAllNotOwner(1L, 3, 5));

        verify(service, never()).findAllNotOwner(anyLong(), anyInt(), anyInt());
    }
}