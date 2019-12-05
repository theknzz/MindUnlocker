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

    /**
     * Method that populates a InsertGameBody class with the user's game information and sent it back to the backend
     * @param points - user's game points
     * @param duration - user's game duration
     * @param difficulty - user's game difficulty
     * @param hints - user's game nr. of hints
     */
    public void sentData(int points, int duration, String difficulty, int hints) {
        // creating the request body

        // converting the class with the body into json
        String parameters = new Gson().toJson("a");

        if (new RetrieveFeedTask().doInBackground("https://mindunlocker20191126085502.azurewebsites.net/api/Games", parameters))
            //TODO existe output quando o post é feito com sucesso ?
            System.out.println("Leaderboard updated");
        System.out.println("Something went wrong");
    }

    /**
     * Async static class the talks to the backend api in order to post the information
     */
        public static class RetrieveFeedTask extends AsyncTask<String, Void, Boolean> {

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


