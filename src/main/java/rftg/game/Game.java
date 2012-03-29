package rftg.game;

import rftg.bundle.cards.CardsDesigns;
import rftg.game.ai.EmptyAI;
import rftg.game.cards.CardPile;

import java.util.Random;

import static rftg.game.Constants.*;

public class Game {
    public CardsDesigns designs = new CardsDesigns();
    public CardPile drawPile;
    public CardPile discardPile;

    public long random_seed;
    long start_seed;

    int session_id;
    boolean simulation;
    int sim_who;

    public int expanded = 0;
    public boolean advanced = false;
    public boolean goal_disabled = true;
    public boolean takeover_disabled = true;
    private Player[] p;

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

    public Game() {
        this(GameConf.DEFAULT_CONF);
    }

    public Game(GameConf conf) {
        initGameParameters(conf);
        initRandom();
        initPlayers(conf);
    }

    private void initRandom() {
        random = new Random(random_seed);
    }

    private void initGameParameters(GameConf conf) {
        this.random_seed = conf.seed;
        this.expanded = conf.expanded;
        this.advanced = conf.advanced;
        this.goal_disabled = conf.goal_disabled;
        this.takeover_disabled = conf.takeover_disabled;
        this.designs.loadFrom(conf.cardFile);
    }

    private void initPlayers(GameConf conf) {
        this.p = new Player[conf.num_players];

        for (int i = 0; i < this.p.length; i++) {
            Player player = new Player();
            /* Set player name */
            player.name = "Player " + i;

            /* Set player interfaces to AI functions */
            player.control = new EmptyAI();

            /* Initialize AI */
            player.control.init(this, player, conf.factor);

            /* Clear choice log size and position */
            player.choice_size = 0;
            player.choice_pos = 0;
            this.p[i] = player;
        }
    }


    public int game_rand() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    public int getNumPlayers() {
        return this.p.length;
    }

    public Player getPlayer(int index) {
        return this.p[index];
    }

    public void setPlayer(int index, Player p) {
        this.p[index] = p;
    }
}
