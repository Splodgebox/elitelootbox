package net.splodgebox.elitelootbox.utils;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    public static List<Integer> calculateBorderSlots(int rows, int columns) {
        List<Integer> borderSlots = new ArrayList<>();
        for (int col = 0; col < columns; col++) {
            borderSlots.add(col);
        }

        for (int col = (rows - 1) * columns; col < rows * columns; col++) {
            borderSlots.add(col);
        }

        for (int row = 1; row < rows - 1; row++) {
            borderSlots.add(row * columns);
        }

        for (int row = 1; row < rows - 1; row++) {
            borderSlots.add((row * columns) + columns - 1);
        }

        return borderSlots;
    }

}
