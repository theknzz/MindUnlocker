package pt.isec.mindunlocker;

import android.content.Context;

import java.io.Serializable;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

/**
 * Class to create Game Engine instance, the game table and solution.
 * As well as to take care of the point scoring aspect of the game.
 *
 * @author João Santos
 */
public class GameEngine implements Serializable {
    private static GameEngine instance;
    private GameTable table = null;

    private String finalTime = null;
    private int[][] solutionTable;
    private int[][] gameTable;
    private int n;
    private int selectedPosX;
    private int selectedPosY;
    private int level = 0;
    private int minutes=0, seconds=0;
    private boolean custom;

    //Point System:
    private int points = 1000;  // When the game starts the user has 1000 points.
    private int CM = 50;        // Correct Multiplier: For each correct entry the user scores 3 * CM.
    private int EM = 5;         // Error Multiplier: An error costs the user an error-multiplier (EM).
    private int FS;             // Final Score: [FS = LM - (TS * 5)]
    private int LM = 0;         // Level-Multiplier: The level-multiplier is 500, 250 and 100.
    private int TS = 0;         // Time Spent: is the finalTime.
    private int hints = 0;

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

    /**
     * @return <code>Instance</code> new or created
     */
    public static GameEngine getInstance() {
        if(instance == null){
            instance = new GameEngine();
        }
        return instance;
    }

    /**
     * @param gE <code>GameEngine</code> of actual instance
     */
    public static void setInstance(GameEngine gE) {
            instance = gE;
    }

    /**
     * Copies solution table to game table
     */
    public void copieTable(){
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                 gameTable[i][j] = solutionTable[i][j];
    }

    /**
     * Create a new table based on a level
     *
     * @param context <code>Context</code>
     * @param level <code>int</code> of the game to be created
     */
    public void createTable(Context context, int level){
        custom = false;
        solutionTable = SudokuGenerator.getInstance().generateTable();
        copieTable();
        gameTable = SudokuGenerator.getInstance().removeElements(gameTable, level);
        table = new GameTable(context);
        table.setTable(gameTable);
    }

    /**
     * @param solutionTable <code>int[][]</code>
     */
    public void createTableWithVars(int[][] solutionTable) {
        this.solutionTable = solutionTable;
    }

    /**
     * @param context
     */
    public void createTableEmpty(Context context) {
        custom = true;
        copieTable();
        table = new GameTable(context);
    }

    /**
     * @return <code>GameTable</code> of the current board
     */
    public GameTable getTable() {
        return table;
    }

    /**
     * @param x <code>int</code> position for the X axis
     * @param y <code>int</code> position for the Y axis
     * @return <code>int</code> value of the cell at the (x,y) location from the solution
     */
    public int getSolutionTable(int x, int y) {
        return solutionTable[x][y];
    }

    /**
     *
     */
    public void setItem() {
        table.setItem(selectedPosX, selectedPosY,n);
    }

    /**
     * @param x <code>int</code> position for the X axis
     * @param y <code>int</code> position for the Y axis
     */
    public void setSelectedPosition(int x, int y) {
        this.selectedPosX = x;
        this.selectedPosY = y;
    }

    /**
     * @param number <code>int</code> number to be played
     */
    public void setNumber(int number){
        n=number;
    }

    /**
     * @return <code>int</code> number of filled cells
     */
    public int NFillCells() {
        return table.fillCells();
    }

    public void setItemCustom() {
        table.setItemCustom(selectedPosX, selectedPosY,n);
    }

    public boolean getCustom() {
        return custom;
    }

    public void startTimer() {

    }

    /**
     * sudokusolver method generates a new solution for a <code>int [][]</code>
     * @param soluction <code>int [][]</code> table to generate a solution
     * @return 
     */
    public boolean sudokusolver(int[][] soluction) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // we search an empty cell
                if (soluction[row][col] == 0) {
                    // we try possible numbers
                    for (int number = 1; number <= 9; number++) {
                        if (checkSudokuSolver(row, col, number, soluction)) {
                            // number ok. it respects sudoku constraints
                            soluction[row][col] = number;

                            if (sudokusolver(soluction)) { // we start backtracking recursively
                                return true;
                            } else { // if not a solution, we empty the cell and we continue
                                soluction[row][col] = 0;
                            }
                        }
                    }
                    return false; // we return false
                }
            }
        }
        return true; // sudoku solved
    }

    /**
     * Check Sudoku Solver
     *
     * @param row <code>int</code>
     * @param col <code>int</code>
     * @param number <code>int</code>
     * @param solution <code>int[][]</code>
     * @return <code>boolean<code/> - <code>true</code> if ...
     * and <code>false</code> if ...
     */
    private boolean checkSudokuSolver(int row, int col, int number, int[][] solution) {
        return (checkHorizontalSolver(row, col, number, solution) &&
                checkVerticalSolver(row, col, number, solution)
                && checkRegionsSolver(row, col, number, solution));
    }

    /**
     * Check Horizontal Solver
     *
     * @param row <code>int</code>
     * @param col <code>int</code>
     * @param number <code>int</code>
     * @param solution <code>int[][]</code>
     * @return <code>boolean<code/> - <code>true</code> if ...
     * and <code>false</code> if ...
     */
    private boolean checkHorizontalSolver(int row, int col, int number, int[][] solution) {
        for (int i = 0; i < 9; i++)
            if (solution[i][col] == number)
                return false;
        return true;
    }

    /**
     * Check Vertical Solver
     *
     * @param row <code>int</code>
     * @param col <code>int</code>
     * @param number <code>int</code>
     * @param solution <code>int[][]</code>
     * @return <code>boolean<code/> - <code>true</code> if ...
     * and <code>false</code> if ...
     */
    private boolean checkVerticalSolver(int row, int col, int number, int[][] solution) {
        for (int i = 0; i < 9; i++)
            if (solution[row][i] == number)
                return false;
        return true;
    }

    /**
     *  Check Regions Solver
     *
     * @param row <code>int</code>
     * @param col <code>int</code>
     * @param number <code>int</code>
     * @param solution <code>int[][]</code>
     * @return <code>boolean<code/> - <code>true</code> if ...
     * and <code>false</code> if ...
     */
    private boolean checkRegionsSolver(int row, int col, int number, int[][] solution) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (solution[i][j] == number)
                    return false;
        return true;
    }

    /**
     * The functions below are reserved for scoring purposes
     */
    public void correctPlay(){
        points += CM;
    }

    /**
     * Updates score for an incorrect play
     */
    public void incorrectPlay(){
        points += EM;
        EM *= 2;    //The error-multiplier (EM) starts at 5 and is doubled every time the user makes a mistake.
    }

    /**
     * Decrement Correct Multiplier
     */
    public void decrementCM(){
        CM -= -1;
    }

    /**
     * Set the time spent
     */
    public void setTimeSpent(){
        TS = seconds;
        if(minutes > 0)
            TS += (minutes*60);
    }

    /**
     * Update score for requested hint
     */
    public void tookHint(){
        points -= 500;
        hints++;
    }

    /**
     * Update score according to the level played
     */
    public void levelScoreAdded(){
        switch(level){
            case 0: LM = 100;break;
            case 1: LM = 250;break;
            case 2: LM = 500;break;
            default: LM = 50;
        }
    }

    /**
     * @return <code>String</code> of the total points scored
     */
    public String finalScore(){
        return " " + getScore() + " points";
    }

    /**
     * @return <code>int</code> of the Final Score
     */
    public int getScore() {
        FS = points;
        FS += LM - (TS * 5);
        return FS;
    }

    /**
     * @return <code>int</code> of the number of hints asked
     */
    public int getHints() {
        return hints;
    }

    /**
     * @return <code>int</code> of the game level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return <code>int</code> of the minutes of game
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * @return <code>int</code> of the seconds of game
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * @param level <code>int</code> of the game played
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @param minutes <code>int</code> of the game played
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * @param seconds <code>int</code> pf the game played
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * @return <code>String</code> of the Final Time spent
     */
    public String getFinalTime() {
        return finalTime;
    }

    /**
     * @param minutes <code>int</code> of the game
     * @param seconds <code>int</code> of the game
     */
    public void setFinalTime(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.finalTime = String.format("%d:%02d", minutes, seconds);
    }
}
