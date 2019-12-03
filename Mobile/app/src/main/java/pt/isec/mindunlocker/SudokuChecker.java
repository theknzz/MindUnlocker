package pt.isec.mindunlocker;

import android.util.Log;

public class SudokuChecker {
    private static SudokuChecker instance;

    private SudokuChecker(){}

    public static SudokuChecker getInstance(){
        if( instance == null ){
            instance = new SudokuChecker();
        }
        return instance;
    }

    public boolean checkSudoku( int[][] sudokuTable){
        return (checkHorizontal(sudokuTable) || checkVertical(sudokuTable) || checkRegions(sudokuTable));
    }

    private boolean checkHorizontal(int[][] sudokuTable) {
        for( int y = 0 ; y < 9 ; y++ ){
            for( int posX = 0 ; posX < 9 ; posX++ ){

                if( sudokuTable[posX][y] == 0 ){
                    return false;
                }
                for( int x = posX + 1 ; x < 9 ; x++ ){
                    if( sudokuTable[posX][y] == sudokuTable[x][y] || sudokuTable[x][y] == 0 ){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkVertical(int[][] sudokuTable) {
        for( int x = 0 ; x < 9 ; x++ ){
            for( int posY = 0 ; posY < 9 ; posY++ ){

                if( sudokuTable[x][posY] == 0 ){
                    return false;
                }
                for( int y = posY + 1 ; y < 9 ; y++ ){
                    if( sudokuTable[x][posY] == sudokuTable[x][y] || sudokuTable[x][y] == 0 ){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean checkRegions(int[][] sudokuTable) {
        for( int xRegion = 0; xRegion < 3; xRegion++ ){
            for( int yRegion = 0; yRegion < 3 ; yRegion++ ){
                if( !checkRegion(sudokuTable, xRegion, yRegion) ){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRegion(int[][] sudokuTable , int xRegion , int yRegion){
        for( int posX = xRegion * 3; posX < xRegion * 3 + 3 ; posX++ ){
            for( int posY = yRegion * 3 ; posY < yRegion * 3 + 3 ; posY++ ){
                for( int x = posX ; x < xRegion * 3 + 3 ; x++ ){
                    for( int y = posY ; y < yRegion * 3 + 3 ; y++ ){
                        if( (( x != posX || y != posY) && sudokuTable[posX][posY] == sudokuTable[x][y] ) || sudokuTable[x][y] == 0 ){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkSudokuPlay(int[][] sudokuTable, int num, int x, int y) {
        return (checkHorizontalPlay(sudokuTable,num,x,y) || checkVerticalPlay(sudokuTable,num,x,y) || checkRegionPlay(sudokuTable,num,x,y));
        //return false;
    }

    private boolean checkRegionPlay(int[][] sudokuTable, int num, int x, int y) {
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


    private boolean checkHorizontalPlay(int[][] sudokuTable, int num, int x, int y) {
        for( int k = 0 ; k < 9 ; k++ ){
            if( k != x && sudokuTable[k][y] == num){
                Log.e("Conflito","nums iguais na mesma linha");
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalPlay(int[][] sudokuTable, int num, int x,int y) {
        for( int k = 0 ; k < 9 ; k++ ){
            if( k != y && sudokuTable[x][k] == num){
                Log.e("Conflito","nums iguais na mesma coluna");
                return true;
            }
        }
        return false;
    }
}