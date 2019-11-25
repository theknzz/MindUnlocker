package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout header;
    private LinearLayout leaderboard;
    private Button login;
    private Button register;
    private Button load;
    private Button startGame;
    private Button history;
    private static boolean firstTime = true; //check if first time in main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        getViews();

        setListeners();

        if(firstTime) {
            hideComponents();
            firstTime = false;
        }else{
            loginResult();
        }
    }

    private void getViews(){
        header = findViewById(R.id.header);
        leaderboard = findViewById(R.id.leaderboard);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnCreateAcc);
        load = findViewById(R.id.btnLoadGame);
        startGame = findViewById(R.id.btnGenerateGame);
        history = findViewById(R.id.btnHistory);
    }

    private void setListeners(){
        setButtonListener(login, LoginActivity.class);
        setButtonListener(register, RegisterActivity.class);
        setButtonListener(startGame, GameplayActivity.class);
        setButtonListener(history, HistoryActivity.class);
    }


    private void setButtonListener(Button button, final Class activity){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(activity);
            }
        });
    }

    private void loginResult(){
        Bundle response = getIntent().getExtras();

        if(response == null) return;

        boolean temp = response.getBoolean("");

        if(temp)
            showComponents();
        else
            hideComponents();
    }

    private void hideComponents(){
        header.setVisibility(View.INVISIBLE);
        leaderboard.setVisibility(View.GONE);
        load.setVisibility(View.GONE);

        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
    }

    private void showComponents(){
        header.setVisibility(View.VISIBLE);
        leaderboard.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);

        login.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
    }

    private void changeActivity(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}