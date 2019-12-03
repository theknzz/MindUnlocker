package pt.isec.mindunlocker.leaderboard;

public class GamesPlayed {
    private int hard, medium, easy;

    public GamesPlayed(int hard, int medium, int easy){
        this.hard = hard;
        this.medium = medium;
        this.easy = easy;
    }

    public int getHard(){
        return hard;
    }

    public int getEasy() {
        return easy;
    }

    public int getMedium() {
        return medium;
    }
}
