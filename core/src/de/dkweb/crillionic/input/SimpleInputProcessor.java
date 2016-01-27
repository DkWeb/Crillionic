package de.dkweb.crillionic.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        if (worldCoords.x < player.getPosition().x) {
            float factor = getNormalizedXDistance(player, screenX, 0);
            player.moveLeft(factor * 200f);
        } else {
            float factor = getNormalizedXDistance(player, screenX, Gdx.graphics.getWidth());
            player.moveRight(factor * 200f);
        }
        return true;
    }

    private Vector2 getScreenPosition(Vector2 worldPosition) {
        Vector3 screenPosition = camera.project(new Vector3(worldPosition.x, worldPosition.y, 0));
        return new Vector2(screenPosition.x, screenPosition.y);
    }

    private float getNormalizedXDistance(GameObject player, int screenX, int screenBorderX) {
        // First we need to calculate the distance between the player and the border
        // -> this is the maximum distance the user can provide by clicking on the screen
        Vector2 screenPositionPlayer = getScreenPosition(player.getPosition());
        float maxDistanceScreen = Math.abs(screenBorderX - screenPositionPlayer.x);
        float actDistanceScreen = Math.abs(screenX - screenPositionPlayer.x);
        return actDistanceScreen / maxDistanceScreen;
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
