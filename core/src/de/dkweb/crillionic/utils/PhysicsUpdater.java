package de.dkweb.crillionic.utils;

import de.dkweb.crillionic.model.GameWorld;

/**
 * Created by dirkweber
 */
public class PhysicsUpdater {
    private final static float PHYSICS_UPDATE_RATE_PER_SECOND = 1/60f;
    private float elapsedRenderTime;
    private GameWorld gameWorld;

    public PhysicsUpdater(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void doPhysics(float renderTimeDelta) {
        elapsedRenderTime += renderTimeDelta;
        while (elapsedRenderTime >= PHYSICS_UPDATE_RATE_PER_SECOND) {
            gameWorld.getPhysicsWorld().step(PHYSICS_UPDATE_RATE_PER_SECOND, 6, 2);
            elapsedRenderTime -= renderTimeDelta;
        }
    }
}
