package com.forgestorm.isometric.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    private IsometricTest isometricTest;

    private Matrix4 invIsoTransform;
    private Vector3 screenPos = new Vector3();

    private Vector2 topRight = new Vector2();
    private Vector2 bottomLeft = new Vector2();
    private Vector2 topLeft = new Vector2();
    private Vector2 bottomRight = new Vector2();

    public IsometricTileMapRenderer(IsometricTest isometricTest, TiledMap map, Batch batch) {
        super(map, batch);
        init();
        this.isometricTest = isometricTest;
    }

    private void init() {
        // create the isometric transform
        Matrix4 isoTransform = new Matrix4();
        isoTransform.idt();

        // isoTransform.translate(0, 32, 0);
        isoTransform.scale((float) (Math.sqrt(2.0) / 2.0), (float) (Math.sqrt(2.0) / 4.0), 1.0f);
        isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);

        // ... and the inverse matrix
        invIsoTransform = new Matrix4(isoTransform);
        invIsoTransform.inv();
    }

    @Override
    public void setView(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        // Temporarily removed to calculate viewbounds regarldess of zoom level. For debugging.
        // When code is ready, uncomment this line and delete the two underneath.
//        float width = camera.viewportWidth * camera.zoom;
//        float height = camera.viewportHeight * camera.zoom;
        float width = camera.viewportWidth;
        float height = camera.viewportHeight;
        float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
        float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);
        viewBounds.set(camera.position.x - w / 2, camera.position.y - h / 2, w, h);
    }

    private Vector3 translateScreenToIso(Vector2 vec) {
        screenPos.set(vec.x, vec.y, 0);
        screenPos.mul(invIsoTransform);

        return screenPos;
    }

    @Getter
    private int startY = 0, endY = 0, startX = 0, endX = 0;

    @Override
    public void renderTileLayer(TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        float tileWidth = layer.getTileWidth() * unitScale;

        final float layerOffsetX = layer.getRenderOffsetX() * unitScale;
        // offset in tiled is y down, so we flip it
        final float layerOffsetY = -layer.getRenderOffsetY() * unitScale;

        int mapRotation = isometricTest.getMapRotation();

        // Setting up the screen points
        // COL2
        float x2 = viewBounds.x - layerOffsetX;
        float y2 = viewBounds.y + viewBounds.height - layerOffsetY;
        bottomLeft.set(x2, y2);

        // ROW1
        float x3 = viewBounds.x - layerOffsetX;
        float y3 = viewBounds.y - layerOffsetY;
        topLeft.set(x3, y3);

        // ROW2
        float x4 = viewBounds.x + viewBounds.width - layerOffsetX;
        float y4 = viewBounds.y + viewBounds.height - layerOffsetY;
        bottomRight.set(x4, y4);

        // COL1
        float x1 = viewBounds.x + viewBounds.width - layerOffsetX;
        float y1 = viewBounds.y - layerOffsetY;
        topRight.set(x1, y1);

        // Transforming screen coordinates to iso coordinates
        startX = (int) (translateScreenToIso(bottomLeft).x / tileWidth); // -2
        endX = (int) (translateScreenToIso(topRight).x / tileWidth); // +2

        startY = (int) (translateScreenToIso(topLeft).y / tileWidth); // -2
        endY = (int) (translateScreenToIso(bottomRight).y / tileWidth); // +2

        System.out.println("-[ ViewBounds | Rotation: " + mapRotation + " ]-------------------");
        System.out.println("Offset x: " + layerOffsetX + ", Offset y: " + layerOffsetY);
        System.out.println("VB x: " + viewBounds.x + ", VB y: " + viewBounds.y);
        System.out.println("VB width: " + viewBounds.width + ", VB height: " + viewBounds.height);
        System.out.println();
        System.out.println("TopLeft: " + topLeft.toString());
        System.out.println("BottomLeft: " + bottomLeft.toString());
        System.out.println("TopRight: " + topRight.toString());
        System.out.println("BottomRight: " + bottomRight.toString());
        System.out.println();
        System.out.println("StartX: " + startX + ", EndX: " + endX);
        System.out.println("StartY: " + startY + ", EndY: " + endY);

        // Draw only visible columns and rows
        int tilesRendered = 0;
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
        System.out.println();
        System.out.println("TilesRendered: " + tilesRendered);
        System.out.println("----------------------------------------------------------");
    }

    @SuppressWarnings("ConstantConditions")
    private boolean renderRotation(TiledMapTileLayer layer, float color, int col, int row, float x, float y, float layerOffsetX, float layerOffsetY) {
        final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
        if (cell == null) return false;

        final TiledMapTile tile = cell.getTile();

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
