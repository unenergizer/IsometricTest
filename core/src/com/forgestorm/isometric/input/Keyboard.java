package com.forgestorm.isometric.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.forgestorm.isometric.IsometricTest;

public class Keyboard implements InputProcessor {

    private IsometricTest isometricTest;
    private OrthographicCamera camera;
    private boolean userInterfaceDebug = false;

    public Keyboard(IsometricTest isometricTest, OrthographicCamera camera) {
        this.isometricTest = isometricTest;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Move Camera
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) camera.position.y += 8;
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) camera.position.y -= 8;
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) camera.position.x -= 16;
        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) camera.position.x += 16;

        // Reset Drawn Tiles
        if (keycode == Input.Keys.DEL) isometricTest.getObjectList().clear();

        // Rotate Map
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

        if (keycode == Input.Keys.F12) {
            userInterfaceDebug = !userInterfaceDebug;
            for (Actor actor : isometricTest.getStageHandler().getStage().getActors()) {
                if (actor instanceof Group) {
                    Group group = (Group) actor;
                    group.setDebug(userInterfaceDebug, true);
                }
            }
        }
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
