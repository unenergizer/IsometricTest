package com.forgestorm.isometric.map;

import com.badlogic.gdx.math.Vector2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class TileObject {
    private final Vector2 tileCords;

    @Setter
    private int usedTextureIndex;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (getClass() != object.getClass()) return false;

        Vector2 objectTileCords = ((TileObject) object).tileCords;
        return objectTileCords.x == tileCords.x && objectTileCords.y == tileCords.y;
    }
}
