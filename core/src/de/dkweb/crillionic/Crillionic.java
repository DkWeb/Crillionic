package de.dkweb.crillionic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.dkweb.crillionic.input.SimpleInputProcessor;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.map.MapObject;
import de.dkweb.crillionic.model.GameObject;
import de.dkweb.crillionic.utils.GlobalConstants;
import de.dkweb.crillionic.utils.JsonManager;

import java.util.ArrayList;
import java.util.List;

public class Crillionic extends ApplicationAdapter {
    SpriteBatch backgroundBatch;
	SpriteBatch batch;
    World world;
    GameObject player;
    Camera camera;
    Texture backgroundTexture;
    Viewport viewport;
    List<GameObject> allBlocks;
    List<GameObject> allBorders;

	@Override
	public void create () {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GlobalConstants.WORLD_WIDTH_IN_UNITS, GlobalConstants.WORLD_HEIGHT_IN_UNITS,
                camera);

        backgroundTexture = new Texture("background.jpg");
		batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
        world = new World(new Vector2(0f, 0f), true);

        Vector2 positionPlayer = new Vector2(0f, 3f);
        Body bodyPlayer = definePhysicsObject(positionPlayer, 0.5f, BodyDef.BodyType.DynamicBody, 0f);
        // Body bodyGround = definePhysicsObject(positionWood, verticesWood, BodyDef.BodyType.StaticBody, 1f);
        // Body bodyGround2 = definePhysicsObject(new Vector2(0, 4f), verticesWood, BodyDef.BodyType.StaticBody, 1f);
        player = new GameObject("Player 1", new Sprite(new Texture("ball_less_color.png")), bodyPlayer, Color.RED);
        // ground = new GameObject("Ground 1", new Sprite(new Texture("wood.jpg")), bodyGround);
        // ground2 = new GameObject("Ground 2", new Sprite(new Texture("wood.jpg")), bodyGround2);
        // block1 = new GameObject("Block 1", new Sprite(new Texture("block.png")), block);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println(contact.getFixtureA().getBody().getUserData());
                WorldManifold manifold = contact.getWorldManifold();
                System.out.println(manifold.getNormal());

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        allBlocks = createBlocks(1, new JsonManager());
        allBorders = createBorders();
        Gdx.input.setInputProcessor(new SimpleInputProcessor(camera, player));
        player.moveDown(500);

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
        borders.add(new GameObject("Border top", new Sprite(new Texture("wood.jpg")), body, null));

        body = definePhysicsObject(new Vector2(0f, -1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border bottom", new Sprite(new Texture("wood.jpg")), body, null));

        body = definePhysicsObject(new Vector2(-1 * (GlobalConstants.WORLD_WIDTH_IN_UNITS / 2), 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border left", new Sprite(new Texture("wood.jpg")), body, null));

        body = definePhysicsObject(new Vector2(GlobalConstants.WORLD_WIDTH_IN_UNITS / 2, 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, 1f);
        borders.add(new GameObject("Border right", new Sprite(new Texture("wood.jpg")), body, null));

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
            nonPlayerObjects.add(new GameObject(block.getId(), new Sprite(new Texture("block.png")), body, block.getColor()));
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
	public void render () {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        // ground.update();
        // ground2.update();
        player.update();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        backgroundBatch.begin();
        backgroundBatch.draw(backgroundTexture, 0f, 0f);
        backgroundBatch.end();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.getSprite().setColor(1f, 1f, 0f, 1f);
        player.getSprite().draw(batch);
        // ground.getSprite().draw(batch);
        // ground2.getSprite().draw(batch);
        // block1.getSprite().setColor(Color.RED);
        // block1.getSprite().draw(batch);
        for (GameObject block : allBlocks) {
            block.getSprite().draw(batch);
        }
        for (GameObject border : allBorders) {
            border.getSprite().draw(batch);
        }
		batch.end();
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
