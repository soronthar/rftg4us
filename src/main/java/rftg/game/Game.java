package rftg.game;

import rftg.bundle.cards.CardsDesigns;
import rftg.game.cards.DesignPile;

import java.util.Random;

import static rftg.game.Constants.*;

public class Game {
    public CardsDesigns designs = new CardsDesigns();
    public DesignPile drawPile;
    public DesignPile discardPile;

    public long random_seed;
    long start_seed;

    int session_id;
    boolean simulation;
    int sim_who;

    public int num_players;
    public int expanded;
    public boolean advanced;
    public boolean goal_disabled;
    public boolean takeover_disabled;
    public Player[] p = new Player[MAX_PLAYER];

    int deck_size;

    /* Information about each card */
    Card[] deck = new Card[MAX_DECK];

    int vp_pool;

    /* Goals active in this game */
    boolean[] goal_active = new boolean[MAX_GOAL];

    /* Goals yet unclaimed */
    boolean[] goal_avail = new boolean[MAX_GOAL];

    /* Maximum progress toward a "most" goal */
    int[] goal_most = new int[MAX_GOAL];

    /* Number of pending takeovers */
    int num_takeover;

    /* Worlds targeted for takeover */
    int[] takeover_target = new int[MAX_TAKEOVER];

    /* Player attempting each takeover */
    int[] takeover_who = new int[MAX_TAKEOVER];

    /* Card holding takeover power */
    int[] takeover_power = new int[MAX_TAKEOVER];

    /* Takeover marked for failure */
    int[] takeover_defeated = new int[MAX_TAKEOVER];

    /* XXX Current kind of "any" good world */
    int oort_kind;

    /* Actions selected this round */
    int[] action_selected = new int[MAX_ACTION];

    /* Current action */
    int cur_action;

    /* Current player in phase */
    int turn;

    /* Current round number */
    int round;

    /* Game is over */
    boolean game_over;


    Random random;

    public void initPlayers() {
        for (int i = 0; i < this.num_players; i++) {
            p[i] = new Player();
        }
        random = new Random(random_seed);
    }

    public int game_rand() {
        return random.nextInt(Integer.MAX_VALUE);
    }
}
