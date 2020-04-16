package com.forgestorm.isometric;

import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

public class TileComparator implements Comparator<TileObject> {

    private final IsometricTest isometricTest;

    TileComparator(IsometricTest isometricTest) {
        this.isometricTest = isometricTest;
    }

    @Override
    public int compare(TileObject tileObject21, TileObject tileObject2) {
        Vector2 xy1 = tileObject21.getTileCords();
        Vector2 xy2 = tileObject2.getTileCords();

        float x1 = xy1.x;
        float y1 = xy1.y;
        float x2 = xy2.x;
        float y2 = xy2.y;
        if (isometricTest.getMapRotation() == 0) {
            // Moving objects or objects placed within a tile
//            if ((y2 - x2) > (y1 - x1)) {
//                return 1;
//            }
//            return -1;
            return (int) ((y2 - x2) - (y1 - x1));
        } else if (isometricTest.getMapRotation() == 1) {
            return (int) ((y2 + x2) - (y1 + x1));
        } else if (isometricTest.getMapRotation() == 2) {
            return (int) ((x2 - y2) - (x1 - y1));
        } else {
            return (int) ((-x2 - y2) - (-x1 - y1));
        }
    }

}
