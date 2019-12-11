package pt.isec.mindunlocker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class GameplayActivityTest {

    @Test
    public void saveGame() {
        SavedGames save = new SavedGames(ApplicationProvider.getApplicationContext());

        GameEngine gameEngine = new GameEngine();

        gameEngine.createTable(ApplicationProvider.getApplicationContext(), 0);

        assertTrue(save.saveGame(gameEngine, "temp.txt"));
    }
}