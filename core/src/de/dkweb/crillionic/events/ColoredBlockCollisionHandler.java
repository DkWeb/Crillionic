package de.dkweb.crillionic.events;

import com.badlogic.gdx.graphics.Color;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.List;

/**
 * Created by dirkweber
 *
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
