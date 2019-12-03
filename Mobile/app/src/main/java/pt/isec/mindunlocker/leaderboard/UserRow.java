package pt.isec.mindunlocker.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserRow extends LinearLayout {
    private String name;
    private int points, totalGames, hints, rank;
    private GamesPlayed difficulties;

    public UserRow(final Context context) {
        super(context);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    public void initParams(String name, int points, int totalGames, int hints, int hard, int medium, int easy, int rank){
        this.name = name;
        this.points = points;
        this.totalGames = totalGames;
        this.hints = hints;
        this.rank = rank;
        difficulties = new GamesPlayed(hard, medium, easy);
    }

    private LinearLayout createLinearLayout(int width, int height, int weight){
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        LayoutParams params = new LayoutParams(width, height);
        params.weight = weight;
        linearLayout.setLayoutParams(params);

        return linearLayout;
    }

    private TextView createTextView(int width, int heigth, String text){
        TextView textView = new TextView(this.getContext());
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView.setText(text);

        return textView;
    }

    private void setName(){
        LinearLayout linearLayout = createLinearLayout(0, LayoutParams.WRAP_CONTENT, 1);

        TextView tRank = createTextView(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, rank + "");
        tRank.setTypeface(tRank.getTypeface(), Typeface.BOLD);
        tRank.setTextColor(Color.rgb(214, 15, 15));

        linearLayout.addView(tRank);

        LayoutParams params = (LinearLayout.LayoutParams)tRank.getLayoutParams();
        params.leftMargin = 15;
        tRank.setLayoutParams(params);

        TextView tName = createTextView(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, name);
        tName.setTypeface(tRank.getTypeface(), Typeface.BOLD);

        linearLayout.addView(tName);

        params = (LinearLayout.LayoutParams)tName.getLayoutParams();
        params.leftMargin = 15;
        tName.setLayoutParams(params);


        this.addView(linearLayout);
    }

    private void setTextParameters(TextView newText){
        newText.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        LayoutParams params = (LinearLayout.LayoutParams)newText.getLayoutParams();
        params.weight = 1;
        newText.setLayoutParams(params);
    }

    private void setUserParameters(){
        LinearLayout linearLayout = createLinearLayout(0, LayoutParams.MATCH_PARENT, 2);

        TextView tHints = createTextView(0, LayoutParams.MATCH_PARENT, hints + "");
        linearLayout.addView(tHints);
        setTextParameters(tHints);

        LinearLayout linearLayout1 = createLinearLayout(0, LayoutParams.MATCH_PARENT, 1);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(linearLayout1);

        TextView tHard = createTextView(0, LayoutParams.MATCH_PARENT, difficulties.getHard() + "");
        linearLayout1.addView(tHard);
        setTextParameters(tHard);

        TextView tMedium = createTextView(0, LayoutParams.MATCH_PARENT, difficulties.getMedium() + "");
        linearLayout1.addView(tMedium);
        setTextParameters(tMedium);

        TextView tEasy = createTextView(0, LayoutParams.MATCH_PARENT, difficulties.getEasy()+ "");
        linearLayout1.addView(tEasy);
        setTextParameters(tEasy);

        TextView tTotalGames = createTextView(0, LayoutParams.MATCH_PARENT, totalGames + "");
        linearLayout.addView(tTotalGames);
        setTextParameters(tTotalGames);


        TextView tPoints = createTextView(0, LayoutParams.MATCH_PARENT, points + "");
        linearLayout.addView(tPoints);
        setTextParameters(tPoints);

        this.addView(linearLayout);
    }

    public void setView(){
        setName();
        setUserParameters();
    }
}
