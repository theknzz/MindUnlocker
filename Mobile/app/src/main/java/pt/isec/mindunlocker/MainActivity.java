package pt.isec.mindunlocker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import pt.isec.mindunlocker.leaderboard.LeaderboardContainer;

public class MainActivity extends AppCompatActivity {
    private Dialog startGameDialog;
    private LinearLayout header, headerLogin;
    private Button login, register, load, customizedGame, history, logOut;
    private LeaderboardContainer leaderContainer;

    private static String username/*, score, rank*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        startGameDialog = new Dialog(this);

        getViews();

        leaderContainer.getLeaderBoard();
        leaderContainer.displayData();

        setListeners();

        hideComponents();

        if (Token.CONTENT != null) {

            Bundle response = getIntent().getExtras();
            if (response != null && "login".equals(response.getString("result")))
                loginResult(response);

            TextView tv_user = findViewById(R.id.user);
            TextView tv_score = findViewById(R.id.score);
            TextView tv_ranking = findViewById(R.id.ranking);

            String [] info = leaderContainer.getUserInfo(String.valueOf(username));

            tv_user.setText(username);
            tv_score.setText(info[0]);
            tv_ranking.setText(info[0]);

            showComponents();
        }
    }

    private void getViews() {
        header = findViewById(R.id.header);
        headerLogin = findViewById(R.id.headerLogin);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnCreateAcc);
        load = findViewById(R.id.btnLoadGame);
        customizedGame = findViewById(R.id.btnCreateGame);
        history = findViewById(R.id.btnHistory);
        logOut = findViewById(R.id.btnLogOut);

        leaderContainer = new LeaderboardContainer(findViewById(R.id.leadercontainer), this);
    }

    private void setListeners() {
        setButtonListener(login, LoginActivity.class);
        setButtonListener(register, RegisterActivity.class);
        setButtonListener(customizedGame, CustomizedGameActivity.class);
        setButtonListener(history, HistoryActivity.class);
        setButtonListener(load, LoadGameActivity.class);
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

            username = response.getString("user");
            //score = response.getString("score");
            //rank = response.getString("ranking");

        }
    }

    private void hideComponents() {
        headerLogin.setVisibility(View.GONE);
        history.setVisibility(View.GONE);
        logOut.setVisibility(View.GONE);

        header.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
    }

    private void showComponents() {
        headerLogin.setVisibility(View.VISIBLE);
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
        Token.CONTENT = username /*= score = rank*/ = null;

        hideComponents();
    }

    @Override
    public void onBackPressed() {
    }
}