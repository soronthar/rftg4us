package rftg.game;

public class Game {
    public long randomSeed;
    public int playerCount;
    public int expansionLevel;
    public boolean advanced;
    public boolean goalDisabled;
    public boolean takeoverDisabled;
    public Player[] players;

    public void initPlayers() {
        players = new Player[playerCount];
    }
}
