package de.dkweb.crillionic.events;

import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;

import java.util.List;

/**
 * Created by dirkweber
 *
 * Implementors can define what should happen with a specific object when it is part of some collision.
 */
public interface GameObjectCollisionHandler {
    /**
     * @return true, if thisObject has been added to the removals. Otherwise false
     */
    public boolean onCollision(List<GameObject> removals, GameObject thisObject,
                              GameObject collidedWith, GameStatistics gameStatistics);
}
