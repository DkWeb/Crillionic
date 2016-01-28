package de.dkweb.crillionic.model;

import de.dkweb.crillionic.utils.GlobalConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dirkweber
 */
public class Highscore {
    private List<Integer> scores;

    public Highscore() {
        scores = new ArrayList<Integer>();
    }

    public List<Integer> getScores() {
        return scores;
    }

    public boolean addToHighscore(int score) {
        if (score <= 0) {
            return false;
        }
        if (scores.size() < GlobalConstants.MAX_HIGHSCORE_ENTRIES) {
            scores.add(score);
            return true;
        }

        // The highscore is already "full". Check if the new score is better than any existing
        for (int i = 0; i < scores.size(); i++) {
            int oldScore = scores.get(i);
            if (score > oldScore) {
                scores.add(i, score);
                scores.remove(scores.size() - 1);
                return true;
            }
        }
        return false;
    }
}

