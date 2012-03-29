package rftg.game;

import java.util.ArrayList;
import java.util.List;

import static rftg.game.Constants.MAX_GOAL;
import static rftg.game.Constants.MAX_WHERE;

public class Player {
    /* Player's name/color */
    public String name;

    /* Ask player to make decisions */
    public Decisions control;

    /* Action(s) chosen */
    int[] action = new int[2];

    /* Previous turn action(s) */
    int[] prev_action = new int[2];

    /* Player has used prestige/search action */
    int prestige_action_used;

    /* Player has used phase bonus */
    int phase_bonus_used;

    /* Player's start world */
    int start;

    /* Player's first card of each location */
    int[] head = new int[MAX_WHERE];

    /* Player's first card of each location as of the start of the phase */
    int[] start_head = new int[MAX_WHERE];


    /* Card chosen in Develop or Settle phase */
    int placing;

    /* Bonus military accrued so far this phase */
    int bonus_military;

    /* Bonus settle discount accrued so far this phase */
    int bonus_reduce;

    /* Number of cards discarded at end of turn */
    int end_discard;

    /* Goal cards claimed */
    boolean[] goal_claimed = new boolean[MAX_GOAL];

    /* Progress toward each goal */
    int[] goal_progress = new int[MAX_GOAL];

    /* Prestige */
    int prestige;

    /* Prestige earned this turn */
    int prestige_turn;

    /* Victory point chips */
    int vp;

    /* Victory points from goals */
    int goal_vp;

    /* Total victory points (if game ended now) */
    public int end_vp;

    /* Player is the winner */
    int winner;

    /* Number of "fake" drawn cards in simulated games */
    int fake_hand;

    /* Total number of "fake" cards seen this turn */
    int total_fake;

    /* Number of cards discarded this turn but not removed from hand */
    int fake_discards;

    /* Counter for cards played */
    int table_order;

    /* Cards, VP, and prestige earned during the current phase */
    int phase_cards;
    int phase_vp;
    int phase_prestige;

    /* Log of player's choices */
    public int[] choice_log = new int[4096];

    /* Size and current position of choice log */
    public int choice_size;
    public int choice_pos;

    /* History of log sizes */
    List<Integer> choice_history = new ArrayList<Integer>();
}
