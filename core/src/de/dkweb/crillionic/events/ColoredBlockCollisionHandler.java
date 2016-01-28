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
package de.dkweb.crillionic.events;

import com.badlogic.gdx.graphics.Color;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.List;

/**
 * Handles the collision of a "normal" colored block
 * If the player has the same color than the block, the block
 * vanishes and the player scores.
 * Whenever the player has the wrong color, nothing happens at all.
 *
 */
public class ColoredBlockCollisionHandler implements GameObjectCollisionHandler {
    @Override
    public boolean onCollision(List<GameObject> removals,
                            GameObject thisObject,
                            GameObject collidedWith,
                            GameStatistics gameStatistics) {
        if (GlobalConstants.PLAYER_ID.equals(collidedWith.getId())) {
            if (sameColorIgnoreAlpha(collidedWith.getColor(), thisObject.getColor())) {
                gameStatistics.increaseScore(thisObject.getType().getScore());
                gameStatistics.decreaseRemainingColorBlocks();
                removals.add(thisObject);
                return true;
            }
        }
        return false;
    }
    private boolean sameColorIgnoreAlpha(Color color1, Color color2) {
        if (color1.r != color2.r) {
            return false;
        }
        if (color1.g != color2.g) {
            return false;
        }
        if (color1.b!= color2.b) {
            return false;
        }
        return true;
    }
}
