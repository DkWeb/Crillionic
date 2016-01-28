package de.dkweb.crillionic.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.dkweb.crillionic.model.GameObjectType;

/**
 * Created by dirkweber
 *
 * The description of a later GameObject block but without any "game engine" specific information
 * You can store this description easily in an external format like json
 */
public class MapObject {
    private String id;

    private GameObjectType type;

    // The position of this block
    // Be aware that this is the position of the center of the block in global coordinates
    private Vector2 position;

    public MapObject(String id, GameObjectType type, Vector2 position) {
        this.id = id;
        this.type = type;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public GameObjectType getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor() {
        return type.getDefaultColor();
    }
}
