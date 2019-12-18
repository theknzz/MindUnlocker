package pt.isec.mindunlocker;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class GameEngine implements Serializable {

    private static final long serialVersionUID = 1000000000L;

    private static GameEngine instance;
    private GameTable table = null;

    private int[][] solutionTable;
    private int[][] gameTable;
    private int[][] gameTableVar;

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
        gameTableVar = new int[9][9];
        solutionTable = new int[9][9];

        initializeVars();
    }

    /**
     * Initialize all points variables
     */
    private void initializeVars(){
        selectedPosX = -1;
        selectedPosY = -1;
        n = 0;

        points = 1000;
        CM = 50;
        EM = 5;
        LM = 0;
        TS = 0;
        hints = 0;
    }

    /**
     * Get x position of selected cell
     * @return <code>int</code> position x from cell selected
     */
    public int getSelectedPosX() {
        return selectedPosX;
    }

    /**
     * Get y position of selected cell
     * @return <code>int</code> position x from cell selected
     */
    public int getSelectedPosY() {
        return selectedPosY;
    }

    /**
     * Get or create a instance from himself
     * @return <code>GameEngine</code>
     */
    public static GameEngine getInstance() {
        if(instance == null){
            instance = new GameEngine();
        }
        return instance;
    }

    /**
     * Set a new instance from himself
     * @param gE <code>GameEngine</code>
     */
    public static void setInstance(GameEngine gE) {
            instance = gE;
    }

    /**
     * Copy a <var>solutionTable</var> to <var>gameTable</var>
     */
    public void copieTable(){
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                 gameTable[i][j] = solutionTable[i][j];
    }

    /**
     * Create a new Table with full numbers put in <var>solutionTable</var>, remove
     * some cells depending of the level
     * @param context <code>Context</code> of the Activity
     * @param level <code>int</code> level to set
     */
    public void createTable(Context context, int level){
        initializeVars();
        custom = false;
        solutionTable = SudokuGenerator.getInstance().generateTable();
//        debugPrintSolutionTable();
        copieTable();
        gameTable = SudokuGenerator.getInstance().removeElements(gameTable, level);
        table = new GameTable(context);
        table.setTable(gameTable);
    }

    /**
     * Get a list with editable cells
     * @return <code>List<Integer></code> editable cells list
     */
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

    /**
     * Create a new table with value already inserted and needs a solution table only
     * @param solutionTable <code>int [][]</code> solution table
     */
    public void createTableWithVars(int[][] solutionTable) {
        initializeVars();
        this.solutionTable = solutionTable;
    }

    /**
     * Create a new table without values and soluction, just create a view table
     * @param context <code>Context</code>
     */
    public void createTableEmpty(Context context) {
        custom = true;
        copieTable();
        table = new GameTable(context);
    }

    /**
     * Create a new table (view) with values in <var>gameTable</var>
     * @param context <code>Context</code>
     */
    public void createTableLoad(Context context) {
        table = new GameTable(context);
        table.setTable(gameTable);
        table.setTableLoad(gameTableVar);
    }

    /**
     * Get a table
     * @return <code>GameTable</code>
     */
    public GameTable getTable() {
        return table;
    }

    /**
     * Get a value from solution table
     * @param x <code>int</code> position horizontal
     * @param y <code>int</code> position vertical
     * @return <code>int</code> value
     */
    public int getSolutionTable(int x, int y) {
        return solutionTable[x][y];
    }

    /**
     * Set number in <var>selectedPosX</var> and <var>selectedPosY</var> from table
     * @param context <code>Context</code>
     */
    public void setItem(Context context) {
        table.setItem(selectedPosX, selectedPosY,n, context);
    }

    /**
     * Test Function - set item wihtout validation
     */
    public void setItemWithNoValidation(Context context) {
        table.setItemWithNoValidation(selectedPosX, selectedPosY, n);
    }

    /**
     * Set number in <var>selectedPosX</var> and <var>selectedPosY</var> from table
     * @param context <code>Context</code>
     */
    public void setItemCustom(Context context) {
        table.setItemCustom(selectedPosX, selectedPosY,n, context);
    }

    /** Gets a Original cell from the table
     *  Helper Method to unit test TC-10
     * @return <code>SudokuCell</code> of an original cell (aka bold, aka not modifiable)
     */
    public SudokuCell getOriginalCell() {
        SudokuCell cell = null;
        do{
            int x = (int) (Math.random() * 9);
            int y = (int) (Math.random() * 9);

            cell = SudokuCell.getInstance()[x][y];
        }while(cell.isModifiable());
        Log.e("CELL", " " + cell.getValue());
        return cell;
    }

    /**
     * Method that sets the number into the game table if the number is valid according to the sudoku rules
     * @return true: if it sets, false: if it doesn't
     */
    public boolean setCustomItemIfValid() {
        return table.setCustomItemIfValid(selectedPosX, selectedPosY, n);
    }

    /**
     * Set value in <var>selectedPosX</var> and <var>selectedPosY</var>
     * @param x <code>int</code> position horizontal
     * @param y <code>int</code> position vertical
     */
    public void setSelectedPosition(int x, int y) {
        this.selectedPosX = x;
        this.selectedPosY = y;
    }

    /**
     * Updates the selected cell to the cell passed in parameter
     * @param cell <code>SudokuCell</code>
     */
    public void setSelectedPosition(SudokuCell cell) {
        this.selectedPosX = (int) cell.getX();
        this.selectedPosY = (int) cell.getY();
    }

    /**
     * Set number
     * @param number <code>int</code> number to set <var>n</var>
     */
    public void setNumber(int number){
        n=number;
    }

    /**
     * Count number of the cells filled
     * @return <code>int</code>
     */
    public int NFillCells() {
        return table.fillCells();
    }

    /**
     * Get Custom
     * @return <code>boolean</code> <var>custom</var>
     */
    public boolean getCustom() {
        return custom;
    }

    /**
     * sudokuSolver method generates a new solution for a <code>int [][]</code>
     * @param soluction <code>int [][]</code> table to generate a solution
     * @return <code>boolean</code> <code>true</code> if exist solution <code>false</code> if not
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

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number
     * @param row <var>int</var> row index
     * @param col <var>int</var> column index
     * @param number <var>int</var> number
     * @param soluction <var>int [][]</var> table
     * @return <code>booelan</code> <code>true</code> if table valid,
     * <code>false</code> if table is invalid
     */
    private boolean checkSudokuSolver(int row, int col, int number, int[][] soluction) {
        return (checkHorizontalSolver(row, col, number, soluction) &&
                checkVerticalSolver(row, col, number, soluction)
                && checkRegionsSolver(row, col, number, soluction));
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number horizontally
     * @param row <var>int</var> row index
     * @param col <var>int</var> column index
     * @param number <var>int</var> number
     * @param soluction <var>int [][]</var> table
     * @return <code>boolean</code> <code>true</code> if exist conflicts in row
     */
    private boolean checkHorizontalSolver(int row, int col, int number, int[][] soluction) {
        for (int i = 0; i < 9; i++)
            if (soluction[i][col] == number)
                return false;
        return true;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number vertically
     * @param row <var>int</var> row index
     * @param col <var>int</var> column index
     * @param number <var>int</var> number
     * @param soluction <var>int [][]</var> table
     * @return <code>boolean</code> <code>true</code> if exist conflicts in column
     */
    private boolean checkVerticalSolver(int row, int col, int number, int[][] soluction) {
        for (int i = 0; i < 9; i++)
            if (soluction[row][i] == number)
                return false;
        return true;
    }

    /**
     * Check <code>Table</code> passed by argument if exist conflicts with number in the regions
     * 3 X 3
     * @param row <var>int</var> row index
     * @param col <var>int</var> column index
     * @param number <var>int</var> number
     * @param soluction <var>int [][]</var> table
     * @return <code>boolean</code> <code>true</code> if exist conflicts in squads 3 X 3
     */
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

    /**
     * Incorrect play made
     */
    public void incorrectPlay(){
        points -= EM;
        EM *= 2;    //The error-multiplier (EM) starts at 5 and is doubled every time the user makes a mistake.
    }

    /*
    public void decrementCM(){
        CM -= 1;
    }*/

    /**
     * Convert time in seconds = <var>TS</var>
     */
    public void setTimeSpent(){
        TS = seconds;
        if(minutes > 0)
            TS += (minutes*60);
    }

    /**
     * Counter of hints and decrement point when it used
     */
    public void tookHint(){
        points -= 50;
        hints++;
    }

    /**
     * Decide depending level how many points game will give when the game is finished
     */
    public void levelScoreAdded(){
        switch(level){
            case 0: LM = 100;break;
            case 2: LM = 500;break;
            default: LM = 250;
        }
    }

    /**
     * Get final time, showing a <code>String</code>
     * @return <code>String</code> Final score
     */
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

    /**
     * Get number of Hints requested
     * @return <code>int</code> <var>hints</var>
     */
    public int getHints() {
        return hints;
    }

    /**
     * Get number of level (0 - 2)
     * @return <code>int</code> <var>level</var>
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get current minutes
     * @return <code>int</code> <var>minutes</var>
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Get current seconds
     * @return <code>int</code> <var>seconds</var>
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Set level (0 - 2)
     * @param level <code>int</code>
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Set Minutes
     * @param minutes <code>minutes</code>
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Set Seconds
     * @param seconds <code>seconds</code>
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Get a String showing a final time
     * @return <code>String</code> <var>finalTime</var>
     */
    public String getFinalTime() {
        return finalTime;
    }

    /**
     * Set a String with final time
     * @param minutes <code>int</code> <var>minutes</var>
     * @param seconds <code>int</code> <var>seconds</var>
     */
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

    public void setValue(int x, int y, int number) {
        gameTableVar[x][y] = number;
    }
}
