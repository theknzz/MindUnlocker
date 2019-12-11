package pt.isec.mindunlocker;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.junit.Test;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

import static org.junit.Assert.*;

/**
 * Unit Tests
 *
 * @author Joao Paris
 * @author Henrique Dias
 */
public class UnitTest {


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


    /**
     * TC-11 Verify game creation difficulty
     */
    private int testGameCreation(int numCells){
        CustomizedGameActivity customizedGameActivity = new CustomizedGameActivity();

        return customizedGameActivity.setLevel(numCells);
    }


    /**
     * Tests if the difficulty given is correct
     */
    @Test
    public void VerifyCreationDiffilcity(){
        assertEquals(-1, testGameCreation(16));  //Impossible
        assertEquals(2, testGameCreation(17));  //Hard (Minimum possible)
        assertEquals(2, testGameCreation(20));  //Hard
        assertEquals(2, testGameCreation(40));  //Hard
        assertEquals(1, testGameCreation(41));  //Medium
        assertEquals(1, testGameCreation(42));  //Medium
        assertEquals(1, testGameCreation(50));  //Medium
        assertEquals(0, testGameCreation(51));  //Easy
        assertEquals(0, testGameCreation(52));  //Easy
        assertEquals(-1, testGameCreation(82));  //Impossible
    }

}