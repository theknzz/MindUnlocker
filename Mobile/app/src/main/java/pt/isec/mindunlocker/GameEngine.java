package pt.isec.mindunlocker;

import android.content.Context;
import pt.isec.mindunlocker.api.insertGame.InsertGame;
import android.widget.Toast;
import java.io.Serializable;
import java.util.Arrays;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class GameEngine implements Serializable {
    private static GameEngine instance;
    private GameTable table = null;

    private int[][] solutionTable;
    private int[][] gameTable;

    private int n;
    private int selectedPosX;
    private int selectedPosY;

    private boolean custom;
    //Point System:
    int points = 1000;  // When the game starts the user has 1000 points.
    int CM = 50;        // Correct Multiplier: For each correct entry the user scores 3 * CM.
    int EM = 5;         // Error Multiplier: An error costs the user an error-multiplier (EM).
    int FS;             // Final Score: [FS = LM - (TS * 5)]
    int LM = 0;         // Level-Multiplier: The level-multiplier is 500, 250 and 100.
    int TS = 0;         // Time Spent: is the finalTime.
    int hints = 0;

    public GameEngine() {
        gameTable = new int[9][9];
        solutionTable = new int[9][9];

        selectedPosX = -1;
        selectedPosY = -1;
        n = 0;
    }

    public int getSelectedPosX() {
        return selectedPosX;
    }

    public int getSelectedPosY() {
        return selectedPosY;
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

    public void createTable(Context context, int level){
        custom = false;
        solutionTable = SudokuGenerator.getInstance().generateTable();
        copieTable();
        gameTable = SudokuGenerator.getInstance().removeElements(gameTable, level);
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

    public void startTimer() {

    }

    /**
     * The functions below are reserved for scoring purposes
     */
    public void correctPlay(){
        points += CM;
    }

    public void incorrectPlay(){
        points += EM;
        EM *= 2;    //The error-multiplier (EM) starts at 5 and is doubled every time the user makes a mistake.
    }

    public void decrementCM(){
        CM -= -1;
    }

    public void setTimeSpent(int seconds, int minutes){
        TS = seconds;
        if(minutes > 0)
            TS += (minutes*60);
    }

    public void tookHint(){
        points -= 500;
        hints++;
    }

    public String finalScore(){
        return " " + getScore() + " points";
    }

    public int getScore() {
        FS = points;
        FS += LM - (TS * 5);
        return FS;
    }

    public int getHints() {
        return hints;
    }
}
