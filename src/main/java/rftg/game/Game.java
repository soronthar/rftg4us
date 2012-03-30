package rftg.game;

import rftg.bundle.cards.CardsDesigns;
import rftg.game.ai.EmptyAI;
import rftg.game.cards.CardPile;
import rftg.game.cards.Design;

import java.util.Random;

import static rftg.game.Constants.*;

public class Game {
    public CardsDesigns designs = new CardsDesigns();
    public CardPile drawPile;
    public CardPile discardPile;

    public long random_seed;
    long start_seed;

    int session_id;
    public boolean simulation;
    public int sim_who;

    public int expanded;
    public boolean advanced;
    public boolean goal_disabled;
    public boolean takeover_disabled;
    private Player[] p;

    int vp_pool;

    /* Goals active in this game */
    boolean[] goal_active;

    /* Goals yet unclaimed */
    boolean[] goal_avail;

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
    int[] action_selected;

    /* Current action */
    public int cur_action;

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
        initDeck();
    }

    private void initDeck() {
        this.drawPile = new CardPile();
        this.discardPile = new CardPile();

        /* Loop over card designs */
        for (int i = 0; i < MAX_DESIGN; i++) {
            /* Get design pointer */
            Design design = this.designs.get(i);

            /* Get number of cards in use */
            int n = design.expand[this.expanded];

            /* Add cards */
            for (int j = 0; j < n; j++) {
                Card card = new Card();

                card.start_owner = card.owner = -1;

                card.start_where = card.where = WHERE_DECK;

                card.unpaid = false;
                card.known = 0;

                /* Clear used power list */
                card.used = new boolean[Constants.MAX_POWER];

                /* Card has not produced */
                card.produced = false;

                /* Set card's design */
                card.design = design;

                /* Card is not covered by a good */
                card.covered = -1;

                /* Card is not followed by any other */
                card.next = -1;

                this.drawPile.add(card);
            }
        }

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

    public void init() {
        Player player;
        Design design;
        Card card;
        int[] goal = new int[MAX_GOAL];
        int n;

        /* Save current random seed */
        this.start_seed = this.random_seed;

//        message_add("start seed: %d\n", this.start_seed);

        /* Game is not simulated */
        this.simulation = false;

        /* Set size of VP pool */
        this.vp_pool = this.getNumPlayers() * VP_SHIPS_PER_PLAYER;

        /* Increase size of pool in third expansion */
        if (this.expanded >= 3) this.vp_pool += 5;

        /* First game round */
        this.round = 1;

        /* No phase or turn */
        this.cur_action = -1;
        this.turn = 0;

        /* Clear selected actions */
        this.action_selected = new int[MAX_ACTION];


        /* Game is not over */
        this.game_over = false;

        /* Clear goals */
        this.goal_active = new boolean[MAX_GOAL];
        this.goal_avail = new boolean[MAX_GOAL];

        /* Clear number of pending takeovers */
        this.num_takeover = 0;

        /* Set Oort Cloud kind to "any" */
        this.oort_kind = GOOD_ANY;


        /* Add goals when expanded */
        if (this.expanded > 0 && !this.goal_disabled) {
            /* No goals available yet */
            n = 0;
            int j, k;
            /* Use correct "first" goals */
            if (this.expanded == 1) {
                /* First expansion only */
                j = GOAL_FIRST_5_VP;
                k = GOAL_FIRST_SIX_DEVEL;
            } else if (this.expanded == 2) {
                /* First and second expansion */
                j = GOAL_FIRST_5_VP;
                k = GOAL_FIRST_8_ACTIVE;
            } else {
                /* All expansions */
                j = GOAL_FIRST_5_VP;
                k = GOAL_FIRST_4_MILITARY;
            }

            /* Add "first" goals to list */
            for (int i = j; i <= k; i++) {
                /* Add goal to list */
                goal[n++] = i;
            }

            /* Select four "first" goals at random */
            for (int i = 0; i < 4; i++) {
                /* Choose goal at random */

                j = this.game_rand() % n;

                /* Goal is active */
                this.goal_active[goal[j]] = true;

                /* Goal is available */
                this.goal_avail[goal[j]] = true;

                /* Remove chosen goal from list */
                goal[j] = goal[--n];
            }

            /* No goals available yet */
            n = 0;

            /* Use correct "most" goals */
            if (this.expanded == 1) {
                /* First expansion only */
                j = GOAL_MOST_MILITARY;
                k = GOAL_MOST_PRODUCTION;
            } else if (this.expanded == 2) {
                /* First and second expansion */
                j = GOAL_MOST_MILITARY;
                k = GOAL_MOST_REBEL;
            } else {
                /* All expansions */
                j = GOAL_MOST_MILITARY;
                k = GOAL_MOST_CONSUME;
            }

            /* Add "most" goals to list */
            for (int i = j; i <= k; i++) {
                /* Add goal to list */
                goal[n++] = i;
            }

            /* Select two "most" goals at random */
            for (int i = 0; i < 2; i++) {
                /* Choose goal at random */
                j = this.game_rand() % n;

                /* Goal is active */
                this.goal_active[goal[j]] = true;

                /* Goal is available */
                this.goal_avail[goal[j]] = true;

                /* Remove chosen goal from list */
                goal[j] = goal[--n];
            }
        }

        /* Loop over players */
        for (int i = 0; i < this.getNumPlayers(); i++) {
            /* Get player pointer */
            player = this.getPlayer(i);

            /* Clear all claimed goals */
            player.goal_claimed = new boolean[MAX_GOAL];
            player.goal_progress = new int[MAX_GOAL];

            /* Player has no actions chosen */
            player.action[0] = player.prev_action[0] = -1;
            player.action[1] = player.prev_action[1] = -1;

            /* Player has not used prestige/search action */
            player.prestige_action_used = 0;

            /* Player has not used phase bonus */
            player.phase_bonus_used = 0;

            /* Player has no card to be placed */
            player.placing = -1;

            /* Player has no cards in any area */
            for (int j = 0; j < MAX_WHERE; j++) {
                /* Clear list head */
                player.head[j] = -1;
                player.start_head[j] = -1;
            }

            /* Player has no bonus military accrued */
            player.bonus_military = 0;

            /* Player has no bonus settle discount */
            player.bonus_reduce = 0;

            /* Player has not discarded any end of turn cards */
            player.end_discard = 0;

            /* No cards played yet */
            player.table_order = 0;

            /* Player has no prestige */
            player.prestige = player.prestige_turn = 0;

            /* Player has no points */
            player.vp = player.goal_vp = player.end_vp = 0;

            /* Player is not the winner */
            player.winner = 0;

            /* Player has earned no rewards this phase */
            player.phase_cards = player.phase_vp = 0;
            player.phase_prestige = 0;

            /* Player has no fake cards */
            player.fake_hand = player.total_fake = 0;
            player.fake_discards = 0;
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

    public int getDeckSize() {
        return this.drawPile.size();
    }

    //TODO: change this.
    public Card[] getDeck() {
        return this.drawPile.getCards();
    }
}
