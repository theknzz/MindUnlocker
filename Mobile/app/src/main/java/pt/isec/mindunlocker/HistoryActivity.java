package pt.isec.mindunlocker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private Context context;
    TableLayout tlGameHistory = null;
    private List<String []> data = new ArrayList<>();

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

        getApiData();
    }

    /**
     * Makes the call to the backend api in other to get the list of games done by the actual player
     */
    private void getApiData() {
        JSONArray data = new RetrieveFeedTask()
                .doInBackground("https://mindunlocker20191126085502.azurewebsites.net/api/Games");
        if (data == null) return;
        try {
            for (int i=0; i<data.length(); i++) {
                JSONObject object = data.getJSONObject(i);

                int gameId = object.getInt("ID");
                int points = object.getInt("Points");
                int difficulty = object.getInt("Dificulty");
                int hints = object.getInt("Hints");
                int duration = object.getInt("Duration");

                String dif = parseDifficulty(difficulty);
                if (dif != null)
                    addGame(gameId, points, dif, hints, duration);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a TableRow with the information of each game inside the TableLayout
     * @param gameId - The game's id
     * @param points - The game's points
     * @param difficulty - The game's difficulty
     * @param hints - The game's hints
     * @param duration - The game's duration
     */
    private void addGame(int gameId, int points, String difficulty, int hints, int duration) {
        TableRow tr = new TableRow(this);

        TextView tvId = new TextView(this);
        tvId.setText(String.valueOf(gameId));

        TextView tvPoints = new TextView(this);
        tvPoints.setText(String.valueOf(points));

        TextView tvDifficulty = new TextView(this);
        tvDifficulty.setText(difficulty);

        TextView tvHints = new TextView(this);
        tvHints.setText(String.valueOf(hints));

        TextView tvDuration = new TextView(this);
        tvDuration.setText(String.valueOf(duration));

        tr.addView(tvId);
        tr.addView(tvPoints);
        tr.addView(tvDifficulty);
        tr.addView(tvHints);
        tr.addView(tvDuration);
    }

    /**
     * Parse the difficulty identifier, got from the backend api, to a String
     * @param id - difficulty identifier
     * @return String correspondent to the difficulty identifier
     */
    private String parseDifficulty(int id) {
        switch (id) {
            case 0: return "Easy";
            case 1: return "Medium";
            case 2: return "Hard";
        }
        return null;
    }

    /**
     * Async task that talks to the backend api in order to get the need information
     */
    static class RetrieveFeedTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... param) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = new URL(param[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + Token.CONTENT);
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                connection.setRequestProperty("Accept", "*/*");

                String line;

                InputStreamReader isr;
                InputStream inputStream;
                int status = connection.getResponseCode();

                if (status != HttpURLConnection.HTTP_OK)  {
                    inputStream = connection.getErrorStream();
                }
                else  {
                    inputStream = connection.getInputStream();
                }


                isr = new InputStreamReader(connection.getInputStream());

                //read in the data from input stream, this can be done a variety of way

                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                //get the string version of the response data
                return new JSONArray(sb.toString());
            } catch (Exception e) {
//                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return null;
        }
    }
}
