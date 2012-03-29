package rftg.game;

public class GameConf {
    public static final GameConf DEFAULT_CONF = new GameConf();

    public boolean verbose = false;
    public int num_players = 3;
    public boolean advanced = false;
    public int expanded = 0;
    public int n = 20;
    public long seed = System.currentTimeMillis();
    public String cardFile = "cards.txt";
    public boolean goal_disabled = true;
    public boolean takeover_disabled = true;
    public double factor = 1.0;
}
