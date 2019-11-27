package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

public class CustomizedGameActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnStartGame;

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

        btnStartGame = findViewById(R.id.btnStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameEngine.getInstance().NFillCells() < MIN_CELLS) {
                    Toast.makeText(getApplicationContext(), "Insert more numbers (min = " + MIN_CELLS + ")", Toast.LENGTH_SHORT).show();
                } else {
                    sudokusolver(GameEngine.getInstance().getTable());
                    GameEngine.getInstance().createTableWithVars(getApplicationContext(), soluction);
                    Intent intent = new Intent(getApplicationContext(), GameplayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean sudokusolver(GameTable board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                soluction[row][col] = board.getItem(row, col).getValue();
            }
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // we search an empty cell
                if (soluction[row][col] == 0) {
                    // we try possible numbers
                    for (int number = 1; number <= 9; number++) {
                        if (GameEngine.getInstance().checkGame(row, col, number)) {
                            // number ok. it respects sudoku constraints
                            soluction[row][col] = number;

                            if (sudokusolver(board)) { // we start backtracking recursively
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

    @Override
    public void onClick(View v) {
        int num = -1;
        switch (v.getId()) {
            case R.id.selectNr1:
                //Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
                num = 1;
                break;
            case R.id.selectNr2:
                //Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
                num = 2;
                break;
            case R.id.selectNr3:
                //Toast.makeText(this,"3",Toast.LENGTH_SHORT).show();
                num = 3;
                break;
            case R.id.selectNr4:
                //Toast.makeText(this,"4",Toast.LENGTH_SHORT).show();
                num = 4;
                break;
            case R.id.selectNr5:
                //Toast.makeText(this,"5",Toast.LENGTH_SHORT).show();
                num = 5;
                break;
            case R.id.selectNr6:
                //Toast.makeText(this,"6",Toast.LENGTH_SHORT).show();
                num = 6;
                break;
            case R.id.selectNr7:
                //Toast.makeText(this,"7",Toast.LENGTH_SHORT).show();
                num = 7;
                break;
            case R.id.selectNr8:
                //Toast.makeText(this,"8",Toast.LENGTH_SHORT).show();
                num = 8;
                break;
            case R.id.selectNr9:
                //Toast.makeText(this,"9",Toast.LENGTH_SHORT).show();
                num = 9;
                break;
        }
        GameEngine.getInstance().setNumberCustom(num, getApplicationContext());
    }
}
