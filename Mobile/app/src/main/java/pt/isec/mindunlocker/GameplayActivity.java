package pt.isec.mindunlocker;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Clock;
import java.util.List;
import java.util.Random;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

public class GameplayActivity extends AppCompatActivity implements View.OnClickListener {
    private Random rand = new Random();
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnGiveUp, btnHint, btnErase, btnPencil;

    Dialog finishDialog,giveupDialog;
    TextView timerTextView,scoreTextView,timeTextView;
    long startTime = 0;

    public String getFinalTime() {
        return finalTime;
    }

    String finalTime = null;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            finalTime = minutes + ":" + seconds;
            timerTextView.setText(String.format("time: %d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        GameEngine.getInstance().createTable(this);
        //printSudoku(solutionTable);

        // Set the timer counting
        timerTextView = findViewById(R.id.gameTimer);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        // Set Dialogs
        giveupDialog = new Dialog(this);
        giveupDialog.setContentView(R.layout.popup_giveup);

        finishDialog = new Dialog(this);
        finishDialog.setContentView(R.layout.popup_finish);
        scoreTextView = (TextView) finishDialog.findViewById(R.id.final_score);
        timeTextView = (TextView) finishDialog.findViewById(R.id.final_time);

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
    public void onClick(View v) {
        Button b = (Button)v;
        deselectAllOthers();
        switch(v.getId()){
            case R.id.giveUpBtn: /*Toast.makeText(this,"Exit",Toast.LENGTH_SHORT).show();*/
                giveupDialog.show();
                break;
            case R.id.hintBtn:/* Toast.makeText(this,"Showing Hint",Toast.LENGTH_SHORT).show();*/
                showHint();
                v.invalidate();
                break;
            case R.id.eraseBtn:/* Toast.makeText(this,"Delete: ON",Toast.LENGTH_SHORT).show();*/
                b.setSelected(true);
                btnPencil.setSelected(false);
                GameEngine.getInstance().setNumber(0);
                break;
            case R.id.pencilBtn:/* Toast.makeText(this,"Delete: ON",Toast.LENGTH_SHORT).show();*/
                if(b.isSelected()) {
                    b.setSelected(false);
                    GameEngine.getInstance().getTable().setPencilMode(true);
                }else{
                    b.setSelected(true);
                    GameEngine.getInstance().getTable().setPencilMode(true);
                }
                break;
            default:
                b.setSelected(true);
                GameEngine.getInstance().setNumber(Integer.parseInt(b.getText().toString()));
                Log.i("Info","selected num: " + b.getText().toString());
                //manda parar a thread do timer
        }
        if(GameEngine.getInstance().getTable().isFinish()){
            timerHandler.removeCallbacks(timerRunnable);
            scoreTextView.setText("1000000");
            timeTextView.setText(getFinalTime());
            finishDialog.show();
        }
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
        //btnPencil.setSelected(true);
    }

    public void showHint() {
        int x = rand.nextInt(9);
        int y = rand.nextInt(9);

        while(true){
            if(GameEngine.getInstance().getTable().getItem(x,y).getValue() == 0){
                GameEngine.getInstance().setNumber(GameEngine.getInstance().getSolutionTable(x,y));
                GameEngine.getInstance().setSelectedPosition(x,y);
                GameEngine.getInstance().setItem();
                break;
            }else{
                x = rand.nextInt(9);
                y = rand.nextInt(9);
            }
        }

        System.out.println("Hint: X: " + x + " Y: " + y + " Valor: " + GameEngine.getInstance().getSolutionTable(x,y));
    }
}
