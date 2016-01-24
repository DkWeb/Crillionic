package de.dkweb.crillionic.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dirkweber
 *
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
