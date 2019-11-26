package pt.isec.mindunlocker;

import android.content.Context;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

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
            table.setItem(selectedPosX,selectedPosY,number);
        }
        if(table.checkGame()){

        }
    }

}
