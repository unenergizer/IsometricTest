package com.forgestorm.isometric.util;

import com.badlogic.gdx.math.Vector2;
import com.forgestorm.isometric.IsometricTest;

public class IsometricUtil {


    private static Vector2 tempVector = new Vector2();

    public static Vector2 screenToMap(float x, float y, boolean stayInGrid) {
        // Test for out of bounds
        if (stayInGrid) {
            Vector2 outOfBoundsCorrection = outOfBoundsCorrection(x, y);
            x = outOfBoundsCorrection.x;
            y = outOfBoundsCorrection.y;
        }

        // Conversion happens here
        tempVector.x = (x + y) * IsometricTest.TILE_WIDTH_HALF;
        tempVector.y = (-x + y) * IsometricTest.TILE_HEIGHT_HALF;
        return tempVector;
    }

    public static Vector2 outOfBoundsCorrection(float x, float y) {
        // Text and fix out of bounds
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > IsometricTest.MAP_SIZE - 1) x = IsometricTest.MAP_SIZE - 1;
        if (y > IsometricTest.MAP_SIZE - 1) y = IsometricTest.MAP_SIZE - 1;
        tempVector.x = x;
        tempVector.y = y;
        return tempVector;
    }

    public static boolean isOutOfBounds(float x, float y) {
        return x < 0 || y < 0 || x > IsometricTest.MAP_SIZE - 1 || y > IsometricTest.MAP_SIZE - 1;
    }
}
