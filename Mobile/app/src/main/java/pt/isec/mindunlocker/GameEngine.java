package pt.isec.mindunlocker;

import android.content.Context;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

public class GameEngine {
    private static GameEngine instance;
    private GameTable table = null;

    private int[][] solutionTable = new int[9][9];
    private int[][] gameTable = new int[9][9];


    private int n = 0;
    private int selectedPosX = -1;
    private int selectedPosY = -1;
    public GameEngine() {
    }

    public static GameEngine getInstance() {
        if(instance == null){
            instance = new GameEngine();
        }
        return instance;
    }

    public void copieTable(){
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                 gameTable[i][j] = solutionTable[i][j];
    }

    private void clearPos() {
        selectedPosY = selectedPosX = -1;

    }

    public void createTable(Context context){
        solutionTable = SudokuGenerator.getInstance().generateTable();
        copieTable();
        gameTable = SudokuGenerator.getInstance().removeElements(gameTable,0);
        table = new GameTable(context);
        table.setTable(gameTable);
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

    public void setNumber(int number){

        if(selectedPosX != -1 && selectedPosY != -1){
            n = number;
            //table.setItem(selectedPosX,selectedPosY,n);
            //clearPos();
            if(table.checkGame()){
                //Show final score
            }else if(number != 0){
                table.checkPosition(number,selectedPosX,selectedPosY);
            }
        }

    }

    public int getSelectedPosX() {
        return selectedPosX;
    }

    public int getSelectedPosY() {
        return selectedPosY;
    }

    public int getN() {
        return n;
    }
}
