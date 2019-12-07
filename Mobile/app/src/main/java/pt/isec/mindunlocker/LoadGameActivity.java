package pt.isec.mindunlocker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class LoadGameActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv;
    private SavedGames saves;

    private static final String FILENAME = "filename";
    private static final String LEVEL = "level";
    private static final String TIME = "time";
    private static final String HINTS = "hints";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadgame);

        lv = findViewById(R.id.lv_loadGame);

        ArrayList<HashMap> list = new ArrayList<>();
        saves = new SavedGames(this);

        HashMap mapAux;

        File[] files = saves.getAllSavedGames();

        if (files != null && files.length > 0) {
            GameplayActivity gAAux;

            for (File f : files) {
                mapAux = new HashMap();

                gAAux = saves.loadGame(f.getName());

                if (gAAux != null) {
                    mapAux.put(FILENAME, f.getName());
                    mapAux.put(LEVEL, gAAux.level);
                    mapAux.put(TIME, gAAux.minutes + ":" + gAAux.seconds);
                    mapAux.put(HINTS, gAAux.gameEngine.getHints());
                    list.add(mapAux);
                }
            }
        } else {

        }

        mapAux = new HashMap();

        mapAux.put(FILENAME, "awdawd");
        mapAux.put(LEVEL, "awdawd");
        mapAux.put(TIME, "awdawdawd");
        mapAux.put(HINTS, "aaaaa");
        list.add(mapAux);

        listviewAdapter adapter = new listviewAdapter(this, list);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) lv.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), clickedItem, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    class listviewAdapter extends BaseAdapter {
        public ArrayList<HashMap> list;
        Activity activity;

        public listviewAdapter(Activity activity, ArrayList<HashMap> list) {
            super();
            this.activity = activity;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        private class ViewHolder {
            TextView txtFirst;
            TextView txtSecond;
            TextView txtThird;
            TextView txtFourth;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.load_row, null);
                holder = new ViewHolder();
                holder.txtFirst = convertView.findViewById(R.id.filename_load);
                holder.txtSecond = convertView.findViewById(R.id.level_load);
                holder.txtThird = convertView.findViewById(R.id.time_load);
                holder.txtFourth = convertView.findViewById(R.id.hints_load);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap map = list.get(position);
            holder.txtFirst.setText((String) map.get(FILENAME));
            holder.txtSecond.setText((String) map.get(LEVEL));
            holder.txtThird.setText((String) map.get(TIME));
            holder.txtFourth.setText((String) map.get(HINTS));

            return convertView;
        }

    }

}

