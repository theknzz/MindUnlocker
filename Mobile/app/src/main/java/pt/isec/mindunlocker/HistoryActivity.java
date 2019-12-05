package pt.isec.mindunlocker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.GoalRow;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pt.isec.mindunlocker.api.Game;


public class HistoryActivity extends AppCompatActivity {
    private Context context;
    TableLayout tlGameHistory = null;
    private List<String[]> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_refactor);
        tlGameHistory = findViewById(R.id.tlGameHistory);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        TableRow tr = new TableRow(this);


        TextView tvPoints = new TextView(this);
        tvPoints.setText("Points");

        tvPoints.setWidth(200);
        TextView tvDifficulty = new TextView(this);
        tvDifficulty.setText("Dificulty");
        tvDifficulty.setWidth(200);
        TextView tvHints = new TextView(this);
        tvHints.setText("Hints");

        tvHints.setWidth(200);
        TextView tvDuration = new TextView(this);
        tvDuration.setText("Duration");

        tr.addView(tvPoints);
        tr.addView(tvDifficulty);
        tr.addView(tvHints);
        tr.addView(tvDuration);
        tr.setPadding(10,10,10,10);
        tr.setBackgroundColor(Color.GRAY);

        tlGameHistory.addView(tr);
        getApiData();
    }

    /**
     * Makes the call to the backend api in other to get the list of games done by the actual player
     */
    private void getApiData() {
        sentData(1, 1, "easy", 1);
    }


    /**
     * Parse the difficulty identifier, got from the backend api, to a String
     *
     * @param id - difficulty identifier
     * @return String correspondent to the difficulty identifier
     */
    private String parseDifficulty(int id) {
        switch (id) {
            case 0:
                return "Easy";
            case 1:
                return "Medium";
            case 2:
                return "Hard";
        }
        return null;
    }


    /**
     * Method that populates a InsertGameBody class with the user's game information and sent it back to the backend
     *
     * @param points     - user's game points
     * @param duration   - user's game duration
     * @param difficulty - user's game difficulty
     * @param hints      - user's game nr. of hints
     */
    public void sentData(int points, int duration, String difficulty, int hints) {
        // creating the request body

        // converting the class with the body into json
        String parameters = new Gson().toJson("cenas");

        if (new RetrieveFeedTask(this).doInBackground("https://mindunlocker20191126085502.azurewebsites.net/api/Games/allGames", parameters))
            //TODO existe output quando o post é feito com sucesso ?
            System.out.println("Leaderboard updated");
        System.out.println("Something went wrong");
    }

    /**
     * Async static class the talks to the backend api in order to post the information
     */
    public class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

        HistoryActivity historyActivity;

        /**
         * Insert a TableRow with the information of each game inside the TableLayout
         *
         * @param gameId     - The game's id
         * @param points     - The game's points
         * @param difficulty - The game's difficulty
         * @param hints      - The game's hints
         * @param duration   - The game's duration
         */
        private void addGame(int gameId, int points, String difficulty, int hints, int duration) {
            TableRow tr = new TableRow(historyActivity);


            TextView tvPoints = new TextView(historyActivity);
            tvPoints.setText(String.valueOf(points));

            tvPoints.setWidth(200);
            TextView tvDifficulty = new TextView(historyActivity);
            tvDifficulty.setText(difficulty);

            TextView tvHints = new TextView(historyActivity);
            tvHints.setText(String.valueOf(hints));

            TextView tvDuration = new TextView(historyActivity);
            tvDuration.setText(String.valueOf(duration));

            tr.addView(tvPoints);
            tr.addView(tvDifficulty);
            tr.addView(tvHints);
            tr.addView(tvDuration);
            tr.setPadding(10,10,10,10);
            tr.setBackgroundColor(Color.GREEN);
            tr.setTop(15);
            tlGameHistory.addView(tr);
        }

        public RetrieveFeedTask(HistoryActivity h) {
            historyActivity = h;
        }

        @Override
        public Boolean doInBackground(String... params) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + Token.CONTENT);
                connection.setDoOutput(true);

                String line;
                //create your inputsream
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                //read in the data from input stream, this can be done a variety of ways

                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<Game>>() {
                }.getType();

                List<Game> games = gson.fromJson(sb.toString(), listType);

                TableLayout tlGameHistory = null;

                tlGameHistory = findViewById(R.id.tlGameHistory);

                for (Game g : games) {

                    addGame(0, g.getPoints(), parseDifficulty(Integer.parseInt(g.getDificulty())), g.getHints(),g.getDuration());
                    System.out.println(g.getDificulty());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}

