package com.forgestorm.isometric.util;

import com.badlogic.gdx.math.Vector2;
import com.forgestorm.isometric.IsometricTest;

public class IsometricUtil {


    private static Vector2 tempVector = new Vector2();

    public static Vector2 screenToMap(float x, float y, int mapWidth, int mapHeight, int tileWidthHalf, int tileHeightHalf, int rotation, boolean stayInGrid) {
        // Test for out of bounds
        if (stayInGrid) {
            Vector2 outOfBoundsCorrection = outOfBoundsCorrection(x, y, mapWidth, mapHeight, rotation);
            x = outOfBoundsCorrection.x;
            y = outOfBoundsCorrection.y;
        }

        // Conversion happens here
        tempVector.x = (x + y) * tileWidthHalf;
        tempVector.y = (-x + y) * tileHeightHalf;
        return tempVector;
    }

    public static Vector2 outOfBoundsCorrection(float x, float y, int mapWidth, int mapHeight, int rotation) {
        // Text and fix out of bounds
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > mapWidth - 1) x = mapWidth - 1;
        if (y > mapHeight - 1) y = mapHeight - 1;
        tempVector.x = x;
        tempVector.y = y;
        return tempVector;
    }

    public static boolean isOutOfBounds(float x, float y, int mapWidth, int mapHeight, int rotation) {
        boolean outOfBounds = false;
        if (rotation == 0) outOfBounds = x < 0 || y < 0 || x > mapWidth - 1 || y > mapHeight - 1;
        return outOfBounds;
    }
}
