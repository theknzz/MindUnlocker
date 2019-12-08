package pt.isec.mindunlocker;

/**
 * File: SudokuGenerator.java
 * @author João Páris
 * Data: 5/12/2019
 * Course: GPS - Gestão de Projeto de Software
 * Desc: Program that generates a complete Sudoku table.
 * Usage: Generates a complete Sudoku table. Return a table to play and a solution.
 */

import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {

    private static SudokuGenerator instance;            //Needs intance because anothers class to
                                                        // call methods
    private ArrayList<ArrayList<Integer>> available;    //ArrayList with numbers to fill the
                                                        // sudoku table
    private Random rand;                                //Value random


    public SudokuGenerator() {
        available = new ArrayList<>();
        rand = new Random();
    }

    /**
     * Function return instance of SudokuGenerator, if it exists, if does not create an object
     * @return instance
     */
    public static SudokuGenerator getInstance(){
        if(instance == null){
            instance = new SudokuGenerator();
        }
        return instance;
    }

    /**
     * Fill a bidimensional array of integer with numbers, creating a complete and valid sudoku
     * table
     * @return int[][] : full sudoku table
     */
    public int[][] generateTable(){
        int[][] sudokuTable = new int[9][9];
        int currPos = 0;

        while( currPos < 81){
            if(currPos == 0){
                clearTable(sudokuTable);
            }

            if(available.get(currPos).size() != 0){
                int i = rand.nextInt(available.get(currPos).size());
                int number = available.get(currPos).get(i);

                if(!hasConflict(sudokuTable, currPos, number)){
                    int posX = currPos % 9;
                    int posY = currPos / 9;

                    sudokuTable[posX][posY] = number;

                    available.get(currPos).remove(i);

                    currPos++;
                }else{
                    available.get(currPos).remove(i);
                }

            }else{
                for (int i = 1; i <= 9; i++){
                    available.get(currPos).add(i);
                }
                currPos--;
            }
        }
        return sudokuTable;
    }

    /**
     * Clear table sent by parameter, putting all numbers to 0 and
     * reset <code>available</code>, creating a new <code>ArrayList</code> and adding default
     * numbers (1 to 9)
     * @param sudokuTable table to clear
     */
    private void clearTable(int[][] sudokuTable) {
        available.clear();

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                sudokuTable[j][i] = 0;
            }
        }

        for (int spot = 0; spot < 81; spot++){
            available.add(new ArrayList<>());
            for (int n = 1; n <= 9; n++) {
                available.get(spot).add(n);
            }
        }
    }

    /**
     * Verify if the <code>sudokuTable</code> (table send by argument) has any conflicts (if the
     * <code>number</code>
     * doesn't exist duplicate in row, column or square 3 x 3)
     * @param sudokuTable int [][] : table to verify
     * @param currPos int : current position
     * @param number int : number to check
     * @return boolean : return <code>true</code> if there is conflict and <code>false</code> if
     * not
     */
    private boolean hasConflict(int[][] sudokuTable, int currPos, final int number){
        int posX = currPos % 9;
        int posY = currPos / 9;

        return checkHorizontal(sudokuTable, number, posX, posY) || checkVertical(sudokuTable,
                number, posX, posY) || check3x3square(sudokuTable, number, posX, posY);
    }

    /**
     * Verify if the <code>sudokuTable</code> (table send by argument) has any conflicts on the
     * column(if the <code>number</code>
     * doesn't exist duplicate)
     * @param sudokuTable int [][] : table to verify
     * @param number int : current position
     * @param posX int : position of current
     * @param posY int : position of current
     * @return boolean : return <code>true</code> if there is conflict and <code>false</code> if
     * not
     */
    private boolean checkVertical(int[][] sudokuTable, int number, int posX, int posY){
        for (int y = posY - 1; y >= 0; y--){
            if(number == sudokuTable[posX][y]){
                return true;
            }
        }
        return false;
    }

    /**
     * Verify if the <code>sudokuTable</code> (table send by argument) has any conflicts on the
     * row(if the <code>number</code>
     * doesn't exist duplicate)
     * @param sudokuTable int [][] : table to verify
     * @param number int : current position
     * @param posX int : position of current
     * @param posY int : position of current
     * @return boolean : return <code>true</code> if there is conflict and <code>false</code> if
     * not
     */
    private boolean checkHorizontal(int[][] sudokuTable, int number, int posX, int posY){
        for (int x = posX - 1; x >= 0; x--){
            if(number == sudokuTable[x][posY]){
                return true;
            }
        }
        return false;
    }

    /**
     * Verify if the <code>sudokuTable</code> (table send by argument) has any conflicts on the
     * square(if the <code>number</code>
     * doesn't exist duplicate)
     * @param sudokuTable int [][] : table to verify
     * @param number int : current position
     * @param posX int : position of current
     * @param posY int : position of current
     * @return boolean : return <code>true</code> if there is conflict and <code>false</code> if
     * not
     */
    private boolean check3x3square(int[][] sudokuTable, int number, int posX, int posY){
        int xRegion = posX / 3;
        int yRegion = posY / 3;

        for (int x = xRegion * 3; x < xRegion * 3 + 3; x++){
            for (int y = yRegion * 3; y < yRegion * 3 + 3; y++){
                if( ( x != posX || y != posY) && number == sudokuTable[x][y]){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Function to remove some numbers depending on the level, setting 0 to remove
     * @param sudokuTable int [][] : table to remove numbers
     * @param level int : difficulty of the game (0 - 2)
     * @return table with some numbers removed
     */
    public int[][] removeElements( int[][] sudokuTable, int level){
        int i = 0, n;

        switch (level){
            case 0: n = 30;break;
            case 2: n = 60;break;
            default: n = 40; // if the level is not 0, 1 or 2 the level is default = 2, ie n = 40;
        }
        //n=1; //Debug

        while( i < n ){
            int x = rand.nextInt(9);
            int y = rand.nextInt(9);

            if( sudokuTable[x][y] != 0 ){
                sudokuTable[x][y] = 0;
                i++;
            }
        }
        return sudokuTable;
    }
}
