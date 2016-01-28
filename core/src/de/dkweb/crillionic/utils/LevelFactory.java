package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.utils.GlobalConstants;
import de.dkweb.crillionic.utils.JsonManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dirkweber
 */
public class LevelFactory {
    public LevelMap createLevel(int levelNo, JsonManager jsonManager) {
        if (levelNo != 1) {
            throw new IllegalArgumentException("We have just implemented level 1 up to now!");
        }
        LevelMap map = null;
        InputStream inputStream = null;
        try {
            FileHandle handle = Gdx.files.getFileHandle("levels/map" + String.valueOf(levelNo) + ".json" , Files
                    .FileType
                    .Internal);
            inputStream = handle.read();
            map = jsonManager.loadLevelMap(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Gdx.app.log(GlobalConstants.APP_TAG, "Unable to close stream to map file", e);
                }
            }
        }
        return map;
    }
}
