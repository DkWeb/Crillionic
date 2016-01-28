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
import de.dkweb.crillionic.model.Highscore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HighscoreManager {
    public Highscore getHighscore(JsonManager jsonManager) {
        InputStream inputStream = null;
        Highscore currentHighscore;
        try {
            FileHandle handle = Gdx.files.getFileHandle("highscore.json", Files.FileType.Local);
            if (handle.exists()) {
                inputStream = handle.read();
                currentHighscore = jsonManager.loadHighscore(inputStream);
            } else {
                currentHighscore = new Highscore();
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Gdx.app.error(GlobalConstants.APP_TAG, "Unable to close input stream to highscore file", e);
                }
            }
        }
        return currentHighscore;
    }
    public boolean addEntry(int achievedScore, JsonManager jsonManager) {
        if (achievedScore == 0) {
            return false;
        }
        Highscore currentHighscore = getHighscore(jsonManager);
        if (currentHighscore == null) {
            Gdx.app.error(GlobalConstants.APP_TAG, "Unable to read out current highscores from file. Abort");
            return false;
        }


        boolean added = currentHighscore.addToHighscore(achievedScore);
        if (!added) {
            return false;
        }
        // Persist the result
        OutputStream outputStream = null;
        try {
            FileHandle handle = Gdx.files.getFileHandle("highscore.json", Files.FileType.Local);
            outputStream = handle.write(false);
            jsonManager.saveHighscore(currentHighscore, outputStream);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Gdx.app.log(GlobalConstants.APP_TAG, "Unable to close output stream to highscore file", e);
                }
            }
        }
        return true;
    }
}
