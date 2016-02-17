package de.dkweb.crillionic.utils;

import de.dkweb.crillionic.model.Highscore;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dirkweber
 */
public class HighscoreTest {
    @Test
    public void isInHigshscoreFailed() {
        Highscore highscore = new Highscore();
        int baseScore = 100;
        for (int i = 0; i < GlobalConstants.MAX_HIGHSCORE_ENTRIES; i++) {
            highscore.addToHighscore((i + 1) * baseScore);
        }
        assertFalse(highscore.isInHigshscore(0));
    }

    @Test
    public void isInHigshscoreSuccess() {
        Highscore highscore = new Highscore();
        assertTrue(highscore.isInHigshscore(100));
    }

    @Test
    public void isInHigshscoreSuccessByReplace() {
        Highscore highscore = new Highscore();
        int baseScore = 100;
        for (int i = 0; i < GlobalConstants.MAX_HIGHSCORE_ENTRIES; i++) {
            highscore.addToHighscore((i + 1) * baseScore);
        }
        assertTrue(highscore.isInHigshscore(baseScore * GlobalConstants.MAX_HIGHSCORE_ENTRIES));
    }


}
