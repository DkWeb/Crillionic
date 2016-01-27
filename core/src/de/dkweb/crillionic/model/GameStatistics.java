package de.dkweb.crillionic.model;

/**
 * Created by dirkweber
 */
public class GameStatistics {
    private int score;
    private int level;

    public GameStatistics(int score, int level) {
        this.score = score;
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public void increaseScore(int increment) {
        score += increment;
    }
}
