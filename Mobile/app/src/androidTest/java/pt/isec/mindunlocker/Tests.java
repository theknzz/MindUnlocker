package pt.isec.mindunlocker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

import static org.junit.Assert.*;

/**
 * Tests
 *
 * @author Joao Paris
 */
@RunWith(AndroidJUnit4.class)
public class Tests {

    /**
     * TC-10 Verify if erase can remove original game values
     */
    @Test
    public void testEraseOriginal() {
        GameEngine engine = GameEngine.getInstance();
        Context context = ApplicationProvider.getApplicationContext();

        engine.createTable(context,0);
        engine.setSelectedPosition(engine.getOriginalCell());

        int x = engine.getSelectedPosX();
        int y = engine.getSelectedPosY();
        engine.setNumber(0);    //zero is for erase
        engine.setItem(context);

        assertFalse(engine.getValueIn(x,y)==0);
        //assertTrue(engine.getValueIn(x,y)==0); //isn't supposed to pass this test
    }

}
