package pt.isec.mindunlocker;

import android.util.Log;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class SudokuChecker {
    private static SudokuChecker instance;

    private SudokuChecker(){}

    public static SudokuChecker getInstance(){
        if( instance == null ){
            instance = new SudokuChecker();
        }
        return instance;
    }

    public boolean checkSudoku( SudokuCell[][] sudokuTable){
        return (checkHorizontal(sudokuTable) || checkVertical(sudokuTable) || checkRegions(sudokuTable));
    }

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

    public boolean checkSudokuPlay(SudokuCell[][] sudokuTable, int num, int x, int y) {
        return (checkHorizontalPlay(sudokuTable,num,x,y) || checkVerticalPlay(sudokuTable,num,x,y) || checkRegionPlay(sudokuTable,num,x,y));
        //return false;
    }

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

    private boolean checkHorizontalPlay(SudokuCell[][] sudokuTable, int num, int x, int y) {
        for( int k = 0 ; k < 9 ; k++ ){
            if( k != x && sudokuTable[k][y].getValue() == num){
                Log.e("Conflito","nums iguais na mesma linha");
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalPlay(SudokuCell[][] sudokuTable, int num, int x,int y) {
        for( int k = 0 ; k < 9 ; k++ ){
            if( k != y && sudokuTable[x][k].getValue() == num){
                Log.e("Conflito","nums iguais na mesma coluna");
                return true;
            }
        }
        return false;
    }

    public boolean checkPositionCustom(SudokuCell[][] sudokuTable, int number, int row,int col) {
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
}