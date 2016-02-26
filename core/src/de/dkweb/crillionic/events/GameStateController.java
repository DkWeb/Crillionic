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
import de.dkweb.crillionic.model.GameState;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.model.GameWorld;
import de.dkweb.crillionic.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dirkweber
 */
public class GameStateController {
    private Crillionic game;
    private GameWorld gameWorld;
    private Assets assets;
    private JsonManager jsonManager;
    private GameStatistics formerStatistics;
    private List<GameState> postponedStates;

    public GameStateController(Crillionic game, Assets assets, JsonManager jsonManager, GameWorld gameWorld) {
        this.game = game;
        this.gameWorld = gameWorld;
        this.assets = assets;
        this.jsonManager = jsonManager;
        this.formerStatistics = gameWorld.getGameStatistics();
        this.postponedStates = new ArrayList<GameState>();
    }

    public void updateGameState(SpriteBatch readyToDrawBatch) {
        GameStatistics currentStatistics = gameWorld.getGameStatistics();
        if (currentStatistics.getLifes() < formerStatistics.getLifes()) {
            // We have either lost a life via a collision or via a tilt
            if (gameWorld.getPlayer().getSpeed() < GlobalConstants.MIN_SPEED_IN_UNITS_PER_RENDER) {
                gameWorld.rememberGameState(GameState.PLAYER_TILT);
            } else {
                gameWorld.rememberGameState(GameState.PLAYER_DESTROYED);
            }
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
        if (currentStatistics.getRemainingColorBlocks() == 0 && formerStatistics.getRemainingColorBlocks() > 0) {
            checkAndAddToHighscore(currentStatistics);
            gameWorld.rememberGameState(GameState.WAITING_FOR_NEXT_LEVEL);
            switchToHighscoreWithDelay(2);
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }

        GameState currentState = gameWorld.getGameState(0);
        if (currentState == GameState.PLAYING) {
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
        if (currentState == GameState.PLAYER_TILT) {
            showTilt(readyToDrawBatch);
            if (!postponedStates.contains(GameState.PLAYER_DESTROYED)) {
                changeStateWithDelay(GameState.PLAYER_DESTROYED, 2);
            }
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
        if (currentState == GameState.PLAYER_DESTROYED) {
            // Two possibilities: 1.) Player has lifes left, 2.) Player has no more lifes left
            if (gameWorld.getGameStatistics().getLifes() == 0) {
                checkAndAddToHighscore(currentStatistics);
                gameWorld.rememberGameState(GameState.WAITING_FOR_GAME_OVER);
                switchToHighscoreWithDelay(2);
            } else {
                gameWorld.rememberGameState(GameState.WAITING_FOR_RESPAWN);
                respawnPlayerWithDelay(2);
            }
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
        if (currentState == GameState.WAITING_FOR_GAME_OVER) {
            boolean isInHighscore = isInHighscore(currentStatistics);
            showGameOver(isInHighscore, readyToDrawBatch);
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
        if (currentState == GameState.WAITING_FOR_RESPAWN) {
            // Do nothing up to now
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
        if (currentState == GameState.WAITING_FOR_NEXT_LEVEL) {
            showLevelCompleted(readyToDrawBatch);
            formerStatistics = new GameStatistics(currentStatistics);
            return;
        }
    }

    private boolean isInHighscore(GameStatistics currentStatistics) {
        int score = GameWorld.getWorld().getScoreCalculator().getLevelScore(currentStatistics);
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtils());
        return highscoreManager.isInHighscore(score, jsonManager);
    }

    private boolean checkAndAddToHighscore(GameStatistics currentStatistics) {
        int score = GameWorld.getWorld().getScoreCalculator().getLevelScore(currentStatistics);
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtils());
        return highscoreManager.addEntry(score, jsonManager);
    }

    private void switchToHighscoreWithDelay(long delayInSeconds) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.openHighscore();
            }
        }, delayInSeconds);
    }

    private void respawnPlayerWithDelay(long delayInSeconds) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameWorld.recreatePlayer();
                gameWorld.rememberGameState(GameState.PLAYING);
            }
        }, delayInSeconds);
    }

    private void changeStateWithDelay(final GameState newState, int delayInSeconds) {
        postponedStates.add(newState);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                postponedStates.remove(newState);
                gameWorld.rememberGameState(newState);
            }
        }, delayInSeconds);
    }

    private void showTilt(SpriteBatch readyToDrawBatch) {
        // Show the tilt text
        I18NBundle bundle = assets.getBundle();
        BitmapFont font = assets.getBigBitmapFont();
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, bundle.get("zero_speed"), Color.RED, 200, Align.left, false);
        font.draw(readyToDrawBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                (Gdx.graphics.getHeight() / 2) - layout.height * 2);
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
