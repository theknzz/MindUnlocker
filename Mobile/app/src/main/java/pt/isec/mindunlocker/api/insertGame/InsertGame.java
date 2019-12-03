package pt.isec.mindunlocker.api.insertGame;

import android.os.AsyncTask;
import android.os.StrictMode;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
                    connection.setRequestProperty("Authorization", "Bearer " + "ckpERcqX5xGmkxGQVKSRSeJcZCeZ_cx2yjoQl_YtU_Wt_YMUzERsTcaHbqaKWgR-TXqIb1b6UwfM22RDWcz-o3NtGsGr46W25INh5whaNTOG-FFmhGICZ-7X6E206MoeKItPmwQMid5-ZHMg3xUDZMnKJ2s1YBKip09SS5m7RSPdz-oZN4Xfv9f2w4KPCsH6_DKYtXf6JPwu6sG4fW-_-CNPGLGj-purBy_gtTLwbC-PizqS2N101DiK3nrdzW_WZZ0z8xcGl2CEAUDaoeyoVBh3j0QN5SZ9fQx-iqZK632SWpHE4xmKJSpQP90TAtKvOBjsy0dRqZwNHSOQYKdV7ujvxMzmxRuRdu4LZuBaF2hijC0MKC7mbg_uIW85JHDops2TeWpfdsg3jso6Y9TZnokTVllLU63NCewOuA_xNtmxYUw2fU9-T1p-5moEs13OUA8sjeYjY4LyXIWCypSeJZmaDIiETzA0A8WSwx9JLeY");
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
