package de.dkweb.crillionic.events;

import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.List;

/**
 * Created by dirkweber
 */
public class DoNothingCollisionHandler implements GameObjectCollisionHandler {
    @Override
    public boolean onCollision(List<GameObject> removals,
                            GameObject thisObject,
                            GameObject collidedWith,
                            GameStatistics gameStatistics) {
        // Should do nothing at all
        return false;
    }
}
