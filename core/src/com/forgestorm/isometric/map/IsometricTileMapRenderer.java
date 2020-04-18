package com.forgestorm.isometric.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.util.IsometricUtil;

import lombok.Getter;

import static com.badlogic.gdx.graphics.g2d.Batch.C1;
import static com.badlogic.gdx.graphics.g2d.Batch.C2;
import static com.badlogic.gdx.graphics.g2d.Batch.C3;
import static com.badlogic.gdx.graphics.g2d.Batch.C4;
import static com.badlogic.gdx.graphics.g2d.Batch.U1;
import static com.badlogic.gdx.graphics.g2d.Batch.U2;
import static com.badlogic.gdx.graphics.g2d.Batch.U3;
import static com.badlogic.gdx.graphics.g2d.Batch.U4;
import static com.badlogic.gdx.graphics.g2d.Batch.V1;
import static com.badlogic.gdx.graphics.g2d.Batch.V2;
import static com.badlogic.gdx.graphics.g2d.Batch.V3;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;
import static com.badlogic.gdx.graphics.g2d.Batch.X1;
import static com.badlogic.gdx.graphics.g2d.Batch.X2;
import static com.badlogic.gdx.graphics.g2d.Batch.X3;
import static com.badlogic.gdx.graphics.g2d.Batch.X4;
import static com.badlogic.gdx.graphics.g2d.Batch.Y1;
import static com.badlogic.gdx.graphics.g2d.Batch.Y2;
import static com.badlogic.gdx.graphics.g2d.Batch.Y3;
import static com.badlogic.gdx.graphics.g2d.Batch.Y4;

@Getter
public class IsometricTileMapRenderer extends BatchTiledMapRenderer {

    private final IsometricTest isometricTest;

    private int startY = 0;
    private int endY = 0;
    private int startX = 0;
    private int endX = 0;
    private int tilesRendered = 0;

    public IsometricTileMapRenderer(IsometricTest isometricTest, TiledMap map, Batch batch) {
        super(map, batch);
        this.isometricTest = isometricTest;
    }

    @Override
    public void renderTileLayer(TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        final float layerOffsetX = layer.getRenderOffsetX() * unitScale;
        // offset in tiled is y down, so we flip it
        final float layerOffsetY = -layer.getRenderOffsetY() * unitScale;

        int mapRotation = isometricTest.getMapRotation();

        Vector2 centerCell = IsometricUtil.screenToCell(
                isometricTest.getCamera(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,
                isometricTest.getTileWidthHalf(), isometricTest.getTileHeightHalf(), isometricTest.getMapRotation());

        int cellRenderDistance = (int) (31 * isometricTest.getCamera().zoom);
        startX = (int) (centerCell.x - cellRenderDistance);
        startY = (int) (centerCell.y - cellRenderDistance);
        endX = (int) (centerCell.x + cellRenderDistance);
        endY = (int) (centerCell.y + cellRenderDistance);

        // Reset before each render
        tilesRendered = 0;

        // Draw only visible columns and rows
        if (mapRotation == 0 | mapRotation == 1 | mapRotation == 3) {
            for (int y = endY; y >= startY; y--) {
                for (int x = startX; x <= endX; x++) {
                    Vector2 tempVector = IsometricUtil.isometricProjection(x, y, isometricTest.getTileWidthHalf(), isometricTest.getTileHeightHalf(), mapRotation);
                    boolean tileWasRendered = renderRotation(layer, color, x, y, tempVector.x, tempVector.y, layerOffsetX, layerOffsetY);
                    if (tileWasRendered) tilesRendered++;
                }
            }
        } else if (mapRotation == 2) {
            // Mirror map rotation 0 for drawing tile order
            for (int y = startY; y <= endY; y++) {
                for (int x = endX; x >= startX; x--) {
                    Vector2 tempVector = IsometricUtil.isometricProjection(x, y, isometricTest.getTileWidthHalf(), isometricTest.getTileHeightHalf(), mapRotation);
                    boolean tileWasRendered = renderRotation(layer, color, x, y, tempVector.x, tempVector.y, layerOffsetX, layerOffsetY);
                    if (tileWasRendered) tilesRendered++;
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private boolean renderRotation(TiledMapTileLayer layer, float color, int col, int row, float x, float y, float layerOffsetX, float layerOffsetY) {
        final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
        if (cell == null) return false;

        final TiledMapTile tile = cell.getTile();

        int bufferX = isometricTest.getTileWidth();
        int bufferY = isometricTest.getTileWidth();

        // Cull out extra tiles from the view bounds
        if (x < viewBounds.x - bufferX) return false;
        if (x > viewBounds.x + viewBounds.width) return false;
        if (y < viewBounds.y - bufferY) return false;
        if (y > viewBounds.y + viewBounds.height) return false;

        if (tile != null) {
            final boolean flipX = cell.getFlipHorizontally();
            final boolean flipY = cell.getFlipVertically();
            final int rotations = cell.getRotation();

            TextureRegion region = tile.getTextureRegion();

            float x1 = x + tile.getOffsetX() * unitScale + layerOffsetX;
            float y1 = y + tile.getOffsetY() * unitScale + layerOffsetY;
            float x2 = x1 + region.getRegionWidth() * unitScale;
            float y2 = y1 + region.getRegionHeight() * unitScale;

            float u1 = region.getU();
            float v1 = region.getV2();
            float u2 = region.getU2();
            float v2 = region.getV();

            vertices[X1] = x1;
            vertices[Y1] = y1;
            vertices[C1] = color;
            vertices[U1] = u1;
            vertices[V1] = v1;

            vertices[X2] = x1;
            vertices[Y2] = y2;
            vertices[C2] = color;
            vertices[U2] = u1;
            vertices[V2] = v2;

            vertices[X3] = x2;
            vertices[Y3] = y2;
            vertices[C3] = color;
            vertices[U3] = u2;
            vertices[V3] = v2;

            vertices[X4] = x2;
            vertices[Y4] = y1;
            vertices[C4] = color;
            vertices[U4] = u2;
            vertices[V4] = v1;

            if (flipX) {
                float temp = vertices[U1];
                vertices[U1] = vertices[U3];
                vertices[U3] = temp;
                temp = vertices[U2];
                vertices[U2] = vertices[U4];
                vertices[U4] = temp;
            }
            if (flipY) {
                float temp = vertices[V1];
                vertices[V1] = vertices[V3];
                vertices[V3] = temp;
                temp = vertices[V2];
                vertices[V2] = vertices[V4];
                vertices[V4] = temp;
            }
            if (rotations != 0) {
                switch (rotations) {
                    case TiledMapTileLayer.Cell.ROTATE_90: {
                        float tempV = vertices[V1];
                        vertices[V1] = vertices[V2];
                        vertices[V2] = vertices[V3];
                        vertices[V3] = vertices[V4];
                        vertices[V4] = tempV;

                        float tempU = vertices[U1];
                        vertices[U1] = vertices[U2];
                        vertices[U2] = vertices[U3];
                        vertices[U3] = vertices[U4];
                        vertices[U4] = tempU;
                        break;
                    }
                    case TiledMapTileLayer.Cell.ROTATE_180: {
                        float tempU = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = tempU;
                        tempU = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = tempU;
                        float tempV = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = tempV;
                        tempV = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = tempV;
                        break;
                    }
                    case TiledMapTileLayer.Cell.ROTATE_270: {
                        float tempV = vertices[V1];
                        vertices[V1] = vertices[V4];
                        vertices[V4] = vertices[V3];
                        vertices[V3] = vertices[V2];
                        vertices[V2] = tempV;

                        float tempU = vertices[U1];
                        vertices[U1] = vertices[U4];
                        vertices[U4] = vertices[U3];
                        vertices[U3] = vertices[U2];
                        vertices[U2] = tempU;
                        break;
                    }
                }
            }
            batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
        }
        return true;
    }
}
