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

import com.badlogic.gdx.math.Vector2;
import de.dkweb.crillionic.model.GameObjectType;

import java.util.*;

/**
 * Describes one level consisting of the different blocks
 */
public class LevelMap {
    public static final int WIDTH_IN_UNITS = 30;
    public static final int HEIGHT_IN_UNITS = 20;

    private int levelId;
    private Map<String, MapObject> blocks;
    private Vector2 initialPlayerPosition;
    private Vector2 initialPlayerImpulse;

    public LevelMap(int levelId) {
        blocks = new HashMap<String, MapObject>();
        this.levelId = levelId;
    }

    public void addBlock(String id, MapObject block) {
        blocks.put(id, block);
    }

    public MapObject getBlock(String id) {
        return blocks.get(id);
    }

    public boolean removeBlock(String id) {
        return blocks.remove(id) != null;
    }

    public Collection<MapObject> getAllBlocks() {
        return blocks.values();
    }

    public List<MapObject> getColoredBlocks() {
        List<MapObject> coloredBlocks = new ArrayList<MapObject>();
        for (MapObject anObject : blocks.values()) {
            if (GameObjectType.COLORED_BLOCKS.contains(anObject.getType())) {
                coloredBlocks.add(anObject) ;
            }
        }
        return coloredBlocks;
    }

    public Vector2 getInitialPlayerPosition() {
        return initialPlayerPosition;
    }

    public Vector2 getInitialPlayerImpulse() {
        return initialPlayerImpulse;
    }

    public int getLevelId() {
        return levelId;
    }
}
