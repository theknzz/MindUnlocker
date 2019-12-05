package pt.isec.mindunlocker;

import android.os.Handler;
import android.widget.TextView;

public class TimeThread implements Runnable {

    private int seconds = 0;
    private int minutes = 0;
    private long startTime = 0;
    private String finalTime = null;
    private Handler timerHandler = null;
    private TextView timerTextView = null;

    public TimeThread(long startTime, String finalTime, Handler timerHandler, TextView timerTextView) {
        this.startTime = startTime;
        this.finalTime = finalTime;
        this.timerHandler = timerHandler;
        this.timerTextView = timerTextView;
    }

    @Override
    public void run() {
        long millis = System.currentTimeMillis() - startTime;
        seconds = (int) (millis / 1000);
        minutes = seconds / 60;
        seconds = seconds % 60;

        //The correct-multiplier starts at 50 and is decremented (-1) every 30 seconds.
        if(seconds == 30 || seconds == 0){GameEngine.getInstance().decrementCM();}

        finalTime = minutes + ":" + seconds;
        timerTextView.setText(String.format("time: %d:%02d", minutes, seconds));

        timerHandler.postDelayed(this, 500);
    }
}
