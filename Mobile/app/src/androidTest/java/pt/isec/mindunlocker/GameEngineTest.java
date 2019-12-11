package pt.isec.mindunlocker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameEngineTest {

    @Test
    public void testInsertInTheSameRow() {
        GameEngine engine = GameEngine.getInstance();

        // create an empty table
        engine.createTableEmpty(ApplicationProvider.getApplicationContext());

        // to set the cursor to the specific cell
        engine.setSelectedPosition(0,0);
        // to set the number to be used to fill the cell
        engine.setNumber(1);
        // insert the number into the table
        engine.setCustomItemIfValid();

        // to set the cursor to the cell that will break the rule
        engine.setSelectedPosition(5,0);
        assertFalse(engine.setCustomItemIfValid());
    }

    @Test
    public void testInsertInTheSameColumn() {
        GameEngine engine = GameEngine.getInstance();

        // create an empty table
        engine.createTableEmpty(ApplicationProvider.getApplicationContext());

        // to set the cursor to the specific cell
        engine.setSelectedPosition(0,0);
        // to set the number to be used to fill the cell
        engine.setNumber(1);
        // insert the number into the table
        engine.setCustomItemIfValid();

        // to set the cursor to the cell that will break the rule
        engine.setSelectedPosition(0,5);
        assertFalse(engine.setCustomItemIfValid());
    }

    @Test
    public void testInsertInTheSameBlock() {
        GameEngine engine = GameEngine.getInstance();

        // create an empty table
        engine.createTableEmpty(ApplicationProvider.getApplicationContext());

        // to set the cursor to the specific cell
        engine.setSelectedPosition(0,0);
        // to set the number to be used to fill the cell
        engine.setNumber(1);
        // insert the number into the table
        engine.setCustomItemIfValid();

        // to set the cursor to the cell that will break the rule
        engine.setSelectedPosition(2,2);
        assertFalse(engine.setCustomItemIfValid());
    }

}