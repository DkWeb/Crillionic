package de.dkweb.crillionic.events;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.model.GameWorld;

/**
 * Created by dirkweber
 *
 * Level events will be thrown by the main game loop and can be handled here.
 */
public interface LevelEventListener {
    /**
     * Called within the first render run after the level has been finished.
     * @param playerSucceeded Indicates if the player has won the level (true) or not (false
     * @param gameStatistics The current game statistics of the level. May be adapted by the implementor
     * @return boolean value, indicating if the level clock should be stopped or not
     *
     */
    public boolean levelJustFinished(boolean playerSucceeded, boolean isInHighscore, GameStatistics gameStatistics,
                                     SpriteBatch readyToDrawBatch);

    /**
     * Called within subsequent render runs after the level has been finished
     * @return boolean value, indicating if the level clock should be stopped or not
     */
    public boolean levelFinished(boolean playerSucceeded, GameStatistics gameStatistics,
                                 float SecondsSinceLevelFinished, SpriteBatch readyToDrawBatch);

    /**
     * Called each time the player loses one life. Will be called one frame BEFORE levelJustFinished or levelFinished
     */
    public boolean playerDestroyed(GameStatistics gameStatistics, final GameWorld gameWorld);
}
