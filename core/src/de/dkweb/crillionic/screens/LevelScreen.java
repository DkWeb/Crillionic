package de.dkweb.crillionic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import de.dkweb.crillionic.Crillionic;
import de.dkweb.crillionic.LevelFactory;
import de.dkweb.crillionic.events.ColoredBlockCollisionHandler;
import de.dkweb.crillionic.events.ColorizerBlockCollisionHandler;
import de.dkweb.crillionic.events.DoNothingCollisionHandler;
import de.dkweb.crillionic.events.KillBlockCollisionHandler;
import de.dkweb.crillionic.input.SimpleInputProcessor;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.map.MapObject;
import de.dkweb.crillionic.model.GameObjectType;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.model.GameStatistics;
import de.dkweb.crillionic.render.StatisticRenderer;
import de.dkweb.crillionic.utils.Assets;
import de.dkweb.crillionic.utils.GlobalConstants;
import de.dkweb.crillionic.utils.HighscoreManager;
import de.dkweb.crillionic.utils.JsonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dirkweber
 */
public class LevelScreen implements Screen {
    private SpriteBatch staticBatch;
    private SpriteBatch batch;
    private World world;
    private GameObject player;
    private Camera camera;
    private Texture backgroundTexture;
    private Viewport viewport;
    private List<GameObject> allBlocks;
    private List<GameObject> allBorders;
    private Assets assets;
    private List<ParticleEffectPool.PooledEffect> pendingEffects;
    private List<GameObject> toRemove;
    private GameStatistics gameStatistics;
    private Crillionic game;
    private JsonManager jsonManager;

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
        backgroundTexture = assets.getTexture(Assets.BACKGROUND);
        batch = new SpriteBatch();
        staticBatch = new SpriteBatch();
        world = new World(new Vector2(0f, 0f), true);
        pendingEffects = new ArrayList<ParticleEffectPool.PooledEffect>();
        toRemove = new ArrayList<GameObject>();
        gameStatistics = new GameStatistics(0, 1, 3);
        player = recreatePlayer();
        world.setContactListener(new ContactListener() {
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
                        GameObject block = findBlockObject((String) userData);
                        if (block != null) {
                            WorldManifold manifold = contact.getWorldManifold();
                            if (block.onCollision(toRemove, player, gameStatistics)) {
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
                System.out.println("Velocity: " + player.getBody().getLinearVelocity().len());
                System.out.println("Velocity x: " + player.getBody().getLinearVelocity().x);
                System.out.println("Velocity y: " + player.getBody().getLinearVelocity().y);
            }
        });

        allBlocks = createBlocks(1, jsonManager);
        allBorders = createBorders();
        Gdx.input.setInputProcessor(new SimpleInputProcessor(camera, player));
        System.out.println("Velocity: " + player.getBody().getLinearVelocity().len());
        System.out.println("Velocity x: " + player.getBody().getLinearVelocity().x);
        System.out.println("Velocity y: " + player.getBody().getLinearVelocity().y);
        player.moveDown(2000);
    }

    private GameObject recreatePlayer() {
        Vector2 positionPlayer = new Vector2(0f, 3f);
        Body bodyPlayer = definePhysicsObject(positionPlayer, 0.5f, BodyDef.BodyType.DynamicBody, 0f);
        GameObject player = new GameObject(GlobalConstants.PLAYER_ID, new Sprite(assets.getTexture(Assets.BALL_TEXTURE)), bodyPlayer,
                                            Color.GREEN, GameObjectType.PLAYER, new DoNothingCollisionHandler());
        player.moveDown(2000);
        return player;
    }

    private GameObject findBlockObject(String id) {
        for (GameObject block : allBlocks) {
            if (block.getId().equals(id)) {
                return block;
            }
        }
        return null;
    }


    private List<GameObject> createBorders() {
        // We need a border around the level to avoid that our player can "fall out of the screen"
        List<GameObject> borders = new ArrayList<GameObject>();
        Vector2[] verticesBorderHorizontal = new Vector2[] {
                new Vector2(-1 * (GlobalConstants.WORLD_WIDTH_IN_UNITS / 2),  0.1f),
                new Vector2(-1 * (GlobalConstants.WORLD_WIDTH_IN_UNITS / 2), -0.1f),
                new Vector2((GlobalConstants.WORLD_WIDTH_IN_UNITS / 2), -0.1f),
                new Vector2((GlobalConstants.WORLD_WIDTH_IN_UNITS / 2),  0.1f)};
        Vector2[] verticesBorderVertical = new Vector2[] {
                new Vector2(-0.1f,  1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                new Vector2(-0.1f, -1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                new Vector2(0.1f, -1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                new Vector2(0.1f, 1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2))};

        Body body = definePhysicsObject(new Vector2(0f, GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2),
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, 0.8f);
        borders.add(new GameObject("Border top", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body,
                                    null, GameObjectType.BORDER, new DoNothingCollisionHandler()));

        body = definePhysicsObject(new Vector2(0f, -1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border bottom", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null,
                                    GameObjectType.BORDER, new DoNothingCollisionHandler()));

        body = definePhysicsObject(new Vector2(-1 * (GlobalConstants.WORLD_WIDTH_IN_UNITS / 2), 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border left", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null,
                                    GameObjectType.BORDER, new DoNothingCollisionHandler()));

        body = definePhysicsObject(new Vector2(GlobalConstants.WORLD_WIDTH_IN_UNITS / 2, 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border right", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null,
                                    GameObjectType.BORDER, new DoNothingCollisionHandler()));

        return borders;
    }

    private List<GameObject> createBlocks(int level, JsonManager jsonManager) {
        List<GameObject> nonPlayerObjects = new ArrayList<GameObject>();
        LevelMap map = new LevelFactory().createLevel(level, jsonManager);
        Vector2[] verticesBlock= new Vector2[] {
                new Vector2(-1,  0.5f),
                new Vector2(-1, -0.5f),
                new Vector2( 1, -0.5f),
                new Vector2( 1,  0.5f)};
        for (MapObject block : map.getAllBlocks()) {
            nonPlayerObjects.add(createBlockObject(block, verticesBlock));
        }
        return nonPlayerObjects;
    }

    private GameObject createBlockObject(MapObject block, Vector2[] verticesBlock) {
        GameObject gameObject = null;
        Body body = definePhysicsObject(block.getPosition(), verticesBlock, BodyDef.BodyType.StaticBody, 1f);
        if (GameObjectType.COLORED_BLOCKS.contains(block.getType())) {
            gameObject = new GameObject(block.getId(), new Sprite(assets.getTexture(Assets.BLOCK_TEXTURE)), body,
                                        block.getColor(), GameObjectType.getColoredBlockFor(block.getColor()),
                                        new ColoredBlockCollisionHandler());
        } else if (GameObjectType.COLORIZE_BLOCKS.contains(block.getType())){
            gameObject = new GameObject(block.getId(), new Sprite(assets.getTexture(Assets.COLORIZE_BLOCK_TEXTURE)), body,
                    block.getColor(), GameObjectType.getColorizeBlockFor(block.getColor()),
                    new ColorizerBlockCollisionHandler());
        } else if (GameObjectType.KILLER == block.getType()){
            gameObject = new GameObject(block.getId(), new Sprite(assets.getTexture(Assets.KILL_BLOCK_TEXTURE)), body,
                    block.getColor(), GameObjectType.KILLER, new KillBlockCollisionHandler());
        } else {
            gameObject = new GameObject(block.getId(), new Sprite(assets.getTexture(Assets.BLOCK_TEXTURE)), body,
                    block.getColor(), GameObjectType.NORMAL_BLOCK, new DoNothingCollisionHandler());
        }
        return gameObject;
    }

    private Body definePhysicsObject(Vector2 position, float radius, BodyDef.BodyType bodyType,
                                     float resitution) {
        Body targetBody;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        targetBody = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = resitution;
        targetBody.createFixture(fixtureDef);
        shape.dispose();
        return targetBody;
    }

    private Body definePhysicsObject(Vector2 position, Vector2[] vertices, BodyDef.BodyType bodyType,
                                     float resitution) {
        Body targetBody;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        targetBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = resitution;
        targetBody.createFixture(fixtureDef);
        shape.dispose();
        return targetBody;
    }

    private void ensureSpeedLimit() {
        Body playerBody = player.getBody();
        Vector2 playerSpeed = player.getBody().getLinearVelocity();
        float speed = playerSpeed.len();
        if (speed > GlobalConstants.MAX_SPEED_IN_UNITS_PER_RENDER) {
            playerBody.setLinearVelocity(playerSpeed.nor().scl(GlobalConstants.MAX_SPEED_IN_UNITS_PER_RENDER));
        }
    }

    @Override
    public void render(float delta) {
        // Make sure that the player doesn't exceed a maximum speed
        ensureSpeedLimit();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        player.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (ParticleEffect effect : pendingEffects) {
            effect.update(Gdx.graphics.getDeltaTime());
        }
        staticBatch.begin();
        staticBatch.draw(backgroundTexture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // new StatisticRenderer().renderGameStatistics(gameStatistics, staticBatch, assets);
        staticBatch.end();
        camera.position.x = player.getPosition().x;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.getSprite().draw(batch);
        for (GameObject block : allBlocks) {
            block.getSprite().draw(batch);
        }
        for (GameObject border : allBorders) {
            border.getSprite().draw(batch);
        }
        for (ParticleEffect effect : pendingEffects) {
            effect.draw(batch, Gdx.graphics.getDeltaTime());
        }
        batch.end();
        staticBatch.begin();
        new StatisticRenderer().renderGameStatistics(gameStatistics, staticBatch, assets);
        staticBatch.end();

        removeOutdatedParticleEffects();
        removeElementsFromWorld();
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

    private void removeElementsFromWorld() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (toRemove.size() > 0) {
                    for (GameObject oneToRemove : toRemove) {
                        world.destroyBody(oneToRemove.getBody());
                        if (oneToRemove.getId().equals(GlobalConstants.PLAYER_ID)) {
                            // The player has already been removed from the physics, but let him still be visible for
                            // some seconds, so that he can "explode". Afterwards we can show some message
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    if (gameStatistics.getLifes() == 0) {
                                        LevelScreen.this.dispose();
                                        new HighscoreManager().addEntry(gameStatistics.getScore(), jsonManager);
                                        game.openMainMenu();
                                    } else {
                                        recreatePlayer();
                                    }
                                }
                            }, 2);
                        }
                    }
                    allBlocks.removeAll(toRemove);
                    toRemove.clear();
                }
            }
        });
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
        staticBatch.dispose();
        batch.dispose();
        for (ParticleEffectPool.PooledEffect effect : pendingEffects) {
            assets.freeEffect(effect);
        }
        world.dispose();
    }
}
