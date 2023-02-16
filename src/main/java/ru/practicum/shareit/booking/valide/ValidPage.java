package ru.practicum.shareit.booking.valide;

public class ValidPage {
    public static Integer page(Integer from, Integer size) {
        if (from % size == 0) {
            return from / size;
        } else {
            throw new ArithmeticException("передан не первый элемент страницы");
        }
    }
}
