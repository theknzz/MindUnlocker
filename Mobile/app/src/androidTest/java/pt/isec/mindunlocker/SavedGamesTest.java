package pt.isec.mindunlocker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;


/**
 * Tests
 *
 * @author Diogo Cardoso
 */
public class SavedGamesTest {

    private String fileName = "temp";

    /**
     * TC-07
     */
    @Test
    public void loadGame() {
        //create instance of class SavesGames
        SavedGames save = new SavedGames(ApplicationProvider.getApplicationContext());

        //create instance of class GameEngine
        GameEngine gameEngine = new GameEngine();

        //create table according
        gameEngine.createTable(ApplicationProvider.getApplicationContext(), 0);

        //create another instance of class GameEngine to compare with previous
        GameEngine gameEngine1 = new GameEngine();

        //create table according
        gameEngine1.createTable(ApplicationProvider.getApplicationContext(), 2);

        assertTrue(gameEngine1.getLevel() != save.loadGame( fileName).getLevel());
    }

    /**
     * TC-03
     */

    @Test
    public void saveGame() {
        SavedGames save = new SavedGames(ApplicationProvider.getApplicationContext());

        GameEngine gameEngine = new GameEngine();

        gameEngine.createTable(ApplicationProvider.getApplicationContext(), 0);

        assertTrue(save.saveGame(gameEngine, fileName));
    }
}