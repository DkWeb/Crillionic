package de.dkweb.crillionic.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.dkweb.crillionic.events.ColoredBlockCollisionHandler;
import de.dkweb.crillionic.events.ColorizerBlockCollisionHandler;
import de.dkweb.crillionic.events.DoNothingCollisionHandler;
import de.dkweb.crillionic.events.KillBlockCollisionHandler;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.map.MapObject;
import de.dkweb.crillionic.utils.Assets;
import de.dkweb.crillionic.utils.GlobalConstants;
import de.dkweb.crillionic.utils.ScoreCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for all GameObjects of the world and the world-representation of the physics engine.
 *
 * May exist only once, thus it is implemented as a singleton.
 *
 * Be aware that because of the android memory model we can not just set some variables from the outside
 * and expect the class to hold this state for its whole lifetime: whenever the game is in the background,
 * it can be "killed" any time.
 * Whenever the app comes into the foreground again, the class is newly instantiated
 * -> existing (even static) variables are set to the value the constructor of this class assigns them.
 *
 * Normally we would cover this problem by doing a lazy initialization for every getter (thus, passing all necessary
 * parameters for object-recreating each time we call the getter).
 *
 * This class uses another approach (to avoid lazy initialization within the first render-call and to avoid passing
 * all the parameters around all the time): the LevelScreen must guarantee to call the init-method each time the app
 * comes back into the foreground -> the singleton will be instantiated again (and we have a central point to
 * "re-set" the state of the class to the state BEFORE the app was set to the background if we want to offer this)
 */
public class GameWorld {
    private final static GameWorld instance = new GameWorld();
    private final static ScoreCalculator scoreCalculator = new ScoreCalculator();

    private List<GameObject> allBlocks;
    private List<GameObject> allBorders;
    private GameObject player;
    private GameStatistics statistics;
    private World physicsWorld;


    private Assets assets;
    private LevelMap map;

    private GameWorld() {
        allBlocks = null;
    }

    public final static GameWorld getWorld() {
        return instance;
    }

    /**
     * This method MUST be called whenever the app comes back into the foreground.
     * Otherwise the singleton will not be instantiated correctly
     */
    public void init(Assets assets, LevelMap map) {
        this.assets = assets;
        this.map = map;
        initializeGameStatistics(map);
        intializeBlocks(assets, map);
        initializePlayer(assets, map);
        initializeBorders(assets);

    }

    public World getPhysicsWorld() {
        if (physicsWorld == null) {
            physicsWorld = new World(new Vector2(0f, 0f), true);
        }
        return physicsWorld;
    }

    private void intializeBlocks(Assets assets, LevelMap  map) {
        allBlocks = createBlocks(assets, map, getGameStatistics());
    }

    private void initializePlayer(Assets assets, LevelMap map) {
        Vector2 positionPlayer = map.getInitialPlayerPosition();
        Body bodyPlayer = definePhysicsObject(positionPlayer, 0.5f, BodyDef.BodyType.DynamicBody, 0f);
        player = new GameObject(GlobalConstants.PLAYER_ID, new Sprite(assets.getTexture(Assets.BALL_TEXTURE)), bodyPlayer,
                                Color.GREEN, GameObjectType.PLAYER, new PlayerCollisionHandler());
    }

    public List<GameObject> getBlocks() {
        if (allBlocks == null) {
            throw new IllegalStateException("You must call init-method before calling a method of this singleton!");
        }
        return allBlocks;
    }

    public GameObject recreatePlayer() {
        initializePlayer(assets, map);
        return getPlayer();
    }

    public GameObject getPlayer() {
        if (player == null) {
            throw new IllegalStateException("You must call init-method before calling a method of this singleton!");
        }
        return player;
    }

    public GameObject findBlockObject(String id) {
        if (allBlocks == null) {
            throw new IllegalStateException("You must call init-method before calling a method of this singleton!");
        }
        for (GameObject block : allBlocks) {
            if (block.getId().equals(id)) {
                return block;
            }
        }
        return null;
    }

    public GameObject findBorderObject(String id) {
        if (allBorders == null) {
            throw new IllegalStateException("You must call init-method before calling a method of this singleton!");
        }
        for (GameObject border : allBorders) {
            if (border.getId().equals(id)) {
                return border;
            }
        }
        return null;
    }

    public GameObject findLevelObject(String id) {
        GameObject block = findBlockObject(id);
        if (block != null) {
            return block;
        }
        return findBorderObject(id);
    }

    public List<GameObject> getBorders() {
        if (allBorders == null) {
            throw new IllegalStateException("You must call init-method before calling a method of this singleton!");
        }
        return allBorders;
    }

    public GameStatistics getGameStatistics() {
        if (statistics == null) {
            throw new IllegalStateException("You must call init-method before calling a method of this singleton!");
        }
        return statistics;
    }

    public ScoreCalculator getScoreCalculator() {
        return scoreCalculator;
    }

    public void dispose() {
        if (physicsWorld != null) {
            physicsWorld.dispose();
            physicsWorld = null;
        }
    }

    /**
     * @return true, if the player was part of the game objects to destroy
     */
    public boolean destroyGameObjects(List<GameObject> toRemove) {
        boolean playerDestroyed = false;
        // The world should be unlocked now -> we can destroy bodies now
        if (toRemove.size() > 0) {
            for (GameObject oneToRemove : toRemove) {
                getPhysicsWorld().destroyBody(oneToRemove.getBody());
                if (oneToRemove.getType() == GameObjectType.PLAYER) {
                    playerDestroyed = true;
                }
            }
            GameWorld.getWorld().getBlocks().removeAll(toRemove);
            toRemove.clear();
        }
        return playerDestroyed;
    }

    private void initializeGameStatistics(LevelMap map) {
        statistics = new GameStatistics(0, map.getLevelId(), GlobalConstants.INITIAL_LIFES,
                                        map.getColoredBlocks().size(), GlobalConstants.INITIAL_TIME);
    }

    private void initializeBorders(Assets assets) {
        // We need a border around the level to avoid that our player can "fall out of the screen"
        allBorders = new ArrayList<GameObject>();
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
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, GlobalConstants.BORDER_RESITUTION);
        allBorders.add(new GameObject("Border top", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body,
                null, GameObjectType.BORDER, new DoNothingCollisionHandler()));

        body = definePhysicsObject(new Vector2(0f, -1 * (GlobalConstants.WORLD_HEIGHT_IN_UNITS / 2)),
                verticesBorderHorizontal, BodyDef.BodyType.StaticBody, GlobalConstants.BORDER_RESITUTION);
        allBorders.add(new GameObject("Border bottom", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null,
                GameObjectType.BORDER, new DoNothingCollisionHandler()));

        body = definePhysicsObject(new Vector2(-1 * (GlobalConstants.WORLD_WIDTH_IN_UNITS / 2), 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, GlobalConstants.BORDER_RESITUTION);
        allBorders.add(new GameObject("Border left", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null,
                GameObjectType.BORDER, new DoNothingCollisionHandler()));

        body = definePhysicsObject(new Vector2(GlobalConstants.WORLD_WIDTH_IN_UNITS / 2, 0f),
                verticesBorderVertical, BodyDef.BodyType.StaticBody, GlobalConstants.BORDER_RESITUTION);
        allBorders.add(new GameObject("Border right", new Sprite(assets.getTexture(Assets.BORDER_TEXTURE)), body, null,
                GameObjectType.BORDER, new DoNothingCollisionHandler()));
    }

    private Body definePhysicsObject(Vector2 position, float radius, BodyDef.BodyType bodyType,
                                     float resitution) {
        Body targetBody;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        targetBody = getPhysicsWorld().createBody(bodyDef);

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
        targetBody = getPhysicsWorld().createBody(bodyDef);

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

    private GameObject createBlockObject(MapObject block, Vector2[] verticesBlock, Assets assets,
                                         GameStatistics gameStatistics) {
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
            gameObject = new GameObject(block.getId(), new Sprite(assets.getTexture(Assets.NORMAL_BLOCK_TEXTURE)), body,
                    block.getColor(), GameObjectType.NORMAL_BLOCK, new DoNothingCollisionHandler());
        }
        return gameObject;
    }

    private List<GameObject> createBlocks(Assets assets, LevelMap map, GameStatistics gameStatistics) {
        List<GameObject> blocks = new ArrayList<GameObject>();
        Vector2[] verticesBlock= new Vector2[] {
                new Vector2(-1,  0.5f),
                new Vector2(-1, -0.5f),
                new Vector2( 1, -0.5f),
                new Vector2( 1,  0.5f)};
        for (MapObject block : map.getAllBlocks()) {
            blocks.add(createBlockObject(block, verticesBlock, assets, gameStatistics));
        }
        return blocks;
    }
}
