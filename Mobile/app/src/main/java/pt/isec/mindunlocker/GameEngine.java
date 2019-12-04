package pt.isec.mindunlocker;

import android.content.Context;
import android.widget.Toast;

import java.util.Arrays;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class GameEngine {
    private static GameEngine instance;
    private GameTable table = null;

    private int[][] solutionTable;
    private int[][] gameTable;


    private int n;
    private int selectedPosX;
    private int selectedPosY;

    private boolean custom;

    public GameEngine() {
        gameTable = new int[9][9];
        solutionTable = new int[9][9];

        selectedPosX = -1;
        selectedPosY = -1;
        n = 0;
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
        custom = false;
        solutionTable = SudokuGenerator.getInstance().generateTable();
        copieTable();
        gameTable = SudokuGenerator.getInstance().removeElements(gameTable,0);
        table = new GameTable(context);
        table.setTable(gameTable);
    }

    public void createTableWithVars(int[][] solutionTable) {
        this.solutionTable = solutionTable;
    }

    public void createTableEmpty(Context context) {
        custom = true;
        copieTable();
        table = new GameTable(context);
    }

    public GameTable getTable() {
        return table;
    }

    public int getSolutionTable(int x, int y) {
        return solutionTable[x][y];
    }

    public void setItem() {
        table.setItem(selectedPosX, selectedPosY,n);
    }

    public void setItemCustom() {
        table.setItemCustom(selectedPosX, selectedPosY,n);
    }

    public void setSelectedPosition(int x, int y) {
        this.selectedPosX = x;
        this.selectedPosY = y;
    }

    public void setNumber(int number){
        n=number;
    }

    public int NFillCells() {
        return table.fillCells();
    }

    public boolean getCustom() {
        return custom;
    }
}
