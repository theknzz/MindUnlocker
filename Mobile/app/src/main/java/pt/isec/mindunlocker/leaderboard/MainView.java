package pt.isec.mindunlocker.leaderboard;


import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainView {
    private Context context;
    private LinearLayout container;

    public MainView(LinearLayout container, final Context context) {
        this.container = container;
        this.context = context;
    }

    public void addRanking(String name, int points, int totalGames, int hints, int hard, int medium, int easy, int rank){
        UserRow newRow = new UserRow(context);
        newRow.initParams(name, points, totalGames, hints, hard, medium, easy, rank);
        newRow.setView();

        container.addView(newRow);
    }

    public void cleanRanking(){
        container.removeAllViews();
    }

    public void getLeaderBoard(){
        JSONArray data = new RetrieveFeedTask(context)
                .doInBackground("https://mindunlocker20191126085502.azurewebsites.net/api/Leaderboard");

        if(data == null) return;

        try {

            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

                String name = object.getString("Name");
                int points = object.getInt("Points");
                int totalGames = object.getInt("TotalGames");
                int hints = object.getInt("Hints");

                JSONObject gamesPlayed = object.getJSONObject("GamesPlayed");
                int hard = gamesPlayed.getInt("Hard");
                int medium = gamesPlayed.getInt("Hard");
                int easy = gamesPlayed.getInt("Hard");

                addRanking(name, points, totalGames, hard, medium, easy, hints, 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class RetrieveFeedTask extends AsyncTask<String, Void, JSONArray> {
        private Context context;

        public RetrieveFeedTask(Context context){
            this.context = context;
        }

        @Override
        protected JSONArray doInBackground(String... param) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = new URL(param[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");

                System.out.println(connection.getResponseCode());

                String line;

                InputStreamReader isr;
                isr = new InputStreamReader(connection.getInputStream());

                //read in the data from input stream, this can be done a variety of way

                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                //get the string version of the response data
                return new JSONArray(sb.toString());
            }catch(Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return null;
        }
    }
}