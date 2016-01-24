package de.dkweb.crillionic.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import de.dkweb.crillionic.model.GameObject;

/**
 * Created by dirkweber
 */
public class SimpleInputProcessor implements InputProcessor {
    private Camera camera;
    private GameObject player;

    public SimpleInputProcessor(Camera camera, GameObject player) {
        this.camera = camera;
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            player.moveLeft(200f);
            return true;
        }
        if (keycode == Input.Keys.RIGHT) {
            player.moveRight(200f);
            return true;
        }
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
