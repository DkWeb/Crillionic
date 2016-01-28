package de.dkweb.crillionic.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.model.Highscore;

import java.io.*;

/**
 * Created by dirkweber
 */
public class JsonManager {
    private Gson gson;

    public JsonManager() {
        gson = new Gson();
    }

    public LevelMap loadLevelMap(InputStream inputStream) {
        return gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), LevelMap.class);
    }

    public void saveHighscore(Highscore highscore, OutputStream outputStream) {
        PrintWriter printWriter = null;
        String json = gson.toJson(highscore);
        try {
            printWriter = new PrintWriter(outputStream);
            printWriter.write(json);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public Highscore loadHighscore(InputStream inputStream) {
        return gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), Highscore.class);
    }
}
