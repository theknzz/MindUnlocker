package pt.isec.mindunlocker;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

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

    private String finalTime = null;

    private int level = 0;
    private int minutes=0, seconds=0;


    private boolean custom;
    //Point System:
    private int points;  // When the game starts the user has 1000 points.
    private int CM;        // Correct Multiplier: For each correct entry the user scores 3 * CM.
    private int EM;         // Error Multiplier: An error costs the user an error-multiplier (EM).
    private int FS;             // Final Score: [FS = LM - (TS * 5)]
    private int LM;         // Level-Multiplier: The level-multiplier is 5000, 2500 and 1000.
    private int TS;         // Time Spent: is the finalTime.
    private int hints;

    public GameEngine() {
        gameTable = new int[9][9];
        solutionTable = new int[9][9];
        selectedPosX = -1;
        selectedPosY = -1;

        n = 0;

        inicializeVars();
    }

    private void inicializeVars(){

        points = 1000;
        CM = 50;
        EM = 5;
        LM = 0;
        TS = 0;
        hints = 0;
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

    public static void setInstance(GameEngine gE) {
            instance = gE;
    }

    public void copieTable(){
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                 gameTable[i][j] = solutionTable[i][j];
    }

    public void createTable(Context context, int level){
        inicializeVars();
        custom = false;
        solutionTable = SudokuGenerator.getInstance().generateTable();
//        debugPrintSolutionTable();
        copieTable();
        gameTable = SudokuGenerator.getInstance().removeElements(gameTable, level);
        table = new GameTable(context);
        table.setTable(gameTable);
    }

    public List<Integer> getEditableCell() {
        return table.getEditableCell();
    }

    /**
     * Method used to "test" the invalid cell hint
     */
    private void debugPrintSolutionTable() {
        if (solutionTable!=null) {
            for (int y=0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    System.out.print(solutionTable[x][y] + " ");
                }
                System.out.println("");
            }
        }
    }

    public void createTableWithVars(int[][] solutionTable) {
        inicializeVars();
        solutionTable = solutionTable;
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

    public void setItem(Context context) {
        table.setItem(selectedPosX, selectedPosY,n, context);
    }

    public void setItemWithNoValidation(Context context) {
        table.setItemWithNoValidation(selectedPosX, selectedPosY, n);
    }

    public void setItemCustom(Context context) {
        table.setItemCustom(selectedPosX, selectedPosY,n, context);
    }

    /**
     * Method that sets the number into the game table if the number is valid according to the sudoku rules
     * @return true: if it sets, false: if it doesn't
     */
    public boolean setCustomItemIfValid() {
        return table.setCustomItemIfValid(selectedPosX, selectedPosY, n);
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
     * sudokuSolver method generates a new solution for a <code>int [][]</code>
     * @param soluction <code>int [][]</code> table to generate a solution
     * @return 
     */
    public boolean sudokuSolver(int[][] soluction) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // we search an empty cell
                if (soluction[row][col] == 0) {
                    // we try possible numbers
                    for (int number = 1; number <= 9; number++) {
                        if (checkSudokuSolver(row, col, number, soluction)) {
                            // number ok. it respects sudoku constraints
                            soluction[row][col] = number;

                            if (sudokuSolver(soluction)) { // we start backtracking recursively
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

    private boolean checkSudokuSolver(int row, int col, int number, int[][] soluction) {
        return (checkHorizontalSolver(row, col, number, soluction) &&
                checkVerticalSolver(row, col, number, soluction)
                && checkRegionsSolver(row, col, number, soluction));
    }

    private boolean checkHorizontalSolver(int row, int col, int number, int[][] soluction) {
        for (int i = 0; i < 9; i++)
            if (soluction[i][col] == number)
                return false;
        return true;
    }

    private boolean checkVerticalSolver(int row, int col, int number, int[][] soluction) {
        for (int i = 0; i < 9; i++)
            if (soluction[row][i] == number)
                return false;
        return true;
    }

    private boolean checkRegionsSolver(int row, int col, int number, int[][] soluction) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (soluction[i][j] == number)
                    return false;
        return true;
    }

    /**
     * The functions below are reserved for scoring purposes
     */
    public void correctPlay(){
        points += CM;
    }

    public void incorrectPlay(){
        points -= EM;
        EM *= 2;    //The error-multiplier (EM) starts at 5 and is doubled every time the user makes a mistake.
    }

    /*
    public void decrementCM(){
        CM -= 1;
    }*/

    public void setTimeSpent(){
        TS = seconds;
        if(minutes > 0)
            TS += (minutes*60);
    }

    public void tookHint(){
        points -= 50;
        hints++;
    }

    public void levelScoreAdded(){
        switch(level){
            case 0: LM = 100;break;
            case 2: LM = 500;break;
            default: LM = 250;
        }
    }

    public String finalScore(){
        return " " + getScore() + " points";
    }

    public int getScore() {
        FS = points;
        FS += LM - ((TS%30) * 5);
        return FS;
    }

    /**
     * Method that checks if the current solution is getting built according to the game solution
     * @return If the current solution has a cell different from the game solution ? Invalid Sudoku Cell : null
     */
    public SudokuCell currentGameIsNotMatchingSolution() {
        for (int y=0; y < 9; y++)
            for (int x=0; x < 9; x++) {
                SudokuCell cell = table.getItem(x, y);
                if (cell.getValue()!=0) {
                    if (cell.getValue() != solutionTable[x][y])
                        return cell;
                }
            }
        return null;
    }

    public int getHints() {
        return hints;
    }

    public int getLevel() {
        return level;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.finalTime = String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Get the selected number
     * @return selected number
     */
    public int getN() {
        return n;
    }

    /**
     * Get the value in the specified cell coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return cell's value
     */
    public int getValueIn(int x, int y) {
        return table.getValueIn(x, y);
    }
}
