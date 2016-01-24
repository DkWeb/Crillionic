package de.dkweb.crillionic.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.dkweb.crillionic.map.LevelMap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
