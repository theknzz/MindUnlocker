package pt.isec.mindunlocker.leaderboard;


import android.content.Context;
import android.graphics.Color;
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
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class LeaderboardContainer {
    private Context context;
    private TableView<String []> container; //view de uma libraria importada
    private List<String []> data = new ArrayList<>();

    /**
     * Initialize model of table and header
     *
     * @param container object of de.codecrafters.tableview.TableView
     * @param context context of the view where is call
     */

    public LeaderboardContainer(TableView<String []> container, final Context context) {
        this.container = container;
        this.context = context;

        TableColumnWeightModel columnModel = new TableColumnWeightModel(7); //modelo para a tabela

        columnModel.setColumnWeight(0, 2); //set weight for first string

        container.setColumnModel(columnModel);

        container.setHeaderBackgroundColor(Color.parseColor("#ffffff")); //cor do header da tabela

        SimpleTableHeaderAdapter sTHA = new SimpleTableHeaderAdapter(context, "Name", "Hints", "Easy", "Medium",
                "Hard", "Total", "Points"); //data do header
        sTHA.setTextSize(10); // tamanho da letra do header

        container.setHeaderAdapter(sTHA);
    }

    /**
     * add data in List<String[]> data to view in SimpleTableDataAdpter
     */

    public void displayData(){
        String [][] newData = new String[data.size()][];

        for(int i = 0; i < newData.length; i++){
            newData[i] = data.get(i);
        }

        SimpleTableDataAdapter sTDA = new SimpleTableDataAdapter(context, data); // data para o body da tabela(recebe o contexto e um array 2D)
        sTDA.setTextSize(10); // tamanho da letra do body
        sTDA.setTextColor(Color.WHITE); // cor da letra

        container.setDataAdapter(sTDA);
    }

    public void addRanking(String name, int points, int totalGames, int hints, int hard, int medium, int easy, int rank){
        data.add(new String[]{rank + " " + name, String.valueOf(hints), String.valueOf(easy),
                String.valueOf(medium), String.valueOf(hard), String.valueOf(totalGames), String.valueOf(points)});
    }

    /**
     * clean views after second time getLeaderboard() is call
     */

    public void cleanRanking(){
        data = new ArrayList<>();
        container.removeAllViews();
    }

    /**
     * Get list data from API about leaderboard
     */

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

                addRanking(name, points, totalGames, hints, hard, medium, easy, i+1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Async task that talks to the backend api in order to get the need information
     */
    static class RetrieveFeedTask extends AsyncTask<String, Void, JSONArray> {
        private Context context;

        public RetrieveFeedTask(Context context){
            this.context = context;
        }

        /**
         * Get values from api to a JSONArray
         * @param param url to communicate with API
         * @return JSONArray with data of players to leaderboard or null if occur an error
         */

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