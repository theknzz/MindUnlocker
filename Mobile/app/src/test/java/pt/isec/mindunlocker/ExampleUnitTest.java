package pt.isec.mindunlocker;

import android.app.Application;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    /**
     * Tests a game given its dificulty
     * @param dificulty game dificulty
     * @return
     */
    private int testScore(int dificulty) {
        GameEngine gameEngine = new GameEngine();

        //definir o nivel
        gameEngine.setLevel(dificulty);

        int nHints = 0;

        switch (dificulty) {
            case 0: {
                nHints = 30;
                break;
            }
            case 1: {
                nHints = 40;
                break;
            }
            case 2: {
                nHints = 60;
                break;
            }
        }

        // takes the number of hints needed to finish the game
        for (int i = 0; i < nHints; i++) {
            gameEngine.tookHint();
        }

        //define time
        gameEngine.setFinalTime(0, 30);
        gameEngine.setTimeSpent();

        //add victory score
        gameEngine.levelScoreAdded();

        return gameEngine.getScore();
    }

    /**
     * Tests if the test score calculation is correct
     */
    @Test
    public void ScoreIsCorrect() {
        assertEquals(-400, testScore(0));
        assertEquals(-750, testScore(1));
        assertEquals(-1500, testScore(2));
    }

}