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
package de.dkweb.crillionic.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.dkweb.crillionic.model.GameObjectType;

/**
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
