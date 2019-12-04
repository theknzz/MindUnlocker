package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;

public class CustomizedGameActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnStartGame, btnErase;

    static final int MIN_CELLS = 17;

    int[][] soluction = new int[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creategame);

        GameEngine.getInstance().createTableEmpty(this);

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
    }

    public void onStartGame (View v) {
        if (GameEngine.getInstance().NFillCells() < MIN_CELLS) {
            Toast.makeText(getApplicationContext(), "Insert more numbers (min = " + MIN_CELLS + ")", Toast.LENGTH_SHORT).show();
        } else {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    soluction[row][col] = GameEngine.getInstance().getTable().getItem(row, col).getValue();
                }
            }
            sudokusolver();
            GameEngine.getInstance().createTableWithVars(soluction);
            Intent intent = new Intent(getApplicationContext(), GameplayActivity.class);
            startActivity(intent);
        }
    }

    public void onErase(View v) {
        //GameEngine.getInstance().setNumberCustom(0, getApplicationContext());
        Button b = (Button)v;
        b.setSelected(true);
        GameEngine.getInstance().setNumber(0);
    }

    private boolean sudokusolver() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // we search an empty cell
                if (soluction[row][col] == 0) {
                    // we try possible numbers
                    for (int number = 1; number <= 9; number++) {
                        if (checkSudokuCustom(row, col, number)) {
                            // number ok. it respects sudoku constraints
                            soluction[row][col] = number;

                            if (sudokusolver()) { // we start backtracking recursively
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

    private boolean checkSudokuCustom(int row, int col, int number) {
        return (checkHorizontalCustom(row, col, number) && checkVerticalCustom(row, col, number)
                && checkRegionsCustom(row, col, number));
    }

    private boolean checkHorizontalCustom(int row, int col, int number) {
        for (int i = 0; i < 9; i++)
            if (soluction[i][col] == number)
                return false;
        return true;
    }

    private boolean checkVerticalCustom(int row, int col, int number) {
        for (int i = 0; i < 9; i++)
            if (soluction[row][i] == number)
                return false;
        return true;
    }

    private boolean checkRegionsCustom(int row, int col, int number) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (soluction[i][j] == number)
                    return false;
        return true;
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        int num = Integer.parseInt(b.getText().toString());

        deselectAllOthers();
        b.setSelected(true);

        //GameEngine.getInstance().setNumberCustom(num, getApplicationContext());
        GameEngine.getInstance().setNumber(num);
        Log.i("Info","selected num: " + b.getText().toString());
    }

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
}
