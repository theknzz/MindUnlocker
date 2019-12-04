package pt.isec.mindunlocker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

import pt.isec.mindunlocker.pt.isec.mindunlocker.view.GameTable;

public class GameplayActivity extends AppCompatActivity implements View.OnClickListener {
    private Random rand = new Random();
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnGiveUp, btnHint, btnErase, btnPencil;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login);
        setContentView(R.layout.gameplay);

        GameEngine.getInstance().createTable(this);
        //printSudoku(solutionTable);

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
                //GameEngine.getInstance().setNumber(1);
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
