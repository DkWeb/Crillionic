package de.dkweb.crillionic.utils;

import de.dkweb.crillionic.model.GameStatistics;

/**
 * Created by dirkweber
 */
public class ScoreCalculator {
    public int getLevelScore(GameStatistics gameStatistics) {
        return gameStatistics.getScore() + calculateTimeBonus((int) gameStatistics.getRemainingTime());
    }

    public int calculateTimeBonus(int time) {
        return time * 10;
    }
}
