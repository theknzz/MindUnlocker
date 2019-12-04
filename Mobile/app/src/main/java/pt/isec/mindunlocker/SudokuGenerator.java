package pt.isec.mindunlocker;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {

    private static SudokuGenerator instance;
    private ArrayList<ArrayList<Integer>> Available = new ArrayList<ArrayList<Integer>>();
    private Random rand = new Random();

    public SudokuGenerator() {
    }

    public static SudokuGenerator getInstance(){
        if(instance == null){
            instance = new SudokuGenerator();
        }
        return instance;
    }

    public int[][] generateTable(){
        int[][] sudokuTable = new int[9][9];
        int currPos = 0;


        while( currPos < 81){
            //Log.i("Loop","pos: " +currPos);
            if(currPos == 0){
                clearTable(sudokuTable);
            }

            if(Available.get(currPos).size() != 0){
                int i = rand.nextInt(Available.get(currPos).size());
                int number = Available.get(currPos).get(i);

                if(!hasConflict(sudokuTable, currPos, number)){
                    int posX = currPos % 9;
                    int posY = currPos / 9;

                    sudokuTable[posX][posY] = number;

                    Available.get(currPos).remove(i);

                    currPos++;
                }else{
                    Available.get(currPos).remove(i);
                }

            }else{
                for (int i = 1; i <= 9; i++){
                    Available.get(currPos).add(i);
                }
                currPos--;
            }
        }

        return sudokuTable;
    }

    private void clearTable(int[][] sudokuTable) {
        Available.clear();

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                sudokuTable[j][i] = 0;
            }
        }

        for (int spot = 0; spot < 81; spot++){
            Available.add(new ArrayList<Integer>());
            for (int n = 1; n <= 9; n++) {
                Available.get(spot).add(n);
            }
        }
    }

    private boolean hasConflict(int[][] sudokuTable, int currPos, final int number){
        int posX = currPos % 9;
        int posY = currPos / 9;

        if(checkHorizontal(sudokuTable,number,posX,posY) || checkVertical(sudokuTable,number,posX,posY) || check3x3square(sudokuTable,number,posX,posY)){
            return true;
        }
        return false;
    }

    private boolean checkVertical(int[][] sudokuTable, int number, int posX, int posY){
        for (int y = posY - 1; y >= 0; y--){
            if(number == sudokuTable[posX][y]){
                return true;
            }
        }
        return false;
    }

    private boolean checkHorizontal(int[][] sudokuTable, int number, int posX, int posY){
        for (int x = posX - 1; x >= 0; x--){
            if(number == sudokuTable[x][posY]){
                return true;
            }
        }
        return false;
    }

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

    public int[][] removeElements( int[][] sudokuTable, int level){
        int i = 0, n;

        switch (level){
            case 0: n = 30;break;
            case 2: n = 60;break;
            default: n = 40; // se for medio ou outro
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
