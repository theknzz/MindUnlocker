package pt.isec.mindunlocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pt.isec.mindunlocker.leaderboard.MainView;
import pt.isec.mindunlocker.login.LoginActivity;
import pt.isec.mindunlocker.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout header, headerLogin;
    private ScrollView leaderboard;
    private Button login, register, load, startGame, history, logOut;
    private static boolean firstTime = true; //check if first time in main
    private MainView leaderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        getViews();

        leaderContainer.getLeaderBoard();

        setListeners();

        if (firstTime) {
            hideComponents();
            firstTime = false;
        } else {
            Bundle response = getIntent().getExtras();

            if (response == null) return;

            if (!"login".equals(response.getString("result"))) return;

            loginResult(response);
        }
    }


    private void getViews() {
        header = findViewById(R.id.header);
        headerLogin = findViewById(R.id.headerLogin);
        leaderboard = findViewById(R.id.leaderboard);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnCreateAcc);
        load = findViewById(R.id.btnLoadGame);
        startGame = findViewById(R.id.btnGenerateGame);
        history = findViewById(R.id.btnHistory);
        logOut = findViewById(R.id.btnLogOut);

        leaderContainer = new MainView(findViewById(R.id.leadercontainer), this);
    }

    private void setListeners() {
        setButtonListener(login, LoginActivity.class);
        setButtonListener(register, RegisterActivity.class);
        setButtonListener(startGame, GameplayActivity.class);
        setButtonListener(history, HistoryActivity.class);
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
        load.setVisibility(View.GONE);
        history.setVisibility(View.GONE);
        logOut.setVisibility(View.GONE);

        header.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
    }

    private void showComponents() {
        headerLogin.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
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