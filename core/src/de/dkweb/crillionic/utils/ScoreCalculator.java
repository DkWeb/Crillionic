package de.dkweb.crillionic.utils;

import de.dkweb.crillionic.model.GameStatistics;

/**
 * Created by dirkweber
 */
public class ScoreCalculator {
    public int getLevelScore(GameStatistics gameStatistics) {
        if (gameStatistics.getRemainingColorBlocks() == 0) {
            return gameStatistics.getScore() + calculateTimeBonus((int) gameStatistics.getRemainingTime());
        }
        return gameStatistics.getScore();
    }

    public int calculateTimeBonus(int time) {
        return time * 10;
    }
}
