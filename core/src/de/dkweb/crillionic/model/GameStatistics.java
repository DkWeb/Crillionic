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
