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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Describes one level consisting of the different blocks
 */
public class LevelMap {
    public static final int WIDTH_IN_UNITS = 30;
    public static final int HEIGHT_IN_UNITS = 20;

    private int levelId;
    private Map<String, MapObject> blocks;

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



}
