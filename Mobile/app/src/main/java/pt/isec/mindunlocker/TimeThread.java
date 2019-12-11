package pt.isec.mindunlocker;

import android.os.Handler;
import android.widget.TextView;

import java.io.Serializable;

public class TimeThread implements Runnable {

    private int seconds;
    private int minutes;
    private Handler timerHandler = null;
    private TextView timerTextView = null;

    public TimeThread(int minutes, int seconds, Handler timerHandler, TextView timerTextView) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.timerHandler = timerHandler;
        this.timerTextView = timerTextView;
    }

    @Override
    public void run() {
            seconds++;
            minutes = seconds / 60;
            seconds = seconds % 60;

            //The correct-multiplier starts at 50 and is decremented (-1) every 30 seconds.
            /*if (seconds == 30 || seconds == 0) {
                GameEngine.getInstance().decrementCM();
            }*/

            timerTextView.setText(String.format("time: %d:%02d", minutes, seconds));

            GameEngine.getInstance().setFinalTime(minutes, seconds);

            timerHandler.postDelayed(this, 1000);
    }
}
