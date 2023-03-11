package ru.practicum.shareit.booking.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyFactory {
    private Map<State, BookingSearch> strategies;

    @Autowired
    public StrategyFactory(Set<BookingSearch> strategySet) {
        createStrategy(strategySet);
    }

    public BookingSearch findBookingSearch(State state) {
        return strategies.get(state);
    }

    private void createStrategy(Set<BookingSearch> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(x -> strategies.put(x.getState(), x));
    }
}
