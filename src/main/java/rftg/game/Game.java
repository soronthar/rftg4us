package rftg.game;

import rftg.game.cards.DesignPile;

public class Game {
    public long randomSeed;
    public int playerCount;
    public int expansionLevel;
    public boolean advanced;
    public boolean goalDisabled;
    public boolean takeoverDisabled;
    public Player[] players;
    public DesignPile drawPile;
    public DesignPile discardPile;

    public void initPlayers() {
        players = new Player[playerCount];
    }
}
