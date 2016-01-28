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
package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.dkweb.crillionic.map.LevelMap;

import java.io.IOException;
import java.io.InputStream;

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
