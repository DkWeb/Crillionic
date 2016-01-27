package de.dkweb.crillionic.model;

/**
 * Created by dirkweber
 */
public class GameStatistics {
    private int score;
    private int level;
    private int lifes;

    public GameStatistics(int score, int level, int lifes) {
        this.score = score;
        this.level = level;
        this.lifes = lifes;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public int getLifes() {
        return lifes;
    }

    public void increaseScore(int increment) {
        score += increment;
    }

    public void decreaseLifes() {
        lifes--;
    }
}
