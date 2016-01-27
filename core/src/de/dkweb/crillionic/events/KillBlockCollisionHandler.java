package de.dkweb.crillionic.events;

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
public class KillBlockCollisionHandler implements GameObjectCollisionHandler {
    @Override
    public boolean onCollision(List<GameObject> removals,
                            GameObject thisObject,
                            GameObject collidedWith,
                            GameStatistics gameStatistics) {
        if (GlobalConstants.PLAYER_ID.equals(collidedWith.getId())) {
            gameStatistics.decreaseLifes();
            removals.add(collidedWith);
            return true;
        }
        return false;
    }
}
