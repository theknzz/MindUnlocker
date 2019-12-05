package pt.isec.mindunlocker.api;

public class Game {
    private int Points;
    private int Duration;
    private String Dificulty;
    private int Hints;

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getDificulty() {
        return Dificulty;
    }

    public void setDificulty(String dificulty) {
        Dificulty = dificulty;
    }

    public int getHints() {
        return Hints;
    }

    public void setHints(int hints) {
        Hints = hints;
    }
}
