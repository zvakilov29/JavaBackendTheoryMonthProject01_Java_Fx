package rpg.items;

import java.util.EnumMap;
import java.util.Map;

public class Inventory {
    private final Map<ItemType, Integer> items = new EnumMap<>(ItemType.class);

    public int getCount(ItemType type) {
        return items.getOrDefault(type, 0);
    }

    public void add(ItemType type, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");
        items.put(type, getCount(type) + amount);
    }

    /**
     * Removes amount if possible. Returns true if removed, false if not enough items.
     */
    public boolean remove(ItemType type, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be > 0");

        int current = getCount(type);
        if (current < amount) return false;

        int remaining = current - amount;
        if (remaining == 0) items.remove(type);
        else items.put(type, remaining);

        return true;
    }
}
