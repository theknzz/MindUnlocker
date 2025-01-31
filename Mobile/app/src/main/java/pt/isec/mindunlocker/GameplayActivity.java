package pt.isec.mindunlocker;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

import pt.isec.mindunlocker.api.insertGame.InsertGame;
import pt.isec.mindunlocker.pt.isec.mindunlocker.view.SudokuCell;


public class GameplayActivity extends AppCompatActivity implements View.OnClickListener {

    GameEngine gameEngine;

    private Random rand = new Random();
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnGiveUp, btnHint, btnErase, btnPencil;

    Dialog finishDialog, giveupDialog;
    TextView timerTextView, scoreTextView, timeTextView, levelTextView;

    InsertGame service = new InsertGame();

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        //Create or return object gameEngine
        gameEngine = GameEngine.getInstance();

        //GetExtras
        Bundle b = getIntent().getExtras();

        if (b == null) {
            gameEngine.setLevel(1);

            gameEngine.createTable(this, gameEngine.getLevel());
            gameEngine.setMinutes(0);
            gameEngine.setSeconds(0);
        } else if (b.getString("type") != null) {
            if (b.getString("type").equals("custom")) {

                gameEngine.setLevel(b.getInt("level"));
            } else if (b.getString("type").equals("load")) {
                gameEngine.createTableLoad(this);
            } else if (b.getString("type").equals("newgame")) {
                gameEngine.setLevel(b.getInt("level"));

                gameEngine.createTable(this, gameEngine.getLevel());
                gameEngine.setMinutes(0);
                gameEngine.setSeconds(0);
            }
        }
        //printSudoku(solutionTable);

        //Set level
        levelTextView = findViewById(R.id.show_level);
        String lvl = "level: ";
        switch (gameEngine.getLevel()) {
            case 0:
                lvl += "Easy";
                break;
            case 1:
                lvl += "Medium";
                break;
            case 2:
                lvl += "Hard";
                break;
        }
        levelTextView.setText(String.format(lvl));

        // Set the timer counting
        timerTextView = findViewById(R.id.gameTimer);
        // create object runnable
        timerRunnable = new TimeThread(gameEngine.getMinutes(), gameEngine.getSeconds(),
                timerHandler, timerTextView);
        timerHandler.postDelayed(timerRunnable, 0);

        // Set Dialogs
        giveupDialog = new Dialog(this);
        giveupDialog.setContentView(R.layout.popup_giveup);

        finishDialog = new Dialog(this);
        finishDialog.setContentView(R.layout.popup_finish);
        scoreTextView = finishDialog.findViewById(R.id.final_score);
        timeTextView = finishDialog.findViewById(R.id.final_time);

        // SetButtons
        btn1 = findViewById(R.id.selectNr1);
        btn2 = findViewById(R.id.selectNr2);
        btn3 = findViewById(R.id.selectNr3);
        btn4 = findViewById(R.id.selectNr4);
        btn5 = findViewById(R.id.selectNr5);
        btn6 = findViewById(R.id.selectNr6);
        btn7 = findViewById(R.id.selectNr7);
        btn8 = findViewById(R.id.selectNr8);
        btn9 = findViewById(R.id.selectNr9);
        btnGiveUp = findViewById(R.id.giveUpBtn);
        btnHint = findViewById(R.id.hintBtn);
        btnErase = findViewById(R.id.eraseBtn);
        btnPencil = findViewById(R.id.pencilBtn);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnGiveUp.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnErase.setOnClickListener(this);
        btnPencil.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        giveup();
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        deselectAllOthers();
        switch (v.getId()) {
            //button give up
            case R.id.giveUpBtn: /*Toast.makeText(this,"Exit",Toast.LENGTH_SHORT).show();*/
                giveup();
                break;
            //button hints
            case R.id.hintBtn:/* Toast.makeText(this,"Showing Hint",Toast.LENGTH_SHORT).show();*/
                showHint();
                GameEngine.getInstance().tookHint();
                v.invalidate();
                break;
            //button erase
            case R.id.eraseBtn:/* Toast.makeText(this,"Delete: ON",Toast.LENGTH_SHORT).show();*/
                b.setSelected(true);
                btnPencil.setSelected(false);
                GameEngine.getInstance().setNumber(0);
                break;
            //button pencil
            case R.id.pencilBtn:/* Toast.makeText(this,"Delete: ON",Toast.LENGTH_SHORT).show();*/
                if (b.isSelected()) {
                    b.setSelected(false);
                    GameEngine.getInstance().getTable().setPencilMode(false);
                } else {
                    b.setSelected(true);
                    GameEngine.getInstance().getTable().setPencilMode(true);
                }
                break;
            default:
                b.setSelected(true);
                gameEngine.setNumber(Integer.parseInt(b.getText().toString()));
                //manda parar a thread do timer
        }
        //if table is complete and correct
        if (gameEngine.getTable().isFinish()) {
            timerHandler.removeCallbacks(timerRunnable);
            gameEngine.levelScoreAdded();
            gameEngine.setTimeSpent();
            scoreTextView.setText(gameEngine.finalScore());
            timeTextView.setText(gameEngine.getFinalTime());
            finishDialog.show();
            if (Token.CONTENT != null) {
                // updating the database with the actual game information
                service.sentData(gameEngine.getScore(),
                        gameEngine.getMinutes() * 60 + gameEngine.getSeconds(),
                        gameEngine.getLevel(), gameEngine.getHints());
            }
        }
    }

    /**
     * Give up the game
     */
    private void giveup() {
        giveupDialog.show();
        timerHandler.removeCallbacks(timerRunnable);
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
        //btnPencil.setSelected(true);
    }

    /**
     * Show a new hint in table
     */
    public void showHint() {
        int x = rand.nextInt(9);
        int y = rand.nextInt(9);
        SudokuCell cell = tableHasUnmatchedCell();

        // The current game has an invalid cell
        if (cell !=null) {
            cell.setWrong(true);
//            cell.getnPaint().setColor(Color.RED);
            cell.invalidate();
            gameEngine.setSelectedPosition(x, y);
            return;
        }

        while (true) {
            if (gameEngine.getTable().getItem(x, y).getValue() == 0) {
                gameEngine.setNumber(gameEngine.getSolutionTable(x, y));
                gameEngine.setSelectedPosition(x, y);
                gameEngine.setItem(this);
                break;
            } else {
                x = rand.nextInt(9);
                y = rand.nextInt(9);
            }
        }
        System.out.println("Hint: X: " + x + " Y: " + y + " Valor: " + gameEngine.getSolutionTable(x, y));
    }

    /**
     * Method searches for a cell that doesn't match the solution
     *
     * @return if has invalid cell ? invalid cell : null
     */
    private SudokuCell tableHasUnmatchedCell() {
        return GameEngine.getInstance().currentGameIsNotMatchingSolution();
    }

    /*
     * Functions onClicks - Dialog give up
     */

    /**
     * @param view - Button Save and Leave from giveup pop up
     */
    public void onLeaveSave(View view) {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            }, 1234);
        } else {
            if (SaveGame())
                backToMain();
        }
    }

    /**
     * @param view - Button Leave from giveup pop up
     */
    public void onLeave(View view) {
        backToMain();
    }

    /**
     * @param view - Button Cancel from giveup pop up
     */
    public void onCancel_Leave(View view) {
        giveupDialog.dismiss();
    }

    /**
     * Return to Main Menu / Screen
     */
    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1234) {
            if (SaveGame())
                backToMain();
        }
    }


    /**
     * @return <code>boolean</code> <code>true</code> if game has been saved, <code>false</code> if
     * no
     */
    private boolean SaveGame() {
        TextView tv = giveupDialog.findViewById(R.id.tv_fileName);

        boolean saved;

        String filename = tv.getText().toString();

        if (filename.trim().equals(" ") || filename.isEmpty()) {
            Toast.makeText(this, "Please put a name for the game!",
                    Toast.LENGTH_SHORT).show();
        } else {
            SavedGames save = new SavedGames(this);

            saved = save.saveGame(gameEngine, filename);

            if (saved) {
                Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void onFinish(View view) {
        backToMain();
    }
}
