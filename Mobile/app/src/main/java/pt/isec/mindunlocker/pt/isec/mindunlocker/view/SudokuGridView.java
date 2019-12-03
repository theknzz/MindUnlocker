package pt.isec.mindunlocker.pt.isec.mindunlocker.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import pt.isec.mindunlocker.GameEngine;
import pt.isec.mindunlocker.R;

public class SudokuGridView extends GridView {

    private Context context;

    public SudokuGridView(final Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        this.context = context;

        SudokuGridViewAdapter gridViewAdapter = new SudokuGridViewAdapter(context);
        setAdapter(gridViewAdapter);

        setOnItemClickListener((parent, view, position, id) -> {
            int x = position % 9;
            int y = position / 9;

            GameEngine.getInstance().setSelectedPosition(x,y);
            GameEngine.getInstance().getTable().setItem(
                    GameEngine.getInstance().getSelectedPosX(),
                    GameEngine.getInstance().getSelectedPosY(),
                    GameEngine.getInstance().getN()
            );
            //GameEngine.getInstance().
            Log.i("Info","selected pos: " + x + "," + y);
            //Toast toast = Toast.makeText(context, "Selected item- x: " + x + " y: " + y, Toast.LENGTH_LONG);
            //toast.show();
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    class SudokuGridViewAdapter extends BaseAdapter {

        private Context context;

        public SudokuGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 81;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return GameEngine.getInstance().getTable().getItem(position);
        }
    }
}
