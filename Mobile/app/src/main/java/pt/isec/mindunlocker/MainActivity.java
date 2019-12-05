package pt.isec.mindunlocker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
    private static boolean firstTime = true; //check if first time in main
    private LeaderboardContainer leaderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        startGameDialog = new Dialog(this);

        getViews();

        leaderContainer.getLeaderBoard();
        leaderContainer.displayData();

        setListeners();

        if (firstTime) {
            hideComponents();
            firstTime = false;
        } else {
            Bundle response = getIntent().getExtras();

            if (response == null) return;

            if ("login".equals(response.getString("result")))
                loginResult(response);

            if(Token.CONTENT != null){
                TextView user = findViewById(R.id.user);
                String [] info = leaderContainer.getUserInfo(String.valueOf(user.getText()));

                if(info != null){
                    setUserInfo(info[0], info[1]);
                }
            }
        }
    }

    private void setUserInfo(String rank, String score){
        TextView tScore = findViewById(R.id.score);
        TextView ranking = findViewById(R.id.ranking);

        ranking.setText(rank);
        tScore.setText(score);
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
            TextView user = findViewById(R.id.user);

            user.setText(response.getString("user"));

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