package net.splodgebox.elitelootbox.utils;


import java.util.*;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random = new Random();
    private double totalWeight = 0;
    private final Set<E> usedItems = new HashSet<>();

    public void add(double weight, E item) {
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
        totalWeight += weight;
        map.put(totalWeight, item);
    }

    public E getRandom(boolean allowDuplicates) {
        if (!allowDuplicates && usedItems.size() == map.size()) {
            throw new IllegalStateException("No more unique items available.");
        }

        E item;
        do {
            double value = random.nextDouble() * totalWeight;
            item = map.higherEntry(value).getValue();
        } while (!allowDuplicates && usedItems.contains(item));

        if (!allowDuplicates) {
            usedItems.add(item);
        }
        return item;
    }

    public void reset() {
        usedItems.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
