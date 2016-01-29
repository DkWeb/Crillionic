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
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class HighscoreManagerTest {
    @Test
    public void readEmptyHighscore() {
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtilsMock(new FileHandle("notExisting")));
        Highscore currentHighscore = highscoreManager.getHighscore(new JsonManager());
        assertEquals(0, currentHighscore.getScores().size());
    }

    @Test
    public void readHighscoreWithTwoEntries() {
        File highscoreFile = getTestResourceFile("highscore.json");
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtilsMock(new FileHandle(highscoreFile)));
        Highscore currentHighscore = highscoreManager.getHighscore(new JsonManager());
        assertEquals(2, currentHighscore.getScores().size());
        assertEquals(Integer.valueOf(2000), currentHighscore.getScores().get(0));
        assertEquals(Integer.valueOf(1000), currentHighscore.getScores().get(1));
    }

    @Test
    public void addEntryToEmptyHighscore() throws IOException {
        File originalFile = getTestResourceFile("empty_highscore.json");
        // Copy the original file and work with the copy only
        File copyFile;
        new FileUtils().copyFile(originalFile, new File(originalFile.getParentFile(), "highscore_copy.json"));
        copyFile = getTestResourceFile("highscore_copy.json");
        copyFile.deleteOnExit();
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtilsMock(new FileHandle(copyFile)));
        assertTrue(highscoreManager.addEntry(3000, new JsonManager()));

        Highscore currentHighscore = highscoreManager.getHighscore(new JsonManager());
        assertEquals(1, currentHighscore.getScores().size());
        assertEquals(Integer.valueOf(3000), currentHighscore.getScores().get(0));
    }

    @Test
    public void addEntryToHighscore() throws IOException {
        File originalFile = getTestResourceFile("highscore.json");
        // Copy the original file and work with the copy only
        File copyFile;
        new FileUtils().copyFile(originalFile, new File(originalFile.getParentFile(), "highscore_copy.json"));
        copyFile = getTestResourceFile("highscore_copy.json");
        copyFile.deleteOnExit();
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtilsMock(new FileHandle(copyFile)));
        assertTrue(highscoreManager.addEntry(3000, new JsonManager()));

        Highscore currentHighscore = highscoreManager.getHighscore(new JsonManager());
        assertEquals(3, currentHighscore.getScores().size());
        assertEquals(Integer.valueOf(3000), currentHighscore.getScores().get(0));
        assertEquals(Integer.valueOf(2000), currentHighscore.getScores().get(1));
        assertEquals(Integer.valueOf(1000), currentHighscore.getScores().get(2));
    }

    @Test
    public void addEntryToHighscoreExceedsHighscoreLimit() throws IOException {
        File originalFile = getTestResourceFile("highscore.json");
        // Copy the original file and work with the copy only
        File copyFile;
        new FileUtils().copyFile(originalFile, new File(originalFile.getParentFile(), "highscore_copy.json"));
        copyFile = getTestResourceFile("highscore_copy.json");
        copyFile.deleteOnExit();
        HighscoreManager highscoreManager = new HighscoreManager(new FileUtilsMock(new FileHandle(copyFile)));
        for (int i = 2; i < GlobalConstants.MAX_HIGHSCORE_ENTRIES; i++) {
            assertTrue(highscoreManager.addEntry((i + 1) * 1000, new JsonManager()));
        }
        Highscore currentHighscore = highscoreManager.getHighscore(new JsonManager());
        assertEquals(GlobalConstants.MAX_HIGHSCORE_ENTRIES, currentHighscore.getScores().size());
        assertTrue(highscoreManager.addEntry(100000, new JsonManager()));

        currentHighscore = highscoreManager.getHighscore(new JsonManager());
        assertEquals(10, currentHighscore.getScores().size());
        assertEquals(Integer.valueOf(100000), currentHighscore.getScores().get(0));
    }


    private File getTestResourceFile(String name) {
        return new File(getClass().getClassLoader().getResource(name).getFile());
    }

}

class FileUtilsMock extends FileUtils {
    private FileHandle handle;

    public FileUtilsMock(FileHandle handle) {
        this.handle = handle;
    }

    @Override
    public FileHandle getFileHandle(String file, Files.FileType fileType) {
        return handle;
    }
}