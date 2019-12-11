package pt.isec.mindunlocker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import java.util.List;

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

    @Test
    public void testNumberPlacement() {

        // get a GameEngine instance
        GameEngine engine = GameEngine.getInstance();
        // get an application context
        Context context = ApplicationProvider.getApplicationContext();

        // create a test table with easy difficulty
        engine.createTable(context, 0);
        // get an editable cell coordinates
        List<Integer> cellCoords = engine.getEditableCell();
        engine.setSelectedPosition(cellCoords.get(0), cellCoords.get(1));

        // set the number to be placed has 1
        engine.setNumber(1);
        // place the number
        engine.setItem(context);

        assertEquals(engine.getValueIn(cellCoords.get(0), cellCoords.get(1)), 1);
    }

    @Test
    public void testEraseNumber() {

        // get a GameEngine instance
        GameEngine engine = GameEngine.getInstance();
        // get an application context
        Context context = ApplicationProvider.getApplicationContext();

        // create a test table with easy difficulty
        engine.createTable(context, 0);
        // get an editable cell coordinates and update the GameEngine with her coordinates
        List<Integer> cellCoords = engine.getEditableCell();
        engine.setSelectedPosition(cellCoords.get(0), cellCoords.get(1));

        // fill the cell with a number
        engine.setNumber(1);
        // place the number
        engine.setItemWithNoValidation(context);

        assertEquals(engine.getValueIn(cellCoords.get(0), cellCoords.get(1)), 1);

        // set the number to be placed has 0 (= erase)
        engine.setNumber(0);
        // place the number
        engine.setItemWithNoValidation(context);

        assertEquals(engine.getValueIn(cellCoords.get(0), cellCoords.get(1)), 0);
    }


}