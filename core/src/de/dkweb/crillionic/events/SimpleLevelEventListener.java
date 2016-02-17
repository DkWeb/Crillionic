package de.dkweb.crillionic.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import de.dkweb.crillionic.Crillionic;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.model.GameWorld;
import de.dkweb.crillionic.screens.LevelScreen;
import de.dkweb.crillionic.utils.Assets;
import de.dkweb.crillionic.utils.FileUtils;
import de.dkweb.crillionic.utils.HighscoreManager;
import de.dkweb.crillionic.utils.JsonManager;

import java.util.concurrent.Future;

/**
 * Created by dirkweber
 */
public class SimpleLevelEventListener implements LevelEventListener {
    private Crillionic game;
    private LevelScreen levelScreen;
    private Assets assets;
    private JsonManager jsonManager;
    private boolean switchToHighscoreCalled;
    private boolean isInHighscore;

    public SimpleLevelEventListener(Assets assets, JsonManager jsonManager, Crillionic game, LevelScreen levelScreen) {
        this.assets = assets;
        this.jsonManager = jsonManager;
        this.game = game;
        this.levelScreen = levelScreen;
        this.switchToHighscoreCalled = false;
    }

    @Override
    public boolean levelJustFinished(boolean playerSucceeded, boolean isInHighscore, GameStatistics gameStatistics,
                                     SpriteBatch readyToDrawBatch) {
        this.isInHighscore = isInHighscore;
        if (playerSucceeded) {
            showLevelCompleted(readyToDrawBatch);
        } else {
            showGameOver(isInHighscore, readyToDrawBatch);

        }
        return true;
    }

    @Override
    public boolean levelFinished(boolean playerSucceeded, GameStatistics gameStatistics, float
                                secondsSinceLevelFinished, SpriteBatch readyToDrawBatch) {
        if (playerSucceeded) {
            showLevelCompleted(readyToDrawBatch);
        } else {
            showGameOver(isInHighscore, readyToDrawBatch);
        }
        if (!switchToHighscoreCalled) {
            switchToHighscore(3);
        }
        return true;
    }

    @Override
    public boolean playerDestroyed(GameStatistics gameStatistics, final GameWorld gameWorld) {
        if (gameStatistics.getLifes() == 0) {
            new HighscoreManager(new FileUtils()).addEntry(gameWorld.getScoreCalculator().getLevelScore(gameStatistics), jsonManager);
        } else {
            // Spawn new player some seconds later
            respawnPlayer(gameWorld, 2);
        }
        return true;
    }

    private void switchToHighscore(long delayInSeconds) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.openHighscore();
            }
        }, delayInSeconds);
    }

    private void respawnPlayer(final GameWorld gameWorld, long delayInSeconds) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameWorld.recreatePlayer();
                levelScreen.setStopClock(false);
            }
        }, delayInSeconds);
    }

    private void showGameOver(boolean isInHighscore, SpriteBatch readyToDrawBatch) {
        // Show the game over screen
        I18NBundle bundle = assets.getBundle();
        BitmapFont font = assets.getBigBitmapFont();
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, bundle.get("game_over"), Color.RED, 200, Align.left, false);
        font.draw(readyToDrawBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                (Gdx.graphics.getHeight() / 2));

        if (isInHighscore) {
            layout = new GlyphLayout(font, bundle.get("highscore_reached"), Color.RED, 200, Align.left, false);
            font.draw(readyToDrawBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                    (Gdx.graphics.getHeight() / 2) - layout.height * 2);
        }
    }

    private void showLevelCompleted(SpriteBatch readyToDrawBatch) {
        // Show the level completed screen
        I18NBundle bundle = assets.getBundle();
        BitmapFont font = assets.getBigBitmapFont();
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, bundle.get("level_completed"), Color.RED, 200, Align.left, false);
        font.draw(readyToDrawBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                (Gdx.graphics.getHeight() / 2));
    }
}
