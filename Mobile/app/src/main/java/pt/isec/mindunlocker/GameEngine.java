package pt.isec.mindunlocker;

import android.content.Context;

import pt.isec.mindunlocker.api.insertGame.InsertGame;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

public class GameEngine {
    private InsertGame service = new InsertGame();
    private static GameEngine instance;
    private GameTable table = null;

    private int[][] solutionTable = new int[9][9];
    private int[][] gameTable = new int[9][9];

    private int selectedPosX;
    private int selectedPosY;

    public GameEngine() {
        clearPos();
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
            table.setItem(selectedPosX,selectedPosY,number);
            clearPos();
        }

        if(table.checkGame()){
            //TODO get the valid values
            service.sentData(200, 20, "Hard", 20);
        }
    }

}
