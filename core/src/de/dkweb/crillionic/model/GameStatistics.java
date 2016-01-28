package de.dkweb.crillionic.model;

/**
 * Created by dirkweber
 */
public class GameStatistics {
    private int score;
    private int level;
    private int lifes;
    /**
     * The remaining color blocks for the current level
     */
    private int remainingColorBlocks;

    public GameStatistics(int score, int level, int lifes, int remainingColorBlocks) {
        this.score = score;
        this.level = level;
        this.lifes = lifes;
        this.remainingColorBlocks = remainingColorBlocks;
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

    public int getRemainingColorBlocks() {
        return remainingColorBlocks;
    }

    public void increaseScore(int increment) {
        score += increment;
    }

    public void decreaseLifes() {
        lifes--;
    }

    public void decreaseRemainingColorBlocks() {
        remainingColorBlocks--;
    }

    public void increaseRemainingColorBlocks() {
        remainingColorBlocks++;
    }
}
