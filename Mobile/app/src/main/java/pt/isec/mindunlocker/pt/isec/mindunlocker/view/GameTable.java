package pt.isec.mindunlocker.pt.isec.mindunlocker.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import pt.isec.mindunlocker.GameEngine;
import pt.isec.mindunlocker.GameplayActivity;
import pt.isec.mindunlocker.SudokuChecker;

public class GameTable implements Serializable {

    private GameEngine gameEngine = GameEngine.getInstance();

    private boolean isPencil = false;

    public boolean isFinish() {
        return finish;
    }

    private boolean finish = false;

    public GameTable(Context context) {
//        this.context = context;

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuCell.getInstance()[x][y] = new SudokuCell(context);
            }
        }
    }

    public void setTable(int[][] table) {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                SudokuCell.getInstance()[x][y].setInitValue(table[x][y]);
                if (table[x][y] != 0) {
                    SudokuCell.getInstance()[x][y].setNotModifiable();
                }
            }
        }
    }

    public SudokuCell[][] getTable() {
        return SudokuCell.getInstance();
    }

    public SudokuCell getItem(int x, int y) {
        return SudokuCell.getInstance()[x][y];
    }

    public SudokuCell getItem(int position) {
        int x = position % 9;
        int y = position / 9;
        return SudokuCell.getInstance()[x][y];
    }

    public void setItem(int x, int y, int number, Context context) {
            SudokuCell selectedCell = getItem(x, y);
            if (!selectedCell.isModifiable())  {
                selectedCell.setGuess(false);
                return;
            }
            if (isPencil) {
                // enables the pencil mode
                selectedCell.setGuess(true);
                selectedCell.setValue(number);
            } else {
                // reset the pencil mode
                selectedCell.setGuess(false);
                // if table has a wrong cell and the user is not changing that cell ignore the input
                if (tableHasWrongCell() && !selectedCell.isWrong()) {
                    Toast.makeText(context, "You may change the wrong cell first!", Toast.LENGTH_LONG).show();
                    return;
                }
                SudokuCell.getInstance()[x][y].setValue(number);
                //clearPos();
                if (checkGame()) {
                    GameEngine.getInstance().finalScore();
                    finish = true;
                    } else if (number != 0) {
                        selectedCell.setWrong(false);
                        if (SudokuChecker.getInstance().checkSudokuPlay(getTable(), number, x, y)) {
                            GameEngine.getInstance().incorrectPlay();
                            selectedCell.setWrong(true);
                            //Toast.makeText(context, "Conflict!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        selectedCell.setWrong(false);
                        GameEngine.getInstance().correctPlay();
                }
            }
    }

    public void setItemWithNoValidation(int x, int y, int n) {
        SudokuCell selectedCell = getItem(x, y);
        if (!selectedCell.isModifiable())  {
            selectedCell.setGuess(false);
            return;
        }
        selectedCell.setValue(n);
    }

    public void setItemCustom(int x, int y, int number, Context context) {
        if (number == 0)
            SudokuCell.getInstance()[x][y].setValue(number);
        else {
            if (SudokuChecker.getInstance().checkPositionCustom(getTable(), number, x, y))
                SudokuCell.getInstance()[x][y].setValue(number);
            else
                Toast.makeText(context, "You can't put " + number + " here", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean setCustomItemIfValid(int x, int y, int number) {
        if (number == 0) {
            SudokuCell.getInstance()[x][y].setValue(number);
            return true;
        } else {
            if (SudokuChecker.getInstance().checkPositionCustom(getTable(), number, x, y)) {
                SudokuCell.getInstance()[x][y].setValue(number);
                return true;
            }
        }
        return false;
    }

    public void setPencilMode(boolean val) {
        isPencil = val;
    }

    public boolean checkGame() {
        if (SudokuChecker.getInstance().checkSudoku(getTable())) {
            //Toast.makeText(context, "Congratulations! You've solved the puzzle!", Toast.LENGTH_LONG).show();
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

    /**
     * Method the checks every table's cell, to see if the table has any wrong cell
     * @return if table has a wrong cell? true : false
     */
    private boolean tableHasWrongCell() {
        for (int y = 0; y < 9; y++)
            for (int x = 0; x < 9; x++)
                if (SudokuCell.getInstance()[x][y].isWrong())
                    return true;
        return false;
    }

    /**
     * Method returns the coordinates of an editable cell
     * @return List of two integers with the editable cell coordinates
     */
    public List<Integer> getEditableCell() {
        SudokuCell cell = null;
        List<Integer> list = new ArrayList<>();
        int x, y;
        do {
            x = (int) (Math.random() * 9);
            y = (int) (Math.random() * 9);

            cell = SudokuCell.getInstance()[x][y];

        } while (!cell.isModifiable());
        list.add(x);
        list.add(y);
        return list;
    }

    /**
     * Get the value inside the x and y coordinates of a cell
     * @param x x cell's coordinate
     * @param y y cell's coordinate
     * @return cell's value or -1 if coordinates are invalid
     */
    public int getValueIn(int x, int y) {
        if (x<0 || x>9) return -1;
        return SudokuCell.getInstance()[x][y].getValue();
    }
}
