package de.dkweb.crillionic;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.dkweb.crillionic.input.SimpleInputProcessor;
import de.dkweb.crillionic.model.GameObject;

public class Crillionic extends ApplicationAdapter {
    SpriteBatch backgroundBatch;
	SpriteBatch batch;
    World world;
    GameObject player;
    GameObject ground;
    GameObject ground2;
    Camera camera;
    Texture backgroundTexture;
    Viewport viewport;

	@Override
	public void create () {
        camera = new OrthographicCamera(30, 20);
        viewport = new StretchViewport(30, 20, camera);

        backgroundTexture = new Texture("background.jpg");
		batch = new SpriteBatch();
        backgroundBatch = new SpriteBatch();
        world = new World(new Vector2(0f, 0f), true);

        Vector2 positionPlayer = new Vector2(0f, 7f);
        Vector2[] verticesPlayer = new Vector2[] {
                                                new Vector2(-1,  1),
                                                new Vector2(-1, -1),
                                                new Vector2( 1, -1),
                                                new Vector2( 1,  1)};
        Vector2 positionWood = new Vector2(0f, 0f);
        Vector2[] verticesWood = new Vector2[] {
                                                new Vector2(-4,  0.1f),
                                                new Vector2(-4, -0.1f),
                                                new Vector2( 4, -0.1f),
                                                new Vector2( 4,  0.1f)};
        Body bodyPlayer = definePhysicsObject(positionPlayer, 0.5f, BodyDef.BodyType.DynamicBody, 0f);
        Body bodyGround = definePhysicsObject(positionWood, verticesWood, BodyDef.BodyType.StaticBody, 1f);
        Body bodyGround2 = definePhysicsObject(new Vector2(0, 8f), verticesWood, BodyDef.BodyType.StaticBody, 1f);
        player = new GameObject("Player 1", new Sprite(new Texture("ball_less_color.png")), bodyPlayer);
        ground = new GameObject("Ground 1", new Sprite(new Texture("wood.jpg")), bodyGround);
        ground2 = new GameObject("Ground 2", new Sprite(new Texture("wood.jpg")), bodyGround2);

        // ground.rotate(25);
        Gdx.input.setInputProcessor(new SimpleInputProcessor(camera, player));
        player.moveDown(1000);
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
        ground.update();
        ground2.update();
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
        ground.getSprite().draw(batch);
        ground2.getSprite().draw(batch);
		batch.end();
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
