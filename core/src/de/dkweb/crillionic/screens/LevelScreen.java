/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Dirk Weber
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.dkweb.crillionic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import de.dkweb.crillionic.Crillionic;
import de.dkweb.crillionic.model.GameWorld;
import de.dkweb.crillionic.utils.*;
import de.dkweb.crillionic.input.SimpleInputProcessor;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.render.StatisticRenderer;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen implements Screen {
    private SpriteBatch staticBatch;
    private SpriteBatch batch;
    private Camera camera;
    private Texture backgroundTexture;
    private Viewport viewport;
    private Assets assets;
    private List<ParticleEffectPool.PooledEffect> pendingEffects;
    private List<GameObject> toRemove;
    private Crillionic game;
    private JsonManager jsonManager;
    private boolean levelCompleted;

    public LevelScreen(Crillionic game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(calculateNonDistortingWidth(), GlobalConstants.WORLD_HEIGHT_IN_UNITS);
        viewport = new FitViewport(calculateNonDistortingWidth(), GlobalConstants.WORLD_HEIGHT_IN_UNITS,
                                    camera);
        jsonManager = new JsonManager();
        LevelMap map = new LevelFactory().createLevel(1, jsonManager);
        GameWorld.getWorld().init(assets, map);
        backgroundTexture = assets.getTexture(Assets.BACKGROUND);
        batch = new SpriteBatch();
        staticBatch = new SpriteBatch();
        pendingEffects = new ArrayList<ParticleEffectPool.PooledEffect>();
        toRemove = new ArrayList<GameObject>();
        GameWorld.getWorld().getPhysicsWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                if (contact.getFixtureA() != null) {
                    Object userData = contact.getFixtureA().getBody().getUserData();
                    if (userData != null && userData instanceof String) {
                        // This should be the id of GameObject
                        GameObject block = GameWorld.getWorld().findBlockObject((String) userData);
                        if (block != null) {
                            WorldManifold manifold = contact.getWorldManifold();
                            if (block.onCollision(toRemove, GameWorld.getWorld().getPlayer(), GameWorld.getWorld()
                                    .getGameStatistics())) {
                                Vector2 contactPos = manifold.getPoints()[0];
                                ParticleEffectPool.PooledEffect explosion = assets.getExplosionEffect();
                                explosion.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                                explosion.setPosition(contactPos.x, contactPos.y);
                                explosion.start();
                                pendingEffects.add(explosion);
                            }
                        }
                    }
                }
            }
        });
        levelCompleted = false;
        Gdx.input.setInputProcessor(new SimpleInputProcessor(camera, GameWorld.getWorld().getPlayer()));
    }

    private void ensureSpeedLimit() {
        GameObject player = GameWorld.getWorld().getPlayer();
        Body playerBody = player.getBody();
        Vector2 playerSpeed = player.getBody().getLinearVelocity();
        float speed = playerSpeed.len();
        if (speed > GlobalConstants.MAX_SPEED_IN_UNITS_PER_RENDER) {
            playerBody.setLinearVelocity(playerSpeed.nor().scl(GlobalConstants.MAX_SPEED_IN_UNITS_PER_RENDER));
        }
    }

    @Override
    public void render(float delta) {
        GameObject player = GameWorld.getWorld().getPlayer();

        // Make sure that the player doesn't exceed a maximum speed
        ensureSpeedLimit();
        GameWorld.getWorld().getPhysicsWorld().step(Gdx.graphics.getDeltaTime(), 6, 2);
        boolean playerDestroyedNow = GameWorld.getWorld().destroyGameObjects(toRemove);
        removeOutdatedParticleEffects();
        // Just "freeze" the player when the level has been completed
        if (!levelCompleted) {
            player.update();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (ParticleEffect effect : pendingEffects) {
            effect.update(Gdx.graphics.getDeltaTime());
        }
        staticBatch.begin();
        staticBatch.draw(backgroundTexture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        staticBatch.end();
        camera.position.x = player.getPosition().x;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.getSprite().draw(batch);
        for (GameObject block : GameWorld.getWorld().getBlocks()) {
            block.getSprite().draw(batch);
        }
        for (GameObject border : GameWorld.getWorld().getBorders()) {
            border.getSprite().draw(batch);
        }
        for (ParticleEffect effect : pendingEffects) {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();
        GameStatistics gameStatistics = GameWorld.getWorld().getGameStatistics();
        staticBatch.begin();
        new StatisticRenderer().renderGameStatistics(gameStatistics, staticBatch, assets);
        if (gameStatistics.getRemainingColorBlocks() == 0) {
            renderLevelCompleted();
        }
        if (gameStatistics.getRemainingColorBlocks() == 0 && !levelCompleted) {
            levelCompleted = true;
            new HighscoreManager(new FileUtils()).addEntry(gameStatistics.getScore(), jsonManager);
            scheduleSwitchToNextLevel();
        }
        if (gameStatistics.getLifes() == 0) {
            renderGameOver(new HighscoreManager(new FileUtils()).addEntry(gameStatistics.getScore(), jsonManager));
        }
        if (playerDestroyedNow && gameStatistics.getLifes() > 0 ) {
            scheduleSpawnNewPlayer();
        }
        if (playerDestroyedNow && gameStatistics.getLifes() == 0) {
            scheduleSwitchToHighscore();
        }
        staticBatch.end();
    }

    private void scheduleSwitchToNextLevel() {
        // Switch to the next level some seconds later
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.openHighscore();
            }
        }, 3);
    }

    private void scheduleSpawnNewPlayer() {
        // Spawn new player some seconds later
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                GameWorld.getWorld().recreatePlayer();
            }
        }, 2);
    }

    private void scheduleSwitchToHighscore() {
        // Switch to the highscore screen some seconds later
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.openHighscore();
            }
        }, 3);
    }

    private void renderGameOver(boolean isInHighscore) {
        // Show the game over screen
        I18NBundle bundle = assets.getBundle();
        BitmapFont font = assets.getBigBitmapFont();
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, bundle.get("game_over"), Color.RED, 200, Align.left, false);
        font.draw(staticBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                (Gdx.graphics.getHeight() / 2));

        if (isInHighscore) {
            layout = new GlyphLayout(font, bundle.get("highscore_reached"), Color.RED, 200, Align.left, false);
            font.draw(staticBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                    (Gdx.graphics.getHeight() / 2) - layout.height * 2);
        }
    }

    private void renderLevelCompleted() {
        // Show the level completed screen
        I18NBundle bundle = assets.getBundle();
        BitmapFont font = assets.getBigBitmapFont();
        font.setColor(Color.RED);
        GlyphLayout layout = new GlyphLayout(font, bundle.get("level_completed"), Color.RED, 200, Align.left, false);
        font.draw(staticBatch, layout, Gdx.graphics.getWidth() / 2 - (layout.width / 2),
                (Gdx.graphics.getHeight() / 2));
    }

    private void removeOutdatedParticleEffects() {
        List<ParticleEffect> effectsToRemove = new ArrayList<ParticleEffect>();
        for (ParticleEffectPool.PooledEffect effect : pendingEffects) {
            if (effect.isComplete()) {
                assets.freeEffect(effect);
                effectsToRemove.add(effect);
            }
        }
        if (effectsToRemove.size() > 0) {
            pendingEffects.removeAll(effectsToRemove);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * We want that there is no vertical scrolling -> we must calculate the width according to the available screen
     * height
     */
    private float calculateNonDistortingWidth() {
        return (Gdx.graphics.getWidth() * GlobalConstants.WORLD_HEIGHT_IN_UNITS) / Gdx.graphics.getHeight();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (staticBatch != null) {
            staticBatch.dispose();
            staticBatch = null;
        }
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        for (ParticleEffectPool.PooledEffect effect : pendingEffects) {
            assets.freeEffect(effect);
        }
        GameWorld.getWorld().dispose();
    }
}
