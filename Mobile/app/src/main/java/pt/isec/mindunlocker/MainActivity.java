package pt.isec.mindunlocker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Dialog startGameDialog;
    private LinearLayout header, headerLogin;
    private ScrollView leaderboard;
    private Button login, register, load, customizedGame, history, logOut;
    private static boolean firstTime = true; //check if first time in main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        startGameDialog = new Dialog(this);

        getViews();

        setListeners();

        if (firstTime) {
            hideComponents();
            firstTime = false;
        } else {
            Bundle response = getIntent().getExtras();

            if (response != null) {
                if ("login".equals(response.getString("result"))) {
                    loginResult(response);
                }
            }
        }
    }

    private void getViews() {
        header = findViewById(R.id.header);
        headerLogin = findViewById(R.id.headerLogin);
        leaderboard = findViewById(R.id.leaderboard);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnCreateAcc);
        load = findViewById(R.id.btnLoadGame);
        customizedGame = findViewById(R.id.btnCreateGame);
        history = findViewById(R.id.btnHistory);
        logOut = findViewById(R.id.btnLogOut);
    }

    private void setListeners() {
        setButtonListener(login, LoginActivity.class);
        setButtonListener(register, RegisterActivity.class);
        setButtonListener(customizedGame, CustomizedGameActivity.class);
        setButtonListener(history, HistoryActivity.class);
    }

    public void onPickLevel(View v) {

        Intent intent = new Intent(MainActivity.this, GameplayActivity.class);
        Bundle bundle = new Bundle();

        Button b = (Button) v;
        switch (b.getId()) {
            case R.id.btn_easylevel:
                //Toast.makeText(this,"Starting an easy game",Toast.LENGTH_LONG).show();
                bundle.putInt("level", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_mediumlevel:
                //Toast.makeText(this,"Starting a medium game",Toast.LENGTH_LONG).show();
                bundle.putInt("level", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_hardlevel:
                //Toast.makeText(this,"Starting hard game",Toast.LENGTH_LONG).show();
                bundle.putInt("level", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

        startGameDialog.cancel();
    }

    public void onGameStart(View v) {
        startGameDialog.setContentView(R.layout.popup_start);
        startGameDialog.show();
    }

    private void setButtonListener(Button button, final Class activity) {
        button.setOnClickListener(v -> changeActivity(activity)); //mudar para java 8 nos modulos se der erro
    }

    private void loginResult(Bundle response) {
        boolean temp = response.getBoolean("success");

        if (temp) {
            //TODO obter da API valores reais apos login
            TextView user = findViewById(R.id.user);
            TextView score = findViewById(R.id.score);
            TextView ranking = findViewById(R.id.ranking);

            user.setText(response.getString("user"));
            score.setText(response.getString("score"));
            ranking.setText(response.getString("ranking"));

            showComponents();
        } else
            hideComponents();
    }

    private void hideComponents() {
        headerLogin.setVisibility(View.GONE);
        leaderboard.setVisibility(View.GONE);
        history.setVisibility(View.GONE);
        logOut.setVisibility(View.GONE);

        header.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
    }

    private void showComponents() {
        headerLogin.setVisibility(View.VISIBLE);
        leaderboard.setVisibility(View.VISIBLE);
        history.setVisibility(View.VISIBLE);
        logOut.setVisibility(View.VISIBLE);

        header.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
    }

    private void changeActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void logOut(View v) {
        Token.CONTENT = null;

        hideComponents();
    }

    @Override
    public void onBackPressed() {
    }
}
