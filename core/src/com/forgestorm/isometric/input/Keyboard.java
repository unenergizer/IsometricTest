package com.forgestorm.isometric.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.forgestorm.isometric.IsometricTest;
import com.forgestorm.isometric.util.IsometricUtil;

public class Keyboard implements InputProcessor {

    private IsometricTest isometricTest;
    private OrthographicCamera camera;

    public Keyboard(IsometricTest isometricTest, OrthographicCamera camera) {
        this.isometricTest = isometricTest;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Move Camera
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) camera.position.y -= 10;
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) camera.position.y += 10;
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) camera.position.x += 10;
        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) camera.position.x -= 10;

        // Rotate Map
        int lastRotation = isometricTest.getMapRotation();
        if (keycode == Input.Keys.NUMPAD_0) {
            isometricTest.setMapRotation(0);
            isometricTest.setSortNeeded(true);
        }
        if (keycode == Input.Keys.NUMPAD_1) {
            isometricTest.setMapRotation(1);
            isometricTest.setSortNeeded(true);
        }
        if (keycode == Input.Keys.NUMPAD_2) {
            isometricTest.setMapRotation(2);
            isometricTest.setSortNeeded(true);
        }
        if (keycode == Input.Keys.NUMPAD_3) {
            isometricTest.setMapRotation(3);
            isometricTest.setSortNeeded(true);
        }
        int nextRotation = isometricTest.getMapRotation();
        if (lastRotation != nextRotation)
            IsometricUtil.cameraFix(camera, lastRotation, nextRotation);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
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
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
