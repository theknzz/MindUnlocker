package pt.isec.mindunlocker.pt.isec.mindunlocker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import pt.isec.mindunlocker.GameEngine;
import pt.isec.mindunlocker.SudokuChecker;

public class GameTable {
    private SudokuCell[][] SudokuTable = new SudokuCell[9][9];

    private Context context;

    public boolean isFinish() {
        return finish;
    }

    private boolean finish = false;

    public GameTable(Context context) {
        this.context = context;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuTable[x][y] = new SudokuCell(context);
            }
        }
    }

    public void setTable(int[][] table) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuTable[x][y].setInitValue(table[x][y]);
                if (table[x][y] != 0) {
                    SudokuTable[x][y].setNotModifiable();
                }
            }
        }
    }

    public SudokuCell[][] getTable() {
        return SudokuTable;
    }

    public SudokuCell getItem(int x, int y) {
        return SudokuTable[x][y];
    }

    public SudokuCell getItem(int position) {
        int x = position % 9;
        int y = position / 9;
        return SudokuTable[x][y];
    }

    public void setItem(int x, int y, int number) {
        SudokuTable[x][y].setValue(number);
        SudokuCell selectedCell = getItem(x, y);
        //clearPos();
        if (checkGame()) {
            finish = true;
        } else if (number != 0) {
           selectedCell.setWrong(false);
            if (SudokuChecker.getInstance().checkSudokuPlay(getTable(), number, x, y)) {
                selectedCell.setWrong(true);
                Toast.makeText(context, "Conflict!", Toast.LENGTH_SHORT).show();
            }
        } else
            selectedCell.setWrong(false);
    }

    public void setItemCustom(int x, int y, int number) {
        if (number == 0)
            SudokuTable[x][y].setValue(number);
        else {
            if (SudokuChecker.getInstance().checkPositionCustom(getTable(), number, x, y))
                SudokuTable[x][y].setValue(number);
            else
                Toast.makeText(context, "You can't put " + number + " here", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPencilMode(boolean val) {
        //SudokuTable[0][0].setGuess(val);
    }

    public boolean checkGame() {
        if (SudokuChecker.getInstance().checkSudoku(getTable())) {
            Toast.makeText(context, "Congratulations! You've solved the puzzle!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public int fillCells() {
        int count = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (getItem(x, y).getValue() != 0) {
                    ++count;
                }
            }
        }
        return count;
    }

}
