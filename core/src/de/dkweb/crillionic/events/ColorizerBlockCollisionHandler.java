package de.dkweb.crillionic.events;

import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.List;

/**
 * Created by dirkweber
 *
 * Handles the collision of a "colorizer" block.
 * Whenever the player touches such a block, he will be colorized with the color of the block.
 * Afterwards he is able to destroy blocks of the very same color.
 *
 */
public class ColorizerBlockCollisionHandler implements GameObjectCollisionHandler {
    @Override
    public boolean onCollision(List<GameObject> removals,
                            GameObject thisObject,
                            GameObject collidedWith,
                            GameStatistics gameStatistics) {
        if (GlobalConstants.PLAYER_ID.equals(collidedWith.getId())) {
            collidedWith.changeColor(thisObject.getColor());
        }
        return false;
    }
}
