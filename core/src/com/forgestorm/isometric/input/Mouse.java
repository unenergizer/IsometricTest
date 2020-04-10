package com.forgestorm.isometric.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.util.IsometricUtil;
import com.forgestorm.isometric.util.ScreenResolutions;

import java.util.List;

import lombok.Getter;

@Getter
public class Mouse implements InputProcessor {

    private static final float ZOOM_MIN = .25f;
    private static final float ZOOM_MAX = 1f;

    private final IsometricTest isometricTest;
    private final OrthographicCamera camera;
    private float lastZoom;

    private int mouseX;
    private int mouseY;

    private Matrix4 isoTransform;
    private Matrix4 invIsoTransform;
    private TiledMapTile tileClicked;
    private Vector2 cellClicked;
    private Vector2 cellHovered;

    public Mouse(IsometricTest isometricTest) {
        this.isometricTest = isometricTest;
        this.camera = isometricTest.getCamera();
        this.lastZoom = camera.zoom;

        //Create the isometric transform
        // https://stackoverflow.com/questions/59357975/how-to-get-tile-position-on-touching-it-in-isometric-libgdx
        isoTransform = new Matrix4();
        isoTransform.idt();
        isoTransform.translate(0.0f, 0.25f, 0.0f);
        isoTransform.scale((float) (Math.sqrt(2.0) / 2.0), (float) (Math.sqrt(2.0) / 4.0), 1.0f);
        isoTransform.rotate(0.0f, 0.0f, 1.0f, -45.0f);

        //... and the inverse matrix
        invIsoTransform = new Matrix4(isoTransform);
        invIsoTransform.inv();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("Button: " + button);

        cellClicked = screenToCell(screenX, screenY);

        if (!IsometricUtil.isOutOfBounds(cellClicked.x, cellClicked.y, isometricTest.getMapWidth(), isometricTest.getMapHeight(), isometricTest.getMapRotation())) {
            wallPlace(cellClicked.x, cellClicked.y, button);

            //Get the tile and the cell
            TiledMapTileLayer layer = (TiledMapTileLayer) isometricTest.getIsoMap().getLayers().get(0);
            TiledMapTileLayer.Cell tileCell = layer.getCell((int) cellClicked.x, (int) cellClicked.y);
            // TODO: tile cell can be null
            if (tileCell.getTile() != null) tileClicked = tileCell.getTile();

            //Flip the tile just so you have a visual to make sure your selected the right tile
            tileCell.setFlipHorizontally(!tileCell.getFlipHorizontally());
        }
        return false;
    }

    private void wallPlace(float x, float y, int button) {
        List<Vector2> wallList = isometricTest.getWallList();

        if (button == 0) {
            // Place wall
            wallList.add(new Vector2(x, y));
        } else if (button == 1) {
            // Remove Wall
            wallList.remove(new Vector2(x, y));
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
        cellHovered = screenToCell(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        float change = lastZoom + amount * .25f;

        if (change <= ZOOM_MIN) {
            change = ZOOM_MIN;
        } else if (change >= ZOOM_MAX) {
            change = ZOOM_MAX;
        }

        lastZoom = change;
        camera.zoom = lastZoom;
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

    private Vector2 worldToCell(float x, float y) {
        int rotation = isometricTest.getMapRotation();

        float col = 0, row = 0;

        if (rotation == 0) {
            col = (x / isometricTest.getTileWidthHalf() - y / isometricTest.getTileHeightHalf()) * .5f;
            row = (x / isometricTest.getTileWidthHalf() + y / isometricTest.getTileHeightHalf()) * .5f;
        } else if (rotation == 1) {
            col = (y / isometricTest.getTileHeightHalf() + x / isometricTest.getTileWidthHalf()) * .5f;
            row = (y / isometricTest.getTileHeightHalf() - x / isometricTest.getTileWidthHalf()) * .5f;
            row += 1;
        } else if (rotation == 2) {
            col = -(x / isometricTest.getTileWidthHalf() - y / isometricTest.getTileHeightHalf()) * .5f;
            row = -(x / isometricTest.getTileWidthHalf() + y / isometricTest.getTileHeightHalf()) * .5f;
            col += 1;
            row += 1;
        } else if (rotation == 3) {
            col = -(y / isometricTest.getTileHeightHalf() + x / isometricTest.getTileWidthHalf()) * .5f;
            row = -(y / isometricTest.getTileHeightHalf() - x / isometricTest.getTileWidthHalf()) * .5f;
            col += 1;
        }

        return new Vector2((int) (col), (int) (row));
    }

    private Vector2 screenToWorld(float x, float y) {
        Vector3 touch = new Vector3(x, y, 0);
        camera.unproject(touch);
//        touch.mul(invIsoTransform); // Joseph said this is not necessary.
//        touch.mul(isoTransform);
        return new Vector2(touch.x, touch.y);
    }


    private Vector2 screenToCell(float x, float y) {
        Vector2 world = screenToWorld(x, y);
        world.y -= isometricTest.getTileHeightHalf();
        return worldToCell(world.x, world.y);
    }
}
