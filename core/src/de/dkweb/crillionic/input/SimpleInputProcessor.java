/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Dirk Weber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.dkweb.crillionic.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.dkweb.crillionic.model.GameObject;

public class SimpleInputProcessor implements InputProcessor {
    private Camera camera;
    private GameObject player;
    private Integer startDragX;
    private Integer startDragY;

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
/*        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        if (worldCoords.x < player.getPosition().x) {
            float factor = getNormalizedXDistance(player, screenX, 0);
            player.moveLeft(factor * 200f);
        } else {
            float factor = getNormalizedXDistance(player, screenX, Gdx.graphics.getWidth());
            player.moveRight(factor * 200f);
        }*/
        return false;
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
        if (startDragX != null || startDragY != null) {
            Vector2 dragVector = new Vector2(screenX - startDragX, screenY - startDragY);
            // We have to switch signs for the y axis because the screen has an y-down
            // and the world an y-up coordinate system
            player.move(dragVector.x, -1 * dragVector.y);
            startDragX = null;
            startDragY = null;
        }
        return false;
    }

    private int getMaximumVectorLength() {
        return Gdx.graphics.getWidth() / 5;
    }

    private float getNormalizedDistance(int screenDistance) {
        float maxScreenDistance = Gdx.graphics.getWidth() / 4f;
        if (screenDistance > maxScreenDistance) {
            return 1.0f;
        }
        return screenDistance / maxScreenDistance;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (startDragX == null) {
            startDragX = screenX;
        }
        if (startDragY == null) {
            startDragY = screenY;
        }
        return true;
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
