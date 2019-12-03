package pt.isec.mindunlocker;

import android.content.Context;
import android.widget.Toast;

import java.util.Arrays;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class GameEngine {
    private static GameEngine instance;
    private GameTable table = null;

    private int[][] solutionTable = new int[9][9];
    private int[][] gameTable = new int[9][9];

    private int selectedPosX = -1;
    private int selectedPosY = -1;

    public GameEngine() {
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    private void copieTable() {
        gameTable = Arrays.copyOf(solutionTable, 9);
    }

    public void createTableToPlay(Context context) {
        if (solutionTable[0][0] == 0) {
            solutionTable = SudokuGenerator.getInstance().generateTable();
            copieTable();
            gameTable = SudokuGenerator.getInstance().removeElements(gameTable, 0);
            table = new GameTable(context);
            table.setTable(gameTable);
        }
    }

    public void createTableWithVars(int[][] solutionTable) {
        this.solutionTable = solutionTable;
    }

    public void createTableEmpty(Context context) {
        copieTable();
        table = new GameTable(context);
    }

    public GameTable getTable() {
        return table;
    }

    public int getSolutionTable(int x, int y) {
        return solutionTable[x][y];
    }

    public void setSelectedPosition(int x, int y) {
        this.selectedPosX = x;
        this.selectedPosY = y;
    }

    public void setNumber(int number) {
        if (selectedPosX != -1 && selectedPosY != -1) {
            table.setItem(selectedPosX, selectedPosY, number);
        }
        if (table.checkGame()) {
            //WINS POP_UP;
        }
    }

//==============
    public void setNumberCustom(int number, Context context) {
        if (selectedPosX != -1)
            if (number != 0) {
                if (checkSudokuCustom(selectedPosX, selectedPosY, number, table.getTable())) {
                    table.setItem(selectedPosX, selectedPosY, number);
                } else {
                    Toast.makeText(context, "You can not put the " + number + " there", Toast.LENGTH_SHORT).show();
                }
            } else
                table.setItem(selectedPosX, selectedPosY, number);

    }

    public boolean checkSudokuCustom(int row, int col, int number, SudokuCell[][] sudokuTable) {
        return (checkHorizontalCustom(col, number, sudokuTable) && checkVerticalCustom(row, number, sudokuTable)
                && checkRegionsCustom(row, col, number, sudokuTable));
    }

    private boolean checkHorizontalCustom(int col, int number, SudokuCell[][] sudokuTable) {
        for (int i = 0; i < 9; i++)
            if (sudokuTable[i][col].getValue() == number)
                return false;
        return true;
    }

    private boolean checkVerticalCustom(int row, int number, SudokuCell[][] sudokuTable) {
        for (int i = 0; i < 9; i++)
            if (sudokuTable[row][i].getValue() == number)
                return false;
        return true;
    }

    private boolean checkRegionsCustom(int row, int col, int number, SudokuCell[][] sudokuTable) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (sudokuTable[i][j].getValue() == number)
                    return false;
        return true;
    }

    public int NFillCells() {
        return table.fillCells();
    }
}
