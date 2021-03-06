package com.forgestorm.isometric.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.map.TileObject;
import com.forgestorm.isometric.util.IsometricUtil;
import com.forgestorm.isometric.util.ScreenResolutions;
import com.forgestorm.isometric.util.BetterCameraZoom;

import java.util.List;

import lombok.Getter;

@Getter
public class Mouse implements InputProcessor {

    private final IsometricTest isometricTest;
    private final OrthographicCamera camera;
    private float lastZoom;

    private int mouseX;
    private int mouseY;

    private TiledMapTile tileClicked;
    private Vector2 cellClicked = new Vector2(0, 0);
    private Vector2 cellHovered = new Vector2(0, 0);

    public Mouse(IsometricTest isometricTest) {
        this.isometricTest = isometricTest;
        this.camera = isometricTest.getCamera();
        this.lastZoom = camera.zoom;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cellClicked = IsometricUtil.screenToCell(camera, screenX, screenY, isometricTest.getTileWidthHalf(), isometricTest.getTileHeightHalf(), isometricTest.getMapRotation());

        // Center camera on Tile Click location
        Vector2 tempVector = IsometricUtil.isometricProjection(cellClicked.x, cellClicked.y, isometricTest.getTileWidthHalf(), isometricTest.getTileHeightHalf(), isometricTest.getMapRotation());
        camera.position.x = tempVector.x + isometricTest.getTileWidthHalf();
        camera.position.y = tempVector.y + isometricTest.getTileHeightHalf();

        if (!IsometricUtil.isOutOfBounds(cellClicked.x, cellClicked.y, isometricTest.getMapWidth(), isometricTest.getMapHeight(), isometricTest.getMapRotation())) {
            wallPlace(cellClicked.x, cellClicked.y, button);

            //Get the tile and the cell
            TiledMapTileLayer layer = (TiledMapTileLayer) isometricTest.getIsoMap().getLayers().get(0);
            TiledMapTileLayer.Cell tileCell = layer.getCell((int) cellClicked.x, (int) cellClicked.y);
            if (tileCell == null) return false;
            if (tileCell.getTile() != null) tileClicked = tileCell.getTile();

            //Flip the tile just so you have a visual to make sure your selected the right tile
            tileCell.setFlipHorizontally(!tileCell.getFlipHorizontally());
        }
        return false;
    }

    private void wallPlace(float x, float y, int button) {
        List<Texture> textureList = isometricTest.getLoadedTextures();
        List<TileObject> testObjectList = isometricTest.getObjectList();
        TileObject tileObject = new TileObject(new Vector2(x, y), 0);

        if (button == 0) {

            boolean tileObjectFound = false;
            for (TileObject loadedObject : testObjectList) {
                if (tileObject.equals(loadedObject)) {
                    System.out.println("Found matching tile");
                    int index = loadedObject.getUsedTextureIndex();
                    index++;
                    if (index == textureList.size()) {
                        index = 0;
                    }
                    loadedObject.setUsedTextureIndex(index);
                    tileObjectFound = true;
                    break;
                }
            }

            // Place wall
            if (!tileObjectFound) testObjectList.add(tileObject);
        } else if (button == 1) {
            // Remove Wall
            for (TileObject loadedObject : testObjectList) {
                if (tileObject.equals(loadedObject)) {
                    testObjectList.remove(loadedObject);
                    break;
                }
            }
        }

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = ScreenResolutions.DESKTOP_800_600.getHeight() - screenY;
        cellHovered = IsometricUtil.screenToCell(camera, screenX, screenY, isometricTest.getTileWidthHalf(), isometricTest.getTileHeightHalf(), isometricTest.getMapRotation());
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom = BetterCameraZoom.findNextZoomValue(camera.zoom, amount);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}
