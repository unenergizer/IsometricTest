package com.forgestorm.isometric.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IsometricUtil {

    public static Vector2 screenToMap(float col, float row, int mapWidth, int mapHeight, int tileWidthHalf, int tileHeightHalf, int rotation, boolean stayInGrid) {
        // Test for out of bounds
        if (stayInGrid) {
            Vector2 outOfBoundsCorrection = outOfBoundsCorrection(col, row, mapWidth, mapHeight);
            col = outOfBoundsCorrection.x;
            row = outOfBoundsCorrection.y;
        }

        // Adjust for map rotation
        return isometricProjection(col, row, tileWidthHalf, tileHeightHalf, rotation);
    }

    public static Vector2 isometricProjection(float col, float row, int tileWidthHalf, int tileHeightHalf, int rotation) {
        Vector2 tempVector = new Vector2();
        if (rotation == 0) {
            // Origin Left
            tempVector.x = (col + row) * tileWidthHalf;
            tempVector.y = (row - col) * tileHeightHalf;
        } else if (rotation == 1) {
            // Origin Bottom
            tempVector.x = (col - row) * tileWidthHalf;
            tempVector.y = (row + col) * tileHeightHalf;
        } else if (rotation == 2) {
            // Origin Right
            tempVector.x = (col + row) * -tileWidthHalf;
            tempVector.y = (row - col) * -tileHeightHalf;
        } else if (rotation == 3) {
            // Origin Top
            tempVector.x = (col - row) * -tileWidthHalf;
            tempVector.y = (row + col) * -tileHeightHalf;
        }
        return tempVector;
    }

    private static Vector2 outOfBoundsCorrection(float x, float y, int mapWidth, int mapHeight) {
        Vector2 tempVector = new Vector2();
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

    private static Vector2 worldToCell(float x, float y, int tileWidthHalf, int tileHeightHalf, int rotation) {
        float col = 0, row = 0;
        if (rotation == 0) {
            // Origin Left
            col = (x / tileWidthHalf - y / tileHeightHalf) * .5f;
            row = (x / tileWidthHalf + y / tileHeightHalf) * .5f;
        } else if (rotation == 1) {
            // Origin Bottom
            col = (y / tileHeightHalf + x / tileWidthHalf) * .5f;
            row = (y / tileHeightHalf - x / tileWidthHalf) * .5f;
            row += 1;
        } else if (rotation == 2) {
            // Origin Right
            col = -(x / tileWidthHalf - y / tileHeightHalf) * .5f;
            row = -(x / tileWidthHalf + y / tileHeightHalf) * .5f;
            col += 1;
            row += 1;
        } else if (rotation == 3) {
            // Origin Top
            col = -(y / tileHeightHalf + x / tileWidthHalf) * .5f;
            row = -(y / tileHeightHalf - x / tileWidthHalf) * .5f;
            col += 1;
        }
        return new Vector2((int) (col), (int) (row));
    }

    public static Vector2 screenToCell(Camera camera, float x, float y, int tileWidthHalf, int tileHeightHalf, int rotation) {
        Vector3 cursorLocation = new Vector3(x, y, 0);
        camera.unproject(cursorLocation);
        cursorLocation.y -= tileHeightHalf;
        return worldToCell(cursorLocation.x, cursorLocation.y, tileWidthHalf, tileHeightHalf, rotation);
    }
}
