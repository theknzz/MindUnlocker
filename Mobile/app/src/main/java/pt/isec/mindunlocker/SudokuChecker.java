package pt.isec.mindunlocker;

import android.util.Log;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class SudokuChecker {
    private static SudokuChecker instance;

    private SudokuChecker(){}


    /**
     * Creates instance if needed and returns it
     * @return <code>SudokuChecker</code> instance
     */
    public static SudokuChecker getInstance(){
        if( instance == null ){
            instance = new SudokuChecker();
        }
        return instance;
    }


    /**
     * Check if table is complete and correct
     * @param sudokuTable <code>SudokuCell[][]</code>
     * @return <code>boolean</code> <code>true</code> if table is complete and valid,
     * <code>false</code> if not
     */
    public boolean checkSudoku( SudokuCell[][] sudokuTable){
        return (checkHorizontal(sudokuTable) || checkVertical(sudokuTable) || checkRegions(sudokuTable));
    }

    /**
     * Check all table rows are completes and valid
     * @param sudokuTable
     * @return <code>boolean</code> <code>true</code> if every row in the table are valid and
     * complete (without 0), <code>false</code> if not
     */
    private boolean checkHorizontal(SudokuCell[][] sudokuTable) {
        for( int y = 0 ; y < 9 ; y++ ){
            for( int posX = 0 ; posX < 9 ; posX++ ){
                if (sudokuTable[posX][y].getValue() == 0) {
                    return false;
                }
                for (int x = posX + 1; x < 9; x++) {
                    if (sudokuTable[posX][y] == sudokuTable[x][y] || sudokuTable[x][y].getValue() == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check all table columns are completes and valid
     * @param sudokuTable
     * @return <code>boolean</code> <code>true</code> if every column in the table are valid and
     * complete (without 0), <code>false</code> if not
     */
    private boolean checkVertical(SudokuCell[][] sudokuTable) {
        for( int x = 0 ; x < 9 ; x++ ){
            for( int posY = 0 ; posY < 9 ; posY++ ){
                if (sudokuTable[x][posY].getValue() == 0) {
                    return false;
                }
                for (int y = posY + 1; y < 9; y++) {
                    if (sudokuTable[x][posY] == sudokuTable[x][y] || sudokuTable[x][y].getValue() == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check all table squares are completes and valid
     * @param sudokuTable
     * @return <code>boolean</code> <code>true</code> if every square in the table are valid and
     * complete (without 0), <code>false</code> if not
     */
    private boolean checkRegions(SudokuCell[][] sudokuTable) {
        for( int xRegion = 0; xRegion < 3; xRegion++ ){
            for( int yRegion = 0; yRegion < 3 ; yRegion++ ){
                if( !checkRegion(sudokuTable, xRegion, yRegion) ){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check table Square passed by argument
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param xRegion <code>int</code> X of square
     * @param yRegion <code>int</code> Y of square
     * @return <code>boolean</code> <code>true</code> if the square send is valid and
     * complete (without 0), <code>false</code> if not
     */
    private boolean checkRegion(SudokuCell[][] sudokuTable , int xRegion , int yRegion){
        for( int posX = xRegion * 3; posX < xRegion * 3 + 3 ; posX++ ){
            for( int posY = yRegion * 3 ; posY < yRegion * 3 + 3 ; posY++ ){
                for( int x = posX ; x < xRegion * 3 + 3 ; x++ ){
                    for( int y = posY ; y < yRegion * 3 + 3 ; y++ ){
                        if (((x != posX || y != posY) && sudokuTable[posX][posY] == sudokuTable[x][y]) || sudokuTable[x][y].getValue() == 0) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if <var>num</var> is possible placed in <var>x</var> and <var>y</var> position
     * without conflicts table
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param num <code>int</code> number to check
     * @param x <var>int</var> x index
     * @param y <var>int</var> y index
     * @return  <code>booelan</code> <code>true</code> if table valid,
     * <code>false</code> if table is invalid
     */
    public boolean checkSudokuPlay(SudokuCell[][] sudokuTable, int num, int x, int y) {
        return (checkHorizontalPlay(sudokuTable,num,x,y) || checkVerticalPlay(sudokuTable,num,x,y) || checkRegionPlay(sudokuTable,num,x,y));
        //return false;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number in the regions
     * 3 X 3
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param num <code>int</code> number to check
     * @param x <var>int</var> x index
     * @param y <var>int</var> y index
     * @return <code>boolean</code> <code>true</code> if exist conflicts in row
     */
    private boolean checkRegionPlay(SudokuCell[][] sudokuTable, int num, int x, int y) {
        int bx, by;
        for (bx = (x/3)*3; bx < (x/3)*3 + 3; bx++) {
            for (by = (y/3)*3; by < (y/3)*3 + 3; by++) {
                // bx and by will now loop over each number in the block which also contains x, y
                if( bx != x && by != y && sudokuTable[bx][by] == sudokuTable[x][y]){
                    Log.e("Conflito","nums iguais na mesma regiao");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number horizontally
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param num <code>int</code> number to check
     * @param x <var>int</var> x index
     * @param y <var>int</var> y index
     * @return <code>boolean</code> <code>true</code> if exist conflicts in row
     */
    private boolean checkHorizontalPlay(SudokuCell[][] sudokuTable, int num, int x, int y) {
        for( int k = 0 ; k < 9 ; k++ ){
            if( k != x && sudokuTable[k][y].getValue() == num){
                Log.e("Conflito","nums iguais na mesma linha");
                return true;
            }
        }
        return false;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number vertically
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param num <code>int</code> number to check
     * @param x <var>int</var> x index
     * @param y <var>int</var> y index
     * @return <code>boolean</code> <code>true</code> if exist conflicts in column
     */
    private boolean checkVerticalPlay(SudokuCell[][] sudokuTable, int num, int x,int y) {
        for( int k = 0 ; k < 9 ; k++ ){
            if( k != y && sudokuTable[x][k].getValue() == num){
                Log.e("Conflito","nums iguais na mesma coluna");
                return true;
            }
        }
        return false;
    }

    /**
     * Check if <var>number</var> is possible placed in <var>row</var> and <var>col</var> position
     * without conflicts table
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param number <code>int</code> number to check
     * @param row <var>int</var> x index
     * @param col <var>int</var> y index
     * @return
     */
    public boolean checkPositionCustom(SudokuCell[][] sudokuTable, int number, int row,int col) {
        return (checkHorizontalCustom(col, number, sudokuTable) && checkVerticalCustom(row, number, sudokuTable)
                && checkRegionsCustom(row, col, number, sudokuTable));
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number horizontally
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param number <code>int</code> number to check
     * @param col <var>int</var> x index
     * @return
     */
    private boolean checkHorizontalCustom(int col, int number, SudokuCell[][] sudokuTable) {
        for (int i = 0; i < 9; i++)
            if (sudokuTable[i][col].getValue() == number)
                return false;
        return true;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number vertically
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param number <code>int</code> number to check
     * @param row <var>int</var> row index
     * @return
     */
    private boolean checkVerticalCustom(int row, int number, SudokuCell[][] sudokuTable) {
        for (int i = 0; i < 9; i++)
            if (sudokuTable[row][i].getValue() == number)
                return false;
        return true;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number in the regions
     * 3 X 3
     * @param sudokuTable <code>SudokuCell[][]</code> table
     * @param row <code>int</code> number to check
     * @param row <var>int</var> row index
     * @param col <var>int</var> col index
     * @return
     */
    private boolean checkRegionsCustom(int row, int col, int number, SudokuCell[][] sudokuTable) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (sudokuTable[i][j].getValue() == number)
                    return false;
        return true;
    }
}