package pt.isec.mindunlocker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class SavedGamesTest {

    @Test
    public void loadGame() {
        SavedGames save = new SavedGames(ApplicationProvider.getApplicationContext());

        GameEngine gameEngine = new GameEngine();

        gameEngine.createTable(ApplicationProvider.getApplicationContext(), 0);

        GameEngine gameEngine1 = new GameEngine();

        gameEngine1.createTable(ApplicationProvider.getApplicationContext(), 2);

        assertTrue(gameEngine1.getLevel() == save.loadGame( "temp.txt").getLevel());
    }

    @Test
    public void saveGame() {
        SavedGames save = new SavedGames(ApplicationProvider.getApplicationContext());

        GameEngine gameEngine = new GameEngine();

        gameEngine.createTable(ApplicationProvider.getApplicationContext(), 0);

        assertTrue(save.saveGame(gameEngine, "temp1"));
    }
}