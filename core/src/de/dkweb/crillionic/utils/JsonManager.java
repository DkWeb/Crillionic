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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.dkweb.crillionic.map.LevelMap;
import de.dkweb.crillionic.model.Highscore;

import java.io.*;

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
        Highscore highscore = gson.fromJson(new BufferedReader(new InputStreamReader(inputStream)), Highscore.class);
        // Make sure that the highscore has no more than the (currently) allowed maximum number of entries
        highscore.ensureEntryLimit(GlobalConstants.MAX_HIGHSCORE_ENTRIES);
        return highscore;
    }
}
