package de.dkweb.crillionic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.dkweb.crillionic.Crillionic;
import de.dkweb.crillionic.LevelFactory;
import de.dkweb.crillionic.input.SimpleInputProcessor;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.map.MapObject;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.utils.Assets;
import de.dkweb.crillionic.utils.GlobalConstants;
import de.dkweb.crillionic.utils.JsonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dirkweber
 */
public class LevelScreen implements Screen {
    private SpriteBatch backgroundBatch;
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
    private Crillionic game;

    public LevelScreen(Crillionic game, Assets assets) {
        this.game = game;
        this.assets = assets;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FillViewport(GlobalConstants.WORLD_WIDTH_IN_UNITS, GlobalConstants.WORLD_HEIGHT_IN_UNITS,
                camera);

        backgroundTexture = assets.getTexture(Assets.BACKGROUND);
        batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
        world = new World(new Vector2(0f, 0f), true);
        pendingEffects = new ArrayList<ParticleEffectPool.PooledEffect>();

        Vector2 positionPlayer = new Vector2(0f, 3f);
        Body bodyPlayer = definePhysicsObject(positionPlayer, 0.5f, BodyDef.BodyType.DynamicBody, 0f);
        player = new GameObject("Player 1", new Sprite(assets.getTexture(Assets.BALL_TEXTURE)), bodyPlayer, Color.RED);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println(contact.getFixtureA().getBody().getUserData());
                // System.out.println(manifold.getNormal());
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
                            Vector2 contactPos = manifold.getPoints()[0];
                            ParticleEffectPool.PooledEffect explosion = assets.getExplosionEffect();
                            explosion.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                            explosion.setPosition(contactPos.x, contactPos.y);
                            explosion.start();
                            pendingEffects.add(explosion);
                        }
                    }
                }
                System.out.println("Normal impulses: " + impulse.getNormalImpulses().length);
                for (float anImpulse : impulse.getNormalImpulses()) {
                    System.out.println(anImpulse);
                }
            }
        });

        allBlocks = createBlocks(1, new JsonManager());
        allBorders = createBorders();
        Gdx.input.setInputProcessor(new SimpleInputProcessor(camera, player));
        player.moveDown(4000);
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
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border top", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body,
                null));

        body = definePhysicsObject(new Vector2(0f, -1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border bottom", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null));

        body = definePhysicsObject(new Vector2(-1 * (GlobalConstants.WORLD_WIDTH_IN_UNITS / 2), 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border left", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null));

        body = definePhysicsObject(new Vector2(GlobalConstants.WORLD_WIDTH_IN_UNITS / 2, 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border right", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null));

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
            Body body = definePhysicsObject(block.getPosition(), verticesBlock, BodyDef.BodyType.StaticBody, 1f);
            nonPlayerObjects.add(new GameObject(block.getId(), new Sprite(assets.getTexture(Assets.BLOCK_TEXTURE)), body, block
                    .getColor()));
        }
        return nonPlayerObjects;
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


    @Override
    public void render(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        player.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (ParticleEffect effect : pendingEffects) {
            effect.update(Gdx.graphics.getDeltaTime());
        }
        backgroundBatch.begin();
        backgroundBatch.draw(backgroundTexture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch.end();
        camera.position.x = player.getPosition().x;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        player.getSprite().setColor(1f, 1f, 0f, 1f);
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
    }
}
