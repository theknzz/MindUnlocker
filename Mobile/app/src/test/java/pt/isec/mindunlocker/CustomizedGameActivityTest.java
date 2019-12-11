package pt.isec.mindunlocker;

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomizedGameActivityTest {

    @Test
    public void validateStartGameWithLessCells() {
        CustomizedGameActivity activity = new CustomizedGameActivity();
        // A game of sudoku can't find is solution if it starts with less then 16 cells
        assertFalse(activity.validateStartGame(15));
    }

    @Test
    public void validateStartGameInInitialState() {
        CustomizedGameActivity activity = new CustomizedGameActivity();
        assertTrue(activity.validateStartGame(17));
    }

    @Test
    public void validateStartGameInCorrectState() {
        CustomizedGameActivity activity = new CustomizedGameActivity();
        assertTrue(activity.validateStartGame(25));
    }

    @Test
    public void validateStartGameInLimitState() {
        CustomizedGameActivity activity = new CustomizedGameActivity();
        assertTrue(activity.validateStartGame(81));
    }

    @Test
    public void validateStartGameWithMoreCells() {
        CustomizedGameActivity activity = new CustomizedGameActivity();
        // A game of sudoku can't start with 81 cells
        assertFalse(activity.validateStartGame(1000));
    }

    @Test
    public void validateStartGameInIncorrectState() {
        CustomizedGameActivity activity = new CustomizedGameActivity();
        assertFalse(activity.validateStartGame(-1));
    }


}