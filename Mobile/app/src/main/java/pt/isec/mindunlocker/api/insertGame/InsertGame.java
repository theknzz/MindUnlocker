package pt.isec.mindunlocker.api.insertGame;

import android.os.AsyncTask;
import android.os.StrictMode;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.isec.mindunlocker.Token;

public class InsertGame {
    public void sentData(int points, int duration, String difficulty, int hints) {

        // creating the request body
        InsertGameBody body = new InsertGameBody(points, duration, difficulty, hints);

        // converting the class with the body into json
        String parameters = new Gson().toJson(body);

        if (new RetrieveFeedTask().doInBackground("https://mindunlocker20191126085502.azurewebsites.net/api/Games", parameters))
            //TODO existe output quando o post é feito com sucesso ?
            System.out.println("Leaderboard updated");
        System.out.println("Something went wrong");
    }

        static class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    URL url = new URL(params[0]);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "Bearer " + Token.CONTENT);
                    connection.setDoOutput(false);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(params[1]);
                    writer.close();

                    connection.connect();
                    int code = connection.getResponseCode();
                    return code==201;

                }catch(Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
    }
}

class InsertGameBody {
    private int Points;
    private int Duration;
    private String Dificulty;
    private int Hints;

    public InsertGameBody(int points, int duration, String difficulty, int hints) {
        this.Points = points;
        this.Duration = duration;
        this.Dificulty = difficulty;
        this.Hints = hints;
    }
}
