package com.forgestorm.isometric.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Keyboard implements InputProcessor {

    private OrthographicCamera camera;

    public Keyboard(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W || keycode == Input.Keys.UP) camera.position.y -= 10;
        if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) camera.position.y += 10;
        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) camera.position.x += 10;
        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) camera.position.x -= 10;
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
