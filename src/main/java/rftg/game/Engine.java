package rftg.game;

import rftg.bundle.cards.Flags;
import rftg.bundle.cards.GoodType;
import rftg.bundle.cards.powers.ConsumePowers;
import rftg.bundle.cards.powers.SettlePowers;
import rftg.game.cards.Design;
import rftg.game.cards.Power;

import static rftg.bundle.cards.Flags.*;
import static rftg.game.Constants.*;

public class Engine {
    public static void message_add(String format, Object... params) {
        System.out.printf(format, params);
    }

    public void init_game(Game g) {
        Player p_ptr;
        Design d_ptr;
        Card c_ptr;
        int[] goal = new int[MAX_GOAL];
        int i, j, k, n;

        /* Save current random seed */
        g.start_seed = g.random_seed;

        message_add("start seed: %d\n", g.start_seed);

        /* Game is not simulated */
        g.simulation = false;

        /* Set size of VP pool */
        g.vp_pool = g.num_players * 12;

        /* Increase size of pool in third expansion */
        if (g.expanded >= 3) g.vp_pool += 5;

        /* First game round */
        g.round = 1;

        /* No phase or turn */
        g.cur_action = -1;
        g.turn = 0;

        /* Clear selected actions */
        for (i = 0; i < MAX_ACTION; i++) g.action_selected[i] = 0;

        /* Game is not over */
        g.game_over = false;

        /* Clear goals */
        for (i = 0; i < MAX_GOAL; i++) {
            /* Goal is not active */
            g.goal_active[i] = false;

            /* Goal is not available */
            g.goal_avail[i] = false;
        }

        /* Clear number of pending takeovers */
        g.num_takeover = 0;

        /* Set Oort Cloud kind to "any" */
        g.oort_kind = GOOD_ANY;


        /* No cards in deck yet */
        g.deck_size = 0;

        /* Loop over card designs */
        for (i = 0; i < MAX_DESIGN; i++) {
            /* Get design pointer */
            d_ptr = g.designs.get(i);

            /* Get number of cards in use */
            n = d_ptr.expand[g.expanded];

            /* Add cards */
            for (j = 0; j < n; j++) {
                /* Get card pointer */
                c_ptr = new Card();

                /* No owner */
                c_ptr.start_owner = c_ptr.owner = -1;

                /* Put location in draw deck */
                c_ptr.start_where = c_ptr.where = WHERE_DECK;

                /* Card is not unpaid */
                c_ptr.unpaid = 0;

                /* Card's location is not known */
                c_ptr.known = 0;

                /* Clear used power list */
                for (k = 0; k < MAX_POWER; k++) c_ptr.used[k] = false;

                /* Card has not produced */
                c_ptr.produced = 0;

                /* Set card's design */
                c_ptr.d_ptr = d_ptr;

                /* Card is not covered by a good */
                c_ptr.covered = -1;

                /* Card is not followed by any other */
                c_ptr.next = -1;

                g.deck[g.deck_size] = c_ptr;
                g.deck_size++;
            }
        }

        /* Add goals when expanded */
        if (g.expanded > 0 && !g.goal_disabled) {
            /* No goals available yet */
            n = 0;

            /* Use correct "first" goals */
            if (g.expanded == 1) {
                /* First expansion only */
                j = GOAL_FIRST_5_VP;
                k = GOAL_FIRST_SIX_DEVEL;
            } else if (g.expanded == 2) {
                /* First and second expansion */
                j = GOAL_FIRST_5_VP;
                k = GOAL_FIRST_8_ACTIVE;
            } else {
                /* All expansions */
                j = GOAL_FIRST_5_VP;
                k = GOAL_FIRST_4_MILITARY;
            }

            /* Add "first" goals to list */
            for (i = j; i <= k; i++) {
                /* Add goal to list */
                goal[n++] = i;
            }

            /* Select four "first" goals at random */
            for (i = 0; i < 4; i++) {
                /* Choose goal at random */

                j = g.game_rand() % n;

                /* Goal is active */
                g.goal_active[goal[j]] = true;

                /* Goal is available */
                g.goal_avail[goal[j]] = true;

                /* Remove chosen goal from list */
                goal[j] = goal[--n];
            }

            /* No goals available yet */
            n = 0;

            /* Use correct "most" goals */
            if (g.expanded == 1) {
                /* First expansion only */
                j = GOAL_MOST_MILITARY;
                k = GOAL_MOST_PRODUCTION;
            } else if (g.expanded == 2) {
                /* First and second expansion */
                j = GOAL_MOST_MILITARY;
                k = GOAL_MOST_REBEL;
            } else {
                /* All expansions */
                j = GOAL_MOST_MILITARY;
                k = GOAL_MOST_CONSUME;
            }

            /* Add "most" goals to list */
            for (i = j; i <= k; i++) {
                /* Add goal to list */
                goal[n++] = i;
            }

            /* Select two "most" goals at random */
            for (i = 0; i < 2; i++) {
                /* Choose goal at random */
                j = g.game_rand() % n;

                /* Goal is active */
                g.goal_active[goal[j]] = true;

                /* Goal is available */
                g.goal_avail[goal[j]] = true;

                /* Remove chosen goal from list */
                goal[j] = goal[--n];
            }
        }

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {
            /* Get player pointer */
            p_ptr = g.p[i];

            /* Clear all claimed goals */
            for (j = 0; j < MAX_GOAL; j++) {
                /* Goal is unclaimed */
                p_ptr.goal_claimed[j] = false;

                /* No progress toward goal */
                p_ptr.goal_progress[j] = 0;
            }

            /* Player has no actions chosen */
            p_ptr.action[0] = p_ptr.prev_action[0] = -1;
            p_ptr.action[1] = p_ptr.prev_action[1] = -1;

            /* Player has not used prestige/search action */
            p_ptr.prestige_action_used = 0;

            /* Player has not used phase bonus */
            p_ptr.phase_bonus_used = 0;

            /* Player has no card to be placed */
            p_ptr.placing = -1;

            /* Player has no cards in any area */
            for (j = 0; j < MAX_WHERE; j++) {
                /* Clear list head */
                p_ptr.head[j] = -1;
                p_ptr.start_head[j] = -1;
            }

            /* Player has no bonus military accrued */
            p_ptr.bonus_military = 0;

            /* Player has no bonus settle discount */
            p_ptr.bonus_reduce = 0;

            /* Player has not discarded any end of turn cards */
            p_ptr.end_discard = 0;

            /* No cards played yet */
            p_ptr.table_order = 0;

            /* Player has no prestige */
            p_ptr.prestige = p_ptr.prestige_turn = 0;

            /* Player has no points */
            p_ptr.vp = p_ptr.goal_vp = p_ptr.end_vp = 0;

            /* Player is not the winner */
            p_ptr.winner = 0;

            /* Player has earned no rewards this phase */
            p_ptr.phase_cards = p_ptr.phase_vp = 0;
            p_ptr.phase_prestige = 0;

            /* Player has no fake cards */
            p_ptr.fake_hand = p_ptr.total_fake = 0;
            p_ptr.fake_discards = 0;
        }

    }

    public void begin_game(Game g) {
        Card c_ptr;
        int[] start = new int[MAX_DECK];
        int[] start_red = new int[MAX_DECK];
        int[] start_blue = new int[MAX_DECK];
        int[][] start_picks = new int[MAX_PLAYER][2];
        int[] hand = new int[MAX_DECK];
        boolean[] discarding = new boolean[MAX_PLAYER];
        int i = 0, j = 0, n = 0, ns = 0;
        int lowest = MAX_DECK, low_i = -1;
        int num_start = 0, num_start_red = 0, num_start_blue = 0;

        /* Loop over cards in deck */
        for (i = 0; i < g.deck_size; i++) {
            /* Get card pointer */
            c_ptr = g.deck[i];

            /* Check for start world */
            if (c_ptr.d_ptr.hasFlag(Flags.START)) {
                /* Add to list */
                start[num_start++] = i;
            }

            /* Check for red start world */
            if (c_ptr.d_ptr.hasFlag(Flags.START_RED)) {
                /* Add to list */
                start_red[num_start_red++] = i;
            }

            /* Check for blue start world */
            if (c_ptr.d_ptr.hasFlag(Flags.START_BLUE)) {
                /* Add to list */
                start_blue[num_start_blue++] = i;
            }
        }

        /* Check for second (or later) expansion */
        if (g.expanded >= 2) {
            /* Loop over players */
            for (i = 0; i < g.num_players; i++) {
                /* Choose a Red start world */
                n = g.game_rand() % num_start_red;

                /* Add to start world choices */
                start_picks[i][0] = start_red[n];

                /* Collapse list */
                start_red[n] = start_red[--num_start_red];

                /* Choose a Blue start world */
                n = g.game_rand() % num_start_blue;

                /* Add to start world choices */
                start_picks[i][1] = start_blue[n];

                /* Collapse list */
                start_blue[n] = start_blue[--num_start_blue];

                /* Get card pointer to first start choice */
                c_ptr = g.deck[start_picks[i][0]];

                /* XXX Move card to discard */
                c_ptr.where = WHERE_DISCARD;

                /* Get card pointer to second start choice */
                c_ptr = g.deck[start_picks[i][1]];

                /* XXX Move card to discard */
                c_ptr.where = WHERE_DISCARD;
            }

            /* Loop over players again */
            for (i = 0; i < g.num_players; i++) {
                /* Get player pointer */


                /* Give player six cards */
                draw_cards(g, i, 6);

                /* Reset list of cards in hand */
                n = 0;

                /* Loop over cards */
                for (j = 0; j < g.deck_size; j++) {
                    /* Get card pointer */
                    c_ptr = g.deck[j];

                    /* Skip unowned cards */
                    if (c_ptr.owner != i) continue;

                    /* Skip cards not in hand */
                    if (c_ptr.where != WHERE_HAND) continue;

                    /* Add card to list */
                    hand[n++] = j;
                }

                /* Two choices for homeworld */
                ns = 2;

                /* Ask player which start world they want */
                send_choice(g, i, CHOICE_START, hand, n,
                        start_picks[i], ns, 0, 0, 0);

                /* Check for aborted game */
                if (g.game_over) return;
            }

            /* Wait for answers from all players before revealing choices */
            wait_for_all(g);

            /* Loop over players */
            for (i = 0; i < g.num_players; i++) {
                /* Get answer */
                extract_choice(g, i, CHOICE_START, hand, n,
                        start_picks[i], ns);

                /* Apply choice */
                start_callback(g, i, hand, n, start_picks[i], ns);
            }
        } else {
            /* Loop over start worlds */
            for (i = 0; i < num_start; i++) {
                /* Get card pointer for start world */
                c_ptr = g.deck[start[i]];

                /* Temporarily move card to discard pile */
                c_ptr.where = WHERE_DISCARD;
            }

            /* Loop over players */
            for (i = 0; i < g.num_players; i++) {
                /* Choose a start world number */
                n = g.game_rand() % num_start;

                /* Chosen world to player */
                place_card(g, i, start[n]);

                /* Remember start world */
                g.p[i].start = start[n];

                /* Collapse list */
                start[n] = start[--num_start];
            }

            /* Loop over start worlds */
            for (i = 0; i < num_start; i++) {
                /* Get card pointer for start world */
                c_ptr = g.deck[start[i]];

                /* Move card back to deck */
                c_ptr.where = WHERE_DECK;
            }

            /* Loop over players again */
            for (i = 0; i < g.num_players; i++) {
                /* Give player six cards */
                draw_cards(g, i, 6);
            }
        }

        /* Find lowest numbered start world */
        for (i = 0; i < g.num_players; i++) {
            /* Check for lower number */
            if (g.p[i].start < lowest) {
                /* Remember lowest number */
                lowest = g.p[i].start;
                low_i = i;
            }
        }

        /* Rotate players until player 0 holds lowest start world */
        for (i = 0; i < low_i; i++) rotate_players(g);

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {

            /* Get player's start world */
            c_ptr = g.deck[g.p[i].start];

            /* Send message */
            message_add("%s starts with %s.\n", g.p[i].name, c_ptr.d_ptr.name);
        }

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {


            /* Get list of cards in hand */
            n = get_player_area(g, i, hand, WHERE_HAND);

            /* Assume player gets four cards */
            j = 4;

            /* Get player's start world */
            c_ptr = g.deck[g.p[i].start];

            /* Check for starting with less */
            if (c_ptr.d_ptr.hasFlag(STARTHAND_3)) j = 3;

            /* Assume not discarding */
            discarding[i] = false;

            /* Check for nothing to discard */
            if (n == j) continue;

            /* Ask player to discard to initial handsize */
            send_choice(g, i, CHOICE_DISCARD, hand, n, null, 0, n - j, 0, 0);

            /* Player is discarding */
            discarding[i] = true;

            /* Check for aborted game */
            if (g.game_over) return;
        }

        /* Wait for all decisions */
        wait_for_all(g);

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {
            /* Skip players who were not asked to discard */
            if (!discarding[i]) continue;

            /* Get discard choice */
            extract_choice(g, i, CHOICE_DISCARD, hand, n, null, 0);

            /* Make discards */
            discard_callback(g, i, hand, n);
        }

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {


            /* Get player's start world */
            c_ptr = g.deck[g.p[i].start];

            /* Check for starting with saved card */
            if (c_ptr.d_ptr.hasFlag(START_SAVE)) {
                /* Get cards in hand */
                n = get_player_area(g, i, hand, WHERE_HAND);

                /* Ask player to save one card */
                ask_player(g, i, CHOICE_SAVE, hand, n, null, 0, 0, 0, 0);

                /* Check for aborted game */
                if (g.game_over) return;

                /* Move card to saved area */
                move_card(g, hand[0], i, WHERE_SAVED);
            }
        }

        /* Clear temporary flags on drawn cards */
        clear_temp(g);

        /* XXX Pretend settle phase to set goal progress properly */
        g.cur_action = ACT_SETTLE;
        check_goals(g);
        g.cur_action = -1;


    }

    public boolean game_round(Game game) {
        throw new UnsupportedOperationException();
    }

    public void score_game(Game game) {
        throw new UnsupportedOperationException();
    }

    public void declare_winner(Game game) {
        throw new UnsupportedOperationException();
    }


    /*
    * Draw a card from the deck.
    */
    void draw_card(Game g, int who) {
        Player p_ptr;
        Card c_ptr;
        int which;

        /* Get player pointer */
        p_ptr = g.p[who];

        /* Check for simulated game */
        if (g.simulation) {
            /* Count fake cards */
            p_ptr.fake_hand++;

            /* Track total number of fake cards seen */
            p_ptr.total_fake++;

            /* Get card from draw pile */
            which = random_draw(g);

            /* Check for failure */
            if (which == -1) return;

            /* Get card pointer */
            c_ptr = g.deck[which];

            /* Move card to discard to simulate deck cycling */
            c_ptr.where = WHERE_DISCARD;

            /* Done */
            return;
        }

        /* Choose random card */
        which = random_draw(g);

        /* Check for failure */
        if (which == -1) return;

        /* Move card to player's hand */
        move_card(g, which, who, WHERE_HAND);

        /* Get card pointer */
        c_ptr = g.deck[which];

        /* Card's location is known to player */
        c_ptr.known |= 1 << who;
    }

    /*
    * Draw a number of cards, as in draw_card() above.
    */
    void draw_cards(Game g, int who, int num) {
        int i;

        /* Draw required number */
        for (i = 0; i < num; i++) draw_card(g, who);
    }


    /*
    * Ask a player to make a decision.
    *
    * In this function we will return immediately after asking.  The answer
    * can later be retrieved using extract_answer() above.
    */
    void send_choice(Game g, int who, int type, int list[], int nl,
                     int special[], int ns, int arg1, int arg2, int arg3) {
        Player p_ptr = g.p[who];

        /* Check for unconsumed choices in log */
        if (p_ptr.choice_pos < p_ptr.choice_size) return;

        /* Ask player for answer */
        p_ptr.control.make_choice(g, who, type, list, nl, special, ns,
                arg1, arg2, arg3);
    }

    /*
    * Wait for all players to have responses ready to the most recent question.
    */
    void wait_for_all(Game g) {
        /* Loop over players */
        for (int i = 0; i < g.num_players; i++) {
            /* Wait for answer to become available */
            g.p[i].control.wait_answer(g, i);
        }
    }


    /*
    * Extract an answer to a pending choice from the player's choice log.
    */
    //TODO: nl and ns are return values
    int extract_choice(Game g, int who, int type, int list[], int nl, int special[], int ns) {
        Player p_ptr;
        int l_ptr;
        int rv, i, num;

        /* Get player pointer */
        p_ptr = g.p[who];

        /* Get current position in log */
        l_ptr = p_ptr.choice_pos;

        /* Check for no data in log */
        if (p_ptr.choice_pos >= p_ptr.choice_size) {
            /* Error */
            throw new RuntimeException("No data available in choice log!");
        }

        /* Check for correct type of answer */
        if (p_ptr.choice_log[l_ptr] != type) {
            throw new RuntimeException("Expected " + type + " in choice log, found " + p_ptr.choice_log[l_ptr] + "!");
        }

        /* Advance pointer */
        l_ptr++;

        /* Copy return value */
        rv = p_ptr.choice_log[l_ptr++];

        /* Get number of items returned */
        num = p_ptr.choice_log[l_ptr++];

        /* Check for number of items available */
        if (nl != 0) {
            /* Copy number of items in list */
            nl = num;

            /* Copy list items */
            for (i = 0; i < num; i++) {
                /* Copy item */
                list[i] = p_ptr.choice_log[l_ptr++];
            }
        } else {
            /* Ensure no items were returned */
            if (num != 0) {
                /* Error */
                throw new RuntimeException("Log has items but nowhere to copy them!");
            }
        }

        /* Get number of special items returned */
        num = p_ptr.choice_log[l_ptr++];

        /* Check for number of special items available */
        if (ns != 0) {
            /* Copy number of special items */
            ns = num;

            /* Copy special items */
            for (i = 0; i < num; i++) {
                /* Copy item */
                special[i] = p_ptr.choice_log[l_ptr++];
            }
        } else {
            /* Ensure no items were returned */
            if (num != 0) {
                /* Error */
                throw new RuntimeException("Log has specials but nowhere to copy them!");
            }
        }

        /* Set log position to current */
        p_ptr.choice_pos = l_ptr - p_ptr.choice_pos;

        /* Return logged answer */
        return rv;
    }

    /*
    * Called when a player has chosen a start world and initial discards.
    */
    int start_callback(Game g, int who, int list[], int n, int special[], int ns) {
        Player p_ptr = g.p[who];
        Card c_ptr;

        /* Ensure exactly one start world chosen */
        if (ns != 1) return 0;

        /* Remember start card */
        p_ptr.start = special[0];

        /* Get card pointer of start world */
        c_ptr = g.deck[special[0]];

        /* Check for 2 cards discarded */
        if (n != 2) return 0;

        /* Discard chosen cards */
        discard_callback(g, who, list, n);

        /* Place start card */
        place_card(g, who, special[0]);

        /* Choice is good */
        return 1;
    }

    /*
    * Place a card on the table for the given player.
    */
    void place_card(Game g, int who, int which) {
        Player p_ptr;
        Card c_ptr;

        /* Get player pointer */
        p_ptr = g.p[who];

        /* Get card being placed */
        c_ptr = g.deck[which];

        /* Move card to player */
        move_card(g, which, who, WHERE_ACTIVE);

        /* Location is known to all */
        c_ptr.known = ~0;

        /* Count order played */
        c_ptr.order = p_ptr.table_order++;

        /* Add a good to windfall worlds */
        if (c_ptr.d_ptr.hasFlag(WINDFALL)) add_good(g, c_ptr);

        /* Check for third expansion */
        if (g.expanded >= 3) {
            /* Check for prestige from card */
            if (c_ptr.d_ptr.hasFlag(PRESTIGE)) {
                /* Add prestige to player */
                gain_prestige(g, who, 1);
            }
        }

        /* Card is as-yet unpaid for */
        c_ptr.unpaid = 1;
    }


    /*
    * Rotate players one spot.
    *
    * This will be called until the player with the lowest start world is
    * player number 0.
    */
    void rotate_players(Game g) {
        Player temp, p_ptr;
        Card c_ptr;
        int i, bit;

        /* Store copy of player 0 */
        temp = g.p[0];

        /* Loop over players */
        for (i = 0; i < g.num_players - 1; i++) {
            /* Copy players one space */
            g.p[i] = g.p[i + 1];
        }

        /* Store old player 0 in last spot */
        g.p[i] = temp;

        /* Loop over cards in deck */
        for (i = 0; i < g.deck_size; i++) {
            /* Get card pointer */
            c_ptr = g.deck[i];

            /* Skip cards owned by no one */
            if (c_ptr.owner == -1) continue;

            /* Adjust owner */
            c_ptr.owner--;

            /* Check for wraparound */
            if (c_ptr.owner < 0) c_ptr.owner = g.num_players - 1;

            /* Track lowest bit of known */
            bit = c_ptr.known & 1;

            /* Adjust known bits */
            c_ptr.known >>= 1;

            /* Rotate old lowest bit to highest position */
            c_ptr.known |= bit << (g.num_players - 1);
        }

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {
            /* Get player pointer */
            p_ptr = g.p[i];

            /* Notify player of rotation */
            p_ptr.control.notify_rotation(g, i);
        }
    }

    /*
    * Return a list of cards in the given player's given area.
    */
    int get_player_area(Game g, int who, int[] list, int where) {
        int x, n = 0;

        /* Start at beginning of list */
        x = g.p[who].head[where];

        /* Loop over cards */
        for (; x != -1; x = g.deck[x].next) {
            /* Add card to list */
            list[n++] = x;
        }

        /* Return number found */
        return n;
    }

    /*
    * Called when player has chosen which cards to discard.
    */
    void discard_callback(Game g, int who, int list[], int num) {

        /* Loop over choices */
        for (int i = 0; i < num; i++) {
            /* Move card to discard */
            move_card(g, list[i], -1, WHERE_DISCARD);
        }
    }

    /*
    * Ask a player to make a decision.
    *
    * If we are replaying a game session, we may already have the decision
    * saved in the player's choice log.  In that case, pull the decision
    * from the log and return it.
    *
    * In this function we always wait for an answer from the player before
    * returning.
    */
    int ask_player(Game g, int who, int type, int list[], int nl,
                   int special[], int ns, int arg1, int arg2, int arg3) {
        Player p_ptr;

        /* Get player pointer */
        p_ptr = g.p[who];

        /* Check for unconsumed choices in log */
        if (p_ptr.choice_pos < p_ptr.choice_size) {
            /* Return logged answer */
            return extract_choice(g, who, type, list, nl, special, ns);
        }

        /* Ask player for answer */
        p_ptr.control.make_choice(g, who, type, list, nl, special, ns,
                arg1, arg2, arg3);

        /* Check for aborted game */
        if (g.game_over) return -1;

        /* Wait for answer to become available */
        p_ptr.control.wait_answer(g, who);

        /* Return logged answer */
        return extract_choice(g, who, type, list, nl, special, ns);
    }

    /*
    * Move a card, keeping track of linked lists.
    *
    * This MUST be called when a card is moved to or from a player.
    */
    void move_card(Game g, int which, int owner, int where) {
        Player p_ptr;
        Card c_ptr;
        int x;

        /* Get card pointer */
        c_ptr = g.deck[which];

        /* Check for current owner */
        if (c_ptr.owner != -1) {
            /* Get pointer of current owner */
            p_ptr = g.p[c_ptr.owner];

            /* Find card in list */
            x = p_ptr.head[c_ptr.where];

            /* Check for beginning of list */
            if (x == which) {
                /* Adjust list forward */
                p_ptr.head[c_ptr.where] = c_ptr.next;
                c_ptr.next = -1;
            } else {
                /* Loop until moved card is found */
                while (g.deck[x].next != which) {
                    /* Move forward */
                    x = g.deck[x].next;
                }

                /* Remove moved card from list */
                g.deck[x].next = c_ptr.next;
                c_ptr.next = -1;
            }
        }

        /* Check for new owner */
        if (owner != -1) {
            /* Get player pointer of new owner */
            p_ptr = g.p[owner];

            /* Add card to beginning of list */
            c_ptr.next = p_ptr.head[where];
            p_ptr.head[where] = which;
        }

        /* Adjust location */
        c_ptr.owner = owner;
        c_ptr.where = where;
    }

    /*
 * Clear temp flags on all cards and player structures.
 */
    void clear_temp(Game g) {
        Player p_ptr;
        Card c_ptr;
        int i, j;

        /* Loop over cards */
        for (i = 0; i < g.deck_size; i++) {
            /* Get card pointer */
            c_ptr = g.deck[i];

            /* Copy current location */
            c_ptr.start_owner = c_ptr.owner;
            c_ptr.start_where = c_ptr.where;

            /* Copy next card */
            c_ptr.start_next = c_ptr.next;

            /* Clear unpaid flag */
            c_ptr.unpaid = 0;

            /* Clear produced flag */
            c_ptr.produced = 0;

            /* Loop over used flags */
            for (j = 0; j < MAX_POWER; j++) {
                /* Clear flag */
                c_ptr.used[j] = false;
            }
        }

        /* Loop over players */
        for (i = 0; i < g.num_players; i++) {
            /* Get player pointer */
            p_ptr = g.p[i];

            /* Clear bonus used flag */
            p_ptr.phase_bonus_used = 0;

            /* Clear bonus military */
            p_ptr.bonus_military = 0;

            /* Clear bonus settle cost reduction */
            p_ptr.bonus_reduce = 0;

            /* Loop over location heads */
            for (j = 0; j < MAX_WHERE; j++) {
                /* Copy start of list */
                p_ptr.start_head[j] = p_ptr.head[j];
            }
        }
    }

    /*
    * Award goals to players who meet requirements.
    */
    void check_goals(Game g) {
        Player p_ptr;
        int[] count = new int[MAX_PLAYER];
        boolean most;
        int i, j, k;
        String msg;

        /* Loop over "first" goals */
        for (i = GOAL_FIRST_5_VP; i <= GOAL_FIRST_4_MILITARY; i++) {
            /* Skip inactive goals */
            if (!g.goal_active[i]) continue;

            /* Skip already claimed goals */
            if (!g.goal_avail[i]) continue;

            /* Do not check goals that cannot happen yet */
            switch (i) {
                /* End of turn only */
                case GOAL_FIRST_DISCARD:

                    /* Only check at end of turn */
                    if (g.cur_action != -1) continue;
                    break;

                /* Develop phase only */
                case GOAL_FIRST_SIX_DEVEL:

                    /* Only check after develop */
                    if (g.cur_action != ACT_DEVELOP &&
                            g.cur_action != ACT_DEVELOP2) continue;
                    break;

                /* Settle phase only */
                case GOAL_FIRST_4_TYPES:

                    /* Only check after settle */
                    if (g.cur_action != ACT_SETTLE &&
                            g.cur_action != ACT_SETTLE2) continue;
                    break;

                /* Develop/Settle phases only */
                case GOAL_FIRST_3_ALIEN:
                case GOAL_FIRST_PHASE_POWER:
                case GOAL_FIRST_3_UPLIFT:
                case GOAL_FIRST_8_ACTIVE:
                case GOAL_FIRST_NEG_MILITARY:
                case GOAL_FIRST_4_MILITARY:

                    /* Only check after develop/settle */
                    if (g.cur_action != ACT_DEVELOP &&
                            g.cur_action != ACT_DEVELOP2 &&
                            g.cur_action != ACT_SETTLE &&
                            g.cur_action != ACT_SETTLE2) continue;
                    break;

                /* Consume phase or beginning of turn only */
                case GOAL_FIRST_5_VP:

                    /* Only check after round start/consume */
                    if (g.cur_action != ACT_CONSUME_TRADE &&
                            g.cur_action != -1)
                        continue;
                    break;

                /* Settle or Produce phases */
                case GOAL_FIRST_4_GOODS:

                    /* Only check after settle or produce */
                    if (g.cur_action != ACT_SETTLE &&
                            g.cur_action != ACT_SETTLE2 &&
                            g.cur_action != ACT_PRODUCE) continue;
                    break;

                /* Any phase */
                case GOAL_FIRST_2_PRESTIGE:
                    break;
            }

            /* Loop over players */
            for (j = 0; j < g.num_players; j++) {
                /* Get player pointer */
                p_ptr = g.p[j];

                /* Check for player meeting requirement */
                if (check_goal_player(g, i, j) > 0) {
                    /* Claim goal */
                    p_ptr.goal_claimed[i] = true;

                    /* Remove goal availability */
                    g.goal_avail[i] = false;

                    /* Message */
                    if (!g.simulation) {
                        /* Send message */
                        message_add("%s claims %s goal.\n", p_ptr.name, goal_name[i]);
                    }
                }
            }
        }

        /* Loop over "most" goals */
        for (i = GOAL_MOST_MILITARY; i <= GOAL_MOST_CONSUME; i++) {
            /* Skip inactive goals */
            if (!g.goal_active[i]) continue;

            /* Do not check goals that cannot happen yet */
            switch (i) {
                /* Settle phase only */
                case GOAL_MOST_BLUE_BROWN:
                case GOAL_MOST_PRODUCTION:
                case GOAL_MOST_REBEL:

                    /* Only check after settle */
                    if (g.cur_action != ACT_SETTLE &&
                            g.cur_action != ACT_SETTLE2) continue;
                    break;

                /* Develop phase only */
                case GOAL_MOST_DEVEL:

                    /* Only check after develop */
                    if (g.cur_action != ACT_DEVELOP &&
                            g.cur_action != ACT_DEVELOP2) continue;
                    break;

                /* Develop/Settle phases only */
                case GOAL_MOST_MILITARY:
                case GOAL_MOST_EXPLORE:
                case GOAL_MOST_CONSUME:

                    /* Only check after develop/settle */
                    if (g.cur_action != ACT_DEVELOP &&
                            g.cur_action != ACT_DEVELOP2 &&
                            g.cur_action != ACT_SETTLE &&
                            g.cur_action != ACT_SETTLE2) continue;
                    break;

                /* Any phase */
                case GOAL_MOST_PRESTIGE:
                    break;
            }

            /* Clear most progress */
            g.goal_most[i] = 0;

            /* Loop over each player */
            for (j = 0; j < g.num_players; j++) {
                /* Get player's progress */
                count[j] = check_goal_player(g, i, j);

                /* Save progress */
                g.p[j].goal_progress[i] = count[j];

                /* Check for more than most */
                if (count[j] > g.goal_most[i]) {
                    /* Remember most */
                    g.goal_most[i] = count[j];
                }

                /* Check for insufficient progress */
                if (count[j] < goal_minimum(i)) count[j] = 0;
            }

            /* Check for losing goal */
            for (j = 0; j < g.num_players; j++) {
                /* Get player pointer */
                p_ptr = g.p[j];

                /* Check for goal claimed and lost */
                if (p_ptr.goal_claimed[i] && (count[j] == 0)) {
                    /* Lose goal */
                    g.goal_avail[i] = true;
                    p_ptr.goal_claimed[i] = false;

                    /* Message */
                    if (!g.simulation) {
                        /* Send message */
                        message_add("%s loses %s goal.\n",
                                p_ptr.name, goal_name[i]);
                    }
                }
            }

            /* Loop over players */
            for (j = 0; j < g.num_players; j++) {
                /* Assume this player has most */
                most = true;

                /* Loop over opponents */
                for (k = 0; k < g.num_players; k++) {
                    /* Do not compete with ourself */
                    if (j == k) continue;

                    /* Check for no more than opponent */
                    if (count[j] <= count[k]) most = false;
                }

                /* Get player pointer */
                p_ptr = g.p[j];

                /* Check for more than anyone else */
                if (most && !p_ptr.goal_claimed[i]) {
                    /* Goal is no longer available */
                    g.goal_avail[i] = false;

                    /* Loop over players */
                    for (k = 0; k < g.num_players; k++) {
                        /* Get player pointer */
                        p_ptr = g.p[k];

                        /* Award card to player with most */
                        p_ptr.goal_claimed[i] = (j == k);
                    }

                    /* Message */
                    if (!g.simulation) {
                        /* Get player pointer */
                        p_ptr = g.p[j];

                        /* Send message */
                        message_add("%s claims %s goal.\n",
                                p_ptr.name, goal_name[i]);
                    }
                }
            }
        }
    }

    /*
 * Return a card index from the draw deck.
 */
    int random_draw(Game g) {
        int i, n;

        /* Count draw deck size */
        n = count_draw(g);

        /* Check for no cards */
        if (n == 0) {
            /* Refresh draw deck */
            refresh_draw(g);

            /* Recount */
            n = count_draw(g);

            /* Check for still no cards */
            if (n == 0) {
                /* No card to return */
                return -1;
            }
        }

        /* Choose randomly */
        n = g.game_rand() % n;

        /* Loop over cards */
        for (i = 0; i < g.deck_size; i++) {
            /* Skip cards not in draw deck */
            if (g.deck[i].where != WHERE_DECK) continue;

            /* Check for chosen card */
            n--;
            if (n <= 0) break;
        }

        /* Clear chosen card's location */
        g.deck[i].where = -1;

        /* Check for just-emptied draw pile */
        if (count_draw(g) != 0) refresh_draw(g);

        /* Return chosen card */
        return i;
    }

    /*
 * Check a player's progress towards a goal.
 *
 * Return zero if the player does not qualify.
 */
    int check_goal_player(Game g, int goal, int who) {
        Player p_ptr;
        Card c_ptr;
        PowerWhere[] w_list = new PowerWhere[100];
        Power o_ptr;
        int[] good = new int[6];
        int[] phase = new int[6];
        int count = 0;
        int i, x, n;

        /* Get player pointer */
        p_ptr = g.p[who];

        /* Switch on goal type */
        switch (goal) {
            /* First to 5 VP chips */
            case GOAL_FIRST_5_VP:

                /* Check for 5 VP */
                return (p_ptr.vp >= 5 ? 1 : 0);

            /* First to 4 good types */
            case GOAL_FIRST_4_TYPES:

                /* Clear good marks */
                for (i = 0; i < 6; i++) good[i] = 0;

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip non-worlds */
                    if (c_ptr.d_ptr.type != TYPE_WORLD) continue;

                    /* Mark good type */
                    good[c_ptr.d_ptr.good_type.ordinal()] = 1;
                }

                /* Count types */
                for (i = GOOD_ANY; i <= GOOD_ALIEN; i++) {
                    /* Check for active type */
                    if (good[i] != 0) count++;
                }

                /* Check for all four types */
                return (count >= 4 ? 1 : 0);

            /* First to three Alien cards */
            case GOAL_FIRST_3_ALIEN:

                /* Count ALIEN flags */
                count = count_active_flags(g, who, ALIEN);

                /* Check for three alien cards */
                return (count >= 3 ? 1 : 0);

            /* First to discard at end of turn */
            case GOAL_FIRST_DISCARD:

                /* Check for previous discard */
                return p_ptr.end_discard;

            /* First to have powers for each phase */
            case GOAL_FIRST_PHASE_POWER:

                /* Clear phase marks */
                for (i = 0; i < 6; i++) phase[i] = 0;

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Loop over card powers */
                    for (i = 0; i < c_ptr.d_ptr.num_power; i++) {
                        /* Get power pointer */
                        o_ptr = c_ptr.d_ptr.powers[i];

                        /* Check for trade power */
                        if (o_ptr.phase == PHASE_CONSUME &&
                                (o_ptr.code & P4_TRADE_MASK) == P4_TRADE_MASK) {
                            /* XXX Mark trade power */
                            phase[0] = 1;
                        } else {
                            /* Mark phase */
                            phase[o_ptr.phase] = 1;
                        }
                    }
                }

                /* Count phases with powers */
                for (i = 0; i < 6; i++) {
                    /* Check for power */
                    if (phase[i] > 0) count++;
                }

                /* Check for all 6 phases */
                return count == 6 ? 1 : 0;

            /* First to have a six-cost development */
            case GOAL_FIRST_SIX_DEVEL:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip worlds */
                    if (c_ptr.d_ptr.type == TYPE_WORLD) continue;

                    /* Skip non-cost-6 cards */
                    if (c_ptr.d_ptr.cost != 6) continue;

                    /* Check for variable points */
                    if (c_ptr.d_ptr.num_vp_bonus > 0) return 1;
                }

                /* No six-cost developments */
                return 0;

            /* First to three Uplift cards */
            case GOAL_FIRST_3_UPLIFT:

                /* Count UPLIFT flags */
                count = count_active_flags(g, who, UPLIFT);

                /* Check for three Uplift cards */
                return count >= 3 ? 1 : 0;

            /* First to 4 goods */
            case GOAL_FIRST_4_GOODS:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip non-worlds */
                    if (c_ptr.d_ptr.type != TYPE_WORLD) continue;

                    /* Check for good */
                    if (c_ptr.covered != -1) count++;
                }

                /* Check for four goods */
                return count >= 4 ? 1 : 0;

            /* First to 8 active cards */
            case GOAL_FIRST_8_ACTIVE:

                /* Check for 8 cards */
                return count_player_area(g, who, WHERE_ACTIVE) >= 8 ? 1 : 0;

            /* First to have negative military or takeover power */
            case GOAL_FIRST_NEG_MILITARY:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip non-worlds */
                    if (c_ptr.d_ptr.type != TYPE_WORLD) continue;

                    /* Count active worlds */
                    count++;
                }

                /* Check for not at least 2 worlds */
                if (count < 2) return 0;

                /* Check for negative military */
                if (total_military(g, who) < 0) return 1;

                /* Check for takeovers disabled */
                if (g.takeover_disabled) return 0;

                /* Get count of military worlds */
                count = count_active_flags(g, who, MILITARY);

                /* Check for less than 2 military worlds */
                if (count < 2) return 0;

                /* Get Settle phase powers */
                n = get_powers(g, who, PHASE_SETTLE, w_list);

                /* Loop over powers */
                for (i = 0; i < n; i++) {
                    /* Get power pointer */
                    o_ptr = w_list[i].o_ptr;

                    /* Check for takeover power */
                    if (o_ptr.hasFlag(SettlePowers.TAKEOVER_REBEL,
                            SettlePowers.TAKEOVER_IMPERIUM,
                            SettlePowers.TAKEOVER_MILITARY,
                            SettlePowers.TAKEOVER_PRESTIGE)) {
                        /* Takeover condition met */
                        return 1;
                    }
                }

                /* Goal not met */
                return 0;

            /* First to 2 prestige and 3 VP chips */
            case GOAL_FIRST_2_PRESTIGE:

                /* Check for enough prestige and VP */
                return p_ptr.prestige >= 2 && p_ptr.vp >= 3 ? 1 : 0;

            /* First to 3 Imperium cards or 4 military worlds */
            case GOAL_FIRST_4_MILITARY:

                /* Check for enough Imperium or military */
                return count_active_flags(g, who, IMPERIUM) >= 3 ||
                        count_active_flags(g, who, MILITARY) >= 4 ? 1 : 0;

            /* Most military (minimum 6) */
            case GOAL_MOST_MILITARY:

                /* Get military strength */
                return total_military(g, who);

            /* Most blue/brown worlds (minimum 3) */
            case GOAL_MOST_BLUE_BROWN:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip non-worlds */
                    if (c_ptr.d_ptr.type != TYPE_WORLD) continue;

                    /* Check for blue or brown */
                    if (c_ptr.d_ptr.good_type == GoodType.NOVELTY ||
                            c_ptr.d_ptr.good_type == GoodType.RARE) {
                        /* Count world */
                        count++;
                    }

                    /* Check for "any" kind */
                    if (c_ptr.d_ptr.good_type == GoodType.ANY &&
                            (g.oort_kind == GOOD_ANY ||
                                    g.oort_kind == GOOD_NOVELTY ||
                                    g.oort_kind == GOOD_RARE)) {
                        /* Count world */
                        count++;
                    }
                }

                /* Return count */
                return count;

            /* Most developments (minimum 4) */
            case GOAL_MOST_DEVEL:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip worlds */
                    if (c_ptr.d_ptr.type == TYPE_WORLD) continue;

                    /* Count developments */
                    count++;
                }

                /* Return count */
                return count;

            /* Most production worlds (minimum 4) */
            case GOAL_MOST_PRODUCTION:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Skip non-worlds */
                    if (c_ptr.d_ptr.type != TYPE_WORLD) continue;

                    /* Skip windfall worlds */
                    if (c_ptr.d_ptr.hasFlag(WINDFALL))
                        continue;

                    /* Skip worlds with no good type */
                    if (c_ptr.d_ptr.good_type == GoodType.NONE) continue;

                    /* Count world */
                    count++;
                }

                /* Return count */
                return count;

            /* Most explore powers (minimum 3) */
            case GOAL_MOST_EXPLORE:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Loop over card powers */
                    for (i = 0; i < c_ptr.d_ptr.num_power; i++) {
                        /* Get power pointer */
                        o_ptr = c_ptr.d_ptr.powers[i];

                        /* Check for explore phase */
                        if (o_ptr.phase == PHASE_EXPLORE) {
                            /* Count card */
                            count++;

                            /* Stop looking */
                            break;
                        }
                    }
                }

                /* Return count */
                return count;

            /* Most Rebel military worlds (minimum 3) */
            case GOAL_MOST_REBEL:

                /* Count military Rebel worlds */
                return count_active_flags(g, who, REBEL, MILITARY);

            /* Most prestige (minimum 3) */
            case GOAL_MOST_PRESTIGE:

                /* Return amount of prestige */
                return p_ptr.prestige;

            /* Most cards with consume powers (minimum 3) */
            case GOAL_MOST_CONSUME:

                /* Start at first active card */
                x = p_ptr.head[WHERE_ACTIVE];

                /* Loop over cards */
                for (; x != -1; x = g.deck[x].next) {
                    /* Get card pointer */
                    c_ptr = g.deck[x];

                    /* Loop over card powers */
                    for (i = 0; i < c_ptr.d_ptr.num_power; i++) {
                        /* Get power pointer */
                        o_ptr = c_ptr.d_ptr.powers[i];

                        /* Check for consume phase */
                        if (o_ptr.phase == PHASE_CONSUME) {
                            /* Skip trade powers */
                            if (o_ptr.hasFlag(ConsumePowers.TRADE_ANY,
                                    ConsumePowers.TRADE_NOVELTY,
                                    ConsumePowers.TRADE_RARE,
                                    ConsumePowers.TRADE_GENE,
                                    ConsumePowers.TRADE_ALIEN,
                                    ConsumePowers.TRADE_THIS,
                                    ConsumePowers.TRADE_BONUS_CHROMO,
                                    ConsumePowers.NO_TRADE)) {
                                /* Skip power */
                                continue;
                            }

                            /* Count card */
                            count++;

                            /* Stop looking */
                            break;
                        }
                    }
                }

                /* Return count */
                return count;
        }

        /* XXX */
        return 0;
    }

    /*
    * Return the minimum amount of progress needed to claim a "most" goal.
    */
    int goal_minimum(int goal) {
        /* Switch on goal type */
        switch (goal) {
            case GOAL_MOST_MILITARY:
                return 6;
            case GOAL_MOST_BLUE_BROWN:
                return 3;
            case GOAL_MOST_DEVEL:
                return 4;
            case GOAL_MOST_PRODUCTION:
                return 4;
            case GOAL_MOST_EXPLORE:
                return 3;
            case GOAL_MOST_REBEL:
                return 3;
            case GOAL_MOST_PRESTIGE:
                return 3;
            case GOAL_MOST_CONSUME:
                return 3;
        }

        /* XXX */
        return -1;
    }

    /*
    * Return the number of cards in the draw deck.
    */
    int count_draw(Game g) {
        Card c_ptr;
        int i, n = 0;

        /* Loop over cards */
        for (i = 0; i < g.deck_size; i++) {
            /* Get card pointer */
            c_ptr = g.deck[i];

            /* Count cards in draw deck */
            if (c_ptr.where == WHERE_DECK) n++;
        }

        /* Return count */
        return n;
    }

    /*
    * Refresh the draw deck.
    */
    void refresh_draw(Game g) {
        Card c_ptr;
        int i;

        /* Message */
        if (!g.simulation) {
            /* Send message */
            message_add("Refreshing draw deck.\n");
        }

        /* Loop over cards */
        for (i = 0; i < g.deck_size; i++) {
            /* Get card pointer */
            c_ptr = g.deck[i];

            /* Skip cards not in discard pile */
            if (c_ptr.where != WHERE_DISCARD) continue;

            /* Move card to draw deck */
            c_ptr.where = WHERE_DECK;

            /* Card's location is no longer known to anyone */
            c_ptr.known = 0;
        }
    }

    /*
 * Return the number of active cards with the given flags.
 *
 * We check the card's location as of the start of the phase.
 */
    int count_active_flags(Game g, int who, Flags... flags) {
        int x, count = 0;

        /* Start at first active card */
        x = g.p[who].start_head[WHERE_ACTIVE];

        /* Loop over cards */
        for (; x != -1; x = g.deck[x].start_next) {
            /* Check for correct flags */
            if (g.deck[x].d_ptr.hasFlag(flags)) count++;
        }

        /* Return count */
        return count;
    }

    /*
 * Return the number of card's in the given player's hand or active area.
 */
    int count_player_area(Game g, int who, int where) {
        int x, n = 0;

        /* Get first card of area chosen */
        x = g.p[who].head[where];

        /* Loop until end of list */
        for (; x != -1; x = g.deck[x].next) {
            /* Count card */
            n++;
        }

        /* Return count */
        return n;
    }

    /*
 * Return non-specific military strength.
 */
    int total_military(Game g, int who) {
        PowerWhere[] w_list = new PowerWhere[100];
        Power o_ptr;
        int i, n, amt = 0;

        /* Get list of settle powers */
        n = get_powers(g, who, PHASE_SETTLE, w_list);

        /* Loop over powers */
        for (i = 0; i < n; i++) {
            /* Get power pointer */
            o_ptr = w_list[i].o_ptr;

            /* Check for non-specific military */
            if (o_ptr.code == P3_EXTRA_MILITARY) {
                /* Add to military */
                amt += o_ptr.value;
            }

            /* Check for non-specific military per military world */
            if (o_ptr.code == (P3_EXTRA_MILITARY | P3_PER_MILITARY)) {
                /* Add to military */
                amt += count_active_flags(g, who, MILITARY);
            }

            /* Check for non-specific military per chromosome flag */
            if (o_ptr.code == (P3_EXTRA_MILITARY | P3_PER_CHROMO)) {
                /* Add to military */
                amt += count_active_flags(g, who, CHROMO);
            }

            /* Check for only if Imperium card active */
            if (o_ptr.code == (P3_EXTRA_MILITARY | P3_IF_IMPERIUM)) {
                /* Check for Imperium flag */
                if (count_active_flags(g, who, IMPERIUM) > 0) {
                    /* Add power's value */
                    amt += o_ptr.value;
                }
            }
        }

        /* Return amount of military */
        return amt;
    }

    /*
 * Return locations of powers for a given player for the given phase.
 */
    int get_powers(Game g, int who, int phase, PowerWhere[] w_list) {
        Card c_ptr;
        Power o_ptr;
        int x, i, n = 0;

        /* Get first active card */
        x = g.p[who].start_head[WHERE_ACTIVE];

        /* Loop over cards */
        for (; x != -1; x = g.deck[x].start_next) {
            /* Get card pointer */
            c_ptr = g.deck[x];

            /* Loop over card's powers */
            for (i = 0; i < c_ptr.d_ptr.num_power; i++) {
                /* Get power pointer */
                o_ptr = c_ptr.d_ptr.powers[i];

                /* Skip used powers */
                if (c_ptr.used[i]) continue;

                /* Skip incorrect phase */
                if (o_ptr.phase != phase) continue;

                /* Check for settle phase and discard power */
                if (o_ptr.phase == PHASE_SETTLE &&
                        (o_ptr.hasFlag(SettlePowers.DISCARD) &&
                                c_ptr.where == WHERE_DISCARD)) continue;

                /* Copy power location */
                w_list[n].c_idx = x;
                w_list[n].o_idx = i;

                /* Copy power pointer */
                w_list[n++].o_ptr = o_ptr;
            }
        }

        /* Return length of list */
        return n;
    }

    /*
    * Add a good to a played card.
    */
    void add_good(Game g, Card c_ptr) {
        int which;

        /* Check for simulated game */
        if (g.simulation) {
            /* Use first available card */
            which = first_draw(g);
        } else {
            /* Get random card to use as good */
            which = random_draw(g);
        }

        /* Check for failure */
        if (which == -1) return;

        /* Move card to owner */
        move_card(g, which, c_ptr.owner, WHERE_GOOD);

        /* Mark covered card */
        c_ptr.covered = which;
    }


    /*
    * Give a player some prestige.
    */
    void gain_prestige(Game g, int who, int num) {
        Player p_ptr;

        /* Get player pointer */
        p_ptr = g.p[who];

        /* Add to prestige */
        p_ptr.prestige += num;

        /* Mark prestige earned this turn */
        p_ptr.prestige_turn = 1;
    }


    /*
    * Goal names.
    */
    private static final String[] goal_name = new String[]
            {
                    "Galactic Standard of Living",
                    "System Diversity",
                    "Overlord Discoveries",
                    "Budget Surplus",
                    "Innovation Leader",
                    "Galactic Status",
                    "Uplift Knowledge",
                    "Galactic Riches",
                    "Expansion Leader",
                    "Peace/War Leader",
                    "Galactic Standing",
                    "Military Influence",

                    "Greatest Military",
                    "Largest Industry",
                    "Greatest Infrastructure",
                    "Production Leader",
                    "Research Leader",
                    "Propaganda Edge",
                    "Galactic Prestige",
                    "Prosperity Lead"
            };

    /*
 * Return the first card pointer from the draw deck.
 *
 * We use this in simulated games for choosing cards to be used as goods
 * and other non-essential tasks.  We don't want to use the random number
 * generator in these cases.
 */
    int first_draw(Game g) {
        Card c_ptr = null;
        int i, n;

        /* Count draw deck size */
        n = count_draw(g);

        /* Check for no cards */
        if (n == 0) {
            /* Refresh draw deck */
            refresh_draw(g);

            /* Recount */
            n = count_draw(g);

            /* Check for still no cards */
            if (n == 0) {
                /* No card to return */
                return -1;
            }
        }

        /* Loop over cards */
        for (i = 0; i < g.deck_size; i++) {
            /* Skip cards not in draw deck */
            if (g.deck[i].where != WHERE_DECK) continue;

            /* Stop at first valid card */
            break;
        }

        /* Clear chosen card's location */
        g.deck[i].where = -1;

        /* Check for just-emptied draw pile */
        if (count_draw(g) == 0) refresh_draw(g);

        /* Return chosen card */
        return i;
    }


}
