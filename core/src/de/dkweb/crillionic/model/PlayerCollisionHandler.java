package de.dkweb.crillionic.model;

import de.dkweb.crillionic.events.GameObjectCollisionHandler;

import java.util.List;

/**
 * Created by dirkweber
 */
public class PlayerCollisionHandler implements GameObjectCollisionHandler {
    @Override
    public boolean onCollision(List<GameObject> removals, GameObject thisObject, GameObject collidedWith, GameStatistics gameStatistics) {
        thisObject.setDirectionLocked(false);
        return false;
    }
}
