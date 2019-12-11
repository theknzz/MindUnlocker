package pt.isec.mindunlocker;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

import static junit.framework.TestCase.assertTrue;


/**
 * Tests
 *
 * @author Diogo Cardoso
 */
public class SudokuCheckerTest{
    private int[][] solutionTable;
    private int[][] gameTable;


    /**
     * TC-04
     */
    @Test
    public void checkInvalidPlacements() {

        for(int i = 0; i < 10; i++) {
            //create a random table solution
            solutionTable = SudokuGenerator.getInstance().generateTable();

            //remove elements according level
            gameTable = SudokuGenerator.getInstance().removeElements(solutionTable, 0);

            //generate cells for interface
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    SudokuCell.getInstance()[x][y] = new SudokuCell(ApplicationProvider.getApplicationContext());
                }
            }

            //set values for cells
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    SudokuCell.getInstance()[x][y].setInitValue(gameTable[x][y]);
                    if (gameTable[x][y] != 0) {
                        SudokuCell.getInstance()[x][y].setNotModifiable();
                    }
                }
            }

            int xPos = 0, yPos = 0;


            // select random pos
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    if (gameTable[x][y] == 0) {
                        xPos = x;
                        yPos = y;
                        break;
                    }
                }
            }

            //see position
            System.out.println("Pos: " + xPos + " " + yPos);

            //see table
            for (int x = 0; x < 9; x++) {
                System.out.println();
                for (int y = 0; y < 9; y++) {
                    System.out.print(gameTable[x][y] + " ");
                }
            }

            System.out.println();

            try {
                assertTrue(SudokuChecker.getInstance().checkSudokuPlay(SudokuCell.getInstance(), 5, xPos, yPos));
            }catch (AssertionError e){
                System.out.println(e.toString());
            }
        }
    }
}