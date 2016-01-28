package de.dkweb.crillionic.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.model.Highscore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dirkweber
 */
public class HighscoreManager {
    public boolean addEntry(int achievedScore, JsonManager jsonManager) {
        if (achievedScore == 0) {
            return false;
        }
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
