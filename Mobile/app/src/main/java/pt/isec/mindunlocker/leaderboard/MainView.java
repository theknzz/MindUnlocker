package pt.isec.mindunlocker.leaderboard;


import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;


public class MainView {
    private Context context;
    private TableView<String []> container;
    private List<String []> data = new ArrayList<>();

    public MainView(TableView<String []> container, final Context context) {
        this.container = container;
        this.context = context;

        TableColumnWeightModel columnModel = new TableColumnWeightModel(8);

        for(int i = 0; i < 8; i++){
            if(i == 1)
                columnModel.setColumnWeight(i, 2);

            columnModel.setColumnWeight(i, 1);
        }

        container.setColumnModel(columnModel);

    }

    public void displayData(){
        String [][] newData = new String[data.size()][];

        for(int i = 0; i < newData.length; i++){
            newData[i] = data.get(i);
        }

        container.setDataAdapter(new SimpleTableDataAdapter(context, data));
    }

    public void addRanking(String name, int points, int totalGames, int hints, int hard, int medium, int easy, int rank){
        data.add(new String[]{String.valueOf(rank), name, String.valueOf(hints), String.valueOf(easy),
                String.valueOf(medium), String.valueOf(hard), String.valueOf(totalGames), String.valueOf(30)});

        System.out.println(points);

//        UserRow newRow = new UserRow(context);
//        newRow.initParams(name, points, totalGames, hints, hard, medium, easy, rank);
//        newRow.setView();
//
//        container.addView(newRow);
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
                int easy = gamesPlayed.getInt("Easy");
                int medium = gamesPlayed.getInt("Medium");
                int hard = gamesPlayed.getInt("Hard");

                addRanking(name, points, totalGames, hints, hard, medium, easy, 1);
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