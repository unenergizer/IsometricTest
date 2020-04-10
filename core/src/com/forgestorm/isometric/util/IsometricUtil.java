package com.forgestorm.isometric.util;

import com.badlogic.gdx.math.Vector2;
import com.forgestorm.isometric.IsometricTest;

public class IsometricUtil {


    private static Vector2 tempVector = new Vector2();

    public static Vector2 screenToMap(IsometricTest isometricTest, float x, float y, boolean stayInGrid) {
        // Test for out of bounds
        if (stayInGrid) {
            Vector2 outOfBoundsCorrection = outOfBoundsCorrection(isometricTest, x, y);
            x = outOfBoundsCorrection.x;
            y = outOfBoundsCorrection.y;
        }

        // Conversion happens here
        tempVector.x = (x + y) * isometricTest.getTileWidthHalf();
        tempVector.y = (-x + y) * isometricTest.getTileHeightHalf();
        return tempVector;
    }

    public static Vector2 outOfBoundsCorrection(IsometricTest isometricTest, float x, float y) {
        // Text and fix out of bounds
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > isometricTest.getMapWidth() - 1) x = isometricTest.getMapWidth() - 1;
        if (y > isometricTest.getMapHeight() - 1) y = isometricTest.getMapHeight() - 1;
        tempVector.x = x;
        tempVector.y = y;
        return tempVector;
    }

    public static boolean isOutOfBounds(IsometricTest isometricTest, float x, float y) {
        return x < 0
                || y < 0
                || x > isometricTest.getMapWidth() - 1
                || y > isometricTest.getMapHeight() - 1;
    }
}
