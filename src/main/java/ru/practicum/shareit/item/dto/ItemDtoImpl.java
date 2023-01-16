package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ItemDtoImpl implements ItemDto {
    private long id;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Long>> itemsId = new HashMap<>();

    @Override
    public Item add(long userId, Item item) {
        ++id;
        item.setId(id);
        item.setUserId(userId);
        items.put(id, item);
        List<Long> list;
        if (!itemsId.containsKey(userId))
            list = new ArrayList<>();
        else
            list = itemsId.get(userId);
        list.add(id);
        itemsId.put(userId, list);
        return item;
    }

    @Override
    public Item edit(long userId, long itemId, Item item) {
        Item item1 = items.get(itemId);
        if (item1.getUserId() == userId) {
            if (item.getName() != null)
                item1.setName(item.getName());
            if (item.getDescription() != null)
                item1.setDescription(item.getDescription());
            if (item.getAvailable() != null)
                item1.setAvailable(item.getAvailable());
            return item1;
        } else {
            throw new NotOwnerException("Вы не явдяетесь владельцем записи");
        }
    }

    @Override
    public Item findById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findAll(long userId) {
        if (itemsId.containsKey(userId)) {
            List<Long> list = itemsId.get(userId);
            return list.stream().map(items::get).collect(Collectors.toList());
        } else
            return new ArrayList<>();
    }

    @Override
    public List<Item> search(String text) {
        if(text.isBlank())
            return new ArrayList<>();
        return Stream.concat(items.values().stream()
                                .filter(Item::getAvailable)
                                .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase())),
                        items.values().stream()
                                .filter(Item::getAvailable)
                                .filter(x -> x.getDescription().toLowerCase().contains(text.toLowerCase())))
                .distinct()
                .collect(Collectors.toList());
    }
}

