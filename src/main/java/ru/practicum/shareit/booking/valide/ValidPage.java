package ru.practicum.shareit.booking.valide;

import ru.practicum.shareit.exception.InvalidValueException;

public class ValidPage {
    public static Integer page(Integer from, Integer size) {
        if (from % size == 0) {
            return from / size;
        } else {
            throw new InvalidValueException("передан не первый элемент страницы");
        }
    }
}
