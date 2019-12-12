package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class CustomizedGameActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnErase;

    static final int MIN_CELLS = 17;

    int[][] soluction = new int[9][9];

    GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creategame);

        gameEngine = GameEngine.getInstance();
        gameEngine.createTableEmpty(this);

        btn1 = findViewById(R.id.selectNr1);
        btn2 = findViewById(R.id.selectNr2);
        btn3 = findViewById(R.id.selectNr3);
        btn4 = findViewById(R.id.selectNr4);
        btn5 = findViewById(R.id.selectNr5);
        btn6 = findViewById(R.id.selectNr6);
        btn7 = findViewById(R.id.selectNr7);
        btn8 = findViewById(R.id.selectNr8);
        btn9 = findViewById(R.id.selectNr9);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        btnErase = findViewById(R.id.btnErase);
    }

    /**
     * @param aux <code>int</code> number of cells filled 
     * @return <code>int</code> level (0 - 2)
     */
    public int setLevel(int aux){
        int level;

        if(aux > 81 || aux < 17){
            level = -1;
        }else if (aux >= 51 && aux <=81) {
            level = 0;  //Easy
        } else if (aux <= 40 && aux >= 17) {
            level = 2;  //Hard
        } else {
            level = 1;  //Medium
        }

        return level;
    }

    /**
     * Verify, read and call the <code>GameplayActivity</code> to start game with values
     * the user entered
     * @param v <code>View</code> Button to Start the game
     */
    public void onStartGame(View v) {
        if (!validateStartGame(gameEngine.NFillCells())) {
            Toast.makeText(getApplicationContext(), "Insert more numbers (min = " + MIN_CELLS + ")", Toast.LENGTH_SHORT).show();
        } else {

            int aux = 0;

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    soluction[row][col] = gameEngine.getTable().getItem(row, col).getValue();
                    if (soluction[row][col] != 0) {
                        aux++;
                    }
                }
            }

            int level = setLevel(aux);

            gameEngine.sudokuSolver(soluction);
          
            gameEngine.createTableWithVars(soluction);
            Intent intent = new Intent(getApplicationContext(), GameplayActivity.class);
            //intent.putExtra("level", level);
            intent.putExtra("level", level);
            intent.putExtra("type", "custom");
            startActivity(intent);
        }
    }


    /**
     * Method that validates if the state of game can generate a new game
     * @param nrFillCells - nr of cells
     * @return true: if has more or at least 16 cells, false: if it hasn't
     */
    public boolean validateStartGame(int nrFillCells) {
        if (nrFillCells < MIN_CELLS || nrFillCells > 81 || nrFillCells < 0) {
            return false;
        }
        return true;
    }
          
    /**
     * Clear cell selected, using 0
     * @param v <code>View</code> button Erase
     */
    public void onErase(View v) {
        Button b = (Button) v;
        deselectAllOthers();
        b.setSelected(true);
        gameEngine.setNumber(0);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        int num = Integer.parseInt(b.getText().toString());

        deselectAllOthers();
        b.setSelected(true);

        gameEngine.setNumber(num);
    }

    /**
     * Unselect buttons
     */
    private void deselectAllOthers() {
        btn1.setSelected(false);
        btn2.setSelected(false);
        btn3.setSelected(false);
        btn4.setSelected(false);
        btn5.setSelected(false);
        btn6.setSelected(false);
        btn7.setSelected(false);
        btn8.setSelected(false);
        btn9.setSelected(false);
        btnErase.setSelected(false);
    }

    public static int getMinCells() {
        return MIN_CELLS;
    }
}
