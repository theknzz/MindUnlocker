package pt.isec.mindunlocker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

import static junit.framework.TestCase.assertTrue;

public class SudokuCheckerTest{
    private int[][] solutionTable;
    private int[][] gameTable;

    @Test
    public void checkInvalidPlacements() {
        solutionTable = SudokuGenerator.getInstance().generateTable();

        gameTable = SudokuGenerator.getInstance().removeElements(solutionTable, 0);

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuCell.getInstance()[x][y] = new SudokuCell(ApplicationProvider.getApplicationContext());
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

        int xPos = 0, yPos = 0;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if(gameTable[x][y] == 0){
                    xPos = x;
                    yPos = y;
                    break;
                }
            }
        }

        System.out.println(xPos + " " + yPos);

        for (int x = 0; x < 9; x++) {
            System.out.println();
            for (int y = 0; y < 9; y++) {
                System.out.print(gameTable[x][y] + " ");
            }
        }

        assertTrue(SudokuChecker.getInstance().checkSudokuPlay(SudokuCell.getInstance(), 5, xPos,yPos));
    }
}