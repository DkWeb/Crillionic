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
package de.dkweb.crillionic.model;

import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Highscore {
    private List<Integer> scores;

    public Highscore() {
        scores = new ArrayList<Integer>();
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void ensureEntryLimit(int maxEntries) {
        if (scores.size() > maxEntries) {
            scores = scores.subList(0, maxEntries);
        }
    }

    public boolean isInHigshscore(int score) {
        if (score <= 0) {
            return false;
        }

        if (scores.size() < GlobalConstants.MAX_HIGHSCORE_ENTRIES) {
            return true;
        }

        // Check, if the achieved score is better than the worst score of the highscore
        return scores.get(scores.size() - 1) < score;
    }

    public boolean addToHighscore(int score) {
        if (score <= 0) {
            return false;
        }

        // Add the score to the highscore at the appropriate position.
        // If this would be at a position > GlobalConstants.MAX_HIGHSCORE_ENTRIES
        // the score was not good enough
        boolean added = false;
        for (int i = 0; i < scores.size(); i++) {
            int oldScore = scores.get(i);
            if (score > oldScore) {
                scores.add(i, score);
                added = true;
                break;
            }
        }
        // If we did not find a appropriate position for inserting the value,
        // but the list is not fully filled, we can simply add the value at
        // the end of the list
        if (!added && scores.size() < GlobalConstants.MAX_HIGHSCORE_ENTRIES) {
            scores.add(score);
            added = true;
        }
        if (scores.size() > GlobalConstants.MAX_HIGHSCORE_ENTRIES) {
            scores.remove(GlobalConstants.MAX_HIGHSCORE_ENTRIES);
        }
        return added;
    }
}

