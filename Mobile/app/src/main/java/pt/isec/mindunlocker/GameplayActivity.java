package pt.isec.mindunlocker;

import android.os.Bundle;
<<<<<<< HEAD

import androidx.appcompat.app.AppCompatActivity;

public class GameplayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
=======
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameplayActivity extends AppCompatActivity  implements View.OnClickListener {
    private Random rand = new Random();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login);
        setContentView(R.layout.gameplay);

        GameEngine.getInstance().createTable(this);
        //printSudoku(solutionTable);

        Button btn1 = findViewById(R.id.selectNr1);
        Button btn2 = findViewById(R.id.selectNr2);
        Button btn3 = findViewById(R.id.selectNr3);
        Button btn4 = findViewById(R.id.selectNr4);
        Button btn5 = findViewById(R.id.selectNr5);
        Button btn6 = findViewById(R.id.selectNr6);
        Button btn7 = findViewById(R.id.selectNr7);
        Button btn8 = findViewById(R.id.selectNr8);
        Button btn9 = findViewById(R.id.selectNr9);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        Button btnGiveUp = findViewById(R.id.giveUpBtn);
        Button btnHint = findViewById(R.id.hintBtn);
        Button btnErase = findViewById(R.id.eraseBtn);
        btnGiveUp.setOnClickListener(this);
        btnHint.setOnClickListener(this);
        btnErase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.selectNr1:
                //Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(1);
                break;
            case R.id.selectNr2:
                //Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(2);
                break;
            case R.id.selectNr3:
                //Toast.makeText(this,"3",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(3);
                break;
            case R.id.selectNr4:
                //Toast.makeText(this,"4",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(4);
                break;
            case R.id.selectNr5:
                //Toast.makeText(this,"5",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(5);
                break;
            case R.id.selectNr6:
                //Toast.makeText(this,"6",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(6);
                break;
            case R.id.selectNr7:
                //Toast.makeText(this,"7",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(7);
                break;
            case R.id.selectNr8:
                //Toast.makeText(this,"8",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(8);
                break;
            case R.id.selectNr9:
                //Toast.makeText(this,"9",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(9);
                break;

            case R.id.giveUpBtn: Toast.makeText(this,"Exit",Toast.LENGTH_SHORT).show();
                //GameEngine.getInstance().setNumber(1);
                break;
            case R.id.hintBtn: Toast.makeText(this,"Showing Hint",Toast.LENGTH_SHORT).show();
                showHint();
                v.invalidate();
                break;
            case R.id.eraseBtn: Toast.makeText(this,"Delete: ON",Toast.LENGTH_SHORT).show();
                GameEngine.getInstance().setNumber(0);
                break;
        }
    }

    public void showHint() {
        int x = rand.nextInt(9);
        int y = rand.nextInt(9);

        while(true){
            if(GameEngine.getInstance().getTable().getItem(x,y).getValue() == 0){
                GameEngine.getInstance().setSelectedPosition(x,y);
                GameEngine.getInstance().setNumber(GameEngine.getInstance().getSolutionTable(x,y));
                break;
            }else{
                x = rand.nextInt(9);
                y = rand.nextInt(9);
            }
        }

        System.out.println("Hint: X: " + x + " Y: " + y + " Valor: " + GameEngine.getInstance().getSolutionTable(x,y));
>>>>>>> GameLogic-paris
    }
}
