package pt.isec.mindunlocker.pt.isec.mindunlocker.view;

import androidx.appcompat.app.AppCompatActivity;

import org.junit.Test;

import java.lang.reflect.Method;

import pt.isec.mindunlocker.CustomizedGameActivity;
import pt.isec.mindunlocker.GameplayActivity;
import pt.isec.mindunlocker.MainActivity;
import pt.isec.mindunlocker.SudokuChecker;
import pt.isec.mindunlocker.SudokuGenerator;

import static org.junit.Assert.assertEquals;

public class SudokuCheckerTest{
    private int[][] solutionTable;
    private int[][] gameTable;

    @Test
    public void checkInvalidPlacements() {
        solutionTable = SudokuGenerator.getInstance().generateTable();

        gameTable = SudokuGenerator.getInstance().removeElements(solutionTable, 0);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuCell.getInstance()[x][y] = new SudokuCell();
            }
        }

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuCell.getInstance()[x][y].setInitValue(gameTable[x][y]);
                if (gameTable[x][y] != 0) {
                    SudokuCell.getInstance()[x][y].setNotModifiable();
                }
            }
        }

        SudokuCell.getInstance()[0][0].setValue(5);

        assertEquals(false, SudokuChecker.getInstance().checkSudokuPlay(SudokuCell.getInstance(), 5, 0,0));
    }
}