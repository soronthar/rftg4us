package rftg.game.ai;

import com.rits.cloning.Cloner;
import rftg.game.Decisions;
import rftg.game.Engine;
import rftg.game.Game;
import rftg.game.Player;

import static rftg.game.Constants.*;

public class EmptyAI implements Decisions {
    private Player player;
    private boolean initialized = false;

    public EmptyAI() {
    }

    @Override
    public void init(Game game, Player who, double factor) {
        this.player = who;
        this.initialized = true;
        System.out.println("EmptyAI.init:9");
    }

    @Override
    public void notify_rotation(Game game, int who) {
        System.out.println("EmptyAI.notify_rotation:14");
    }

    @Override
    public void prepare_phase(Game game, int who, int phase, int arg) {
        System.out.println("EmptyAI.prepare_phase:19");
    }

    @Override
    //Todo:again, nl and ns seem to be return values too.
    public void make_choice(Game g, int who, int type, int[] list, int nl, int[] special, int ns, int arg1, int arg2, int arg3) {
        int rv = 0;

        switch (type) {
            case CHOICE_START:
                ai_choose_start(g, who, list, nl, special, ns);
                rv = 0;
                break;
            default:
                throw new RuntimeException("Unknown Choice");
        }

        /* Get player pointer */
        Player player = g.getPlayer(who);

        int choiceIndex = player.choice_size;
        player.choice_log[choiceIndex++] = type;
        player.choice_log[choiceIndex++] = rv;


        /* Check for number of list items available */
        if (nl != 0) {
            /* Add number of returned list items */
            player.choice_log[choiceIndex++] = nl;

            /* Copy list items */
            for (int i = 0; i < nl; i++) {
                /* Copy list item */
                player.choice_log[choiceIndex++] = list[i];
            }
        } else {
            player.choice_log[choiceIndex++] = nl;
        }

        /* Check for number of special items available */
        if (ns != 0) {
            /* Add number of returned special items */
            player.choice_log[choiceIndex++] = ns;
            /* Copy special items */
            for (int i = 0; i < ns; i++) {
                /* Copy special item */
                player.choice_log[choiceIndex++] = special[i];
            }
        } else {
            /* Add no special items */
            player.choice_log[choiceIndex++] = 0;
        }

        /* Mark new size of choice log */
        player.choice_size = choiceIndex;
        System.out.println("EmptyAI.make_choice:24 -" + type);
    }

    private class AIChosenResult {
        int best;
        double b_s;
    }

    private void ai_choose_start(Game g, int who, int[] list, int num, int[] special, int ns) {
        int i, best = -1, best_discards = -1;
        double b_s = -1;
        int target;
        AIChosenResult result = new AIChosenResult();

        /* Loop over choices of start world */
        for (i = 0; i < ns; i++) {
            /* Simulate game */
            Game sim = simulate_game(g, who);

            /* Try using this card */
            Engine.place_card(sim, who, special[i]);

            /* Assume discard to 4 */
            target = 4;

            /* Clear score */
            result.b_s = -1;
            /* Score best starting discards */
            //note: discards and score are return values
            ai_choose_discard_aux(sim, who, list, num, num - target, 0, result);

            /* Check for better than before */
            if (result.b_s > b_s) {
                /* Track best */
                b_s = result.b_s;
                best = special[i];
                best_discards = result.best;
            }
        }

        /* Remember start world chosen */
        special[0] = best;
        ns = 1;

        /* Start with no cards to discard */
        num = 0;

        /* Loop over set of chosen cards to discard */
        for (i = 0; (1 << i) <= best_discards; i++) {
            /* Check for bit set */
            if ((best_discards & (1 << i)) == (1 << i)) {
                /* Add card to discard list */
                list[num++] = list[i];
            }
        }
    }


    /*
    * Helper function for ai_choose_discard().
    */
    void ai_choose_discard_aux(Game g, int who, int list[], int n, int c,
                               int chosen, AIChosenResult result) {
        double score;
        int[] discards = new int[MAX_DECK];
        int num_discards = 0;

        /* Check for too few choices */
        if (c > n) return;

        /* Check for end */
        if (n == 0) {
            for (int i = 0; (1 << i) <= chosen; i++) {
                if ((chosen & (1 << i)) == (1 << i)) {
                    discards[num_discards++] = list[i];
                }
            }

            /* Copy game */
            Game sim = simulate_game(g, who);

            /* Apply choice */
            Engine.discard_callback(sim, who, discards, num_discards);

            /* Check for explore phase */
            if (sim.cur_action == ACT_EXPLORE_5_0) {
                /* Simulate most rest of turn */
                complete_turn(sim, COMPLETE_DEVSET);
            }

            /* Evaluate result */
            score = eval_game(sim, who);

            /* Check for better score */
            if (score_better(score, result.b_s)) {
                /* Save better choice */
                result.b_s = score;
                result.best = chosen;
            }

            /* Done */
            return;
        }

        /* Try without current card */
        ai_choose_discard_aux(g, who, list, n - 1, c, chosen << 1, result);

        /* Try with current card (if more can be chosen) */
        if (c != 0) ai_choose_discard_aux(g, who, list, n - 1, c - 1, (chosen << 1) + 1, result);
    }

    private double eval_game(Game sim, int who) {
        return 1;  //TODO: implement this... this is one of the places where the neural netwrk does its magic
    }

    private void complete_turn(Game sim, int completeDevset) {
        //TODO: implement this... the AI is effectively playing the game, so there is copy&paste code all around.
    }

    /**
     * Return true if the first score is at least as good as the second.
     * <p/>
     * Due to small cumulative errors from the neural network, a simple >= does
     * not work.
     */
    static boolean score_better(double s1, double s2) {
        return s1 >= s2 - 0.000001;
    }

    /*
    * Copy the game state to a temporary copy so that we can simulate the future.
    */
    Game simulate_game(Game orig, int who) {
        int i;

        Cloner cloner = new Cloner();
        Game sim = cloner.deepClone(orig);

        /* Loop over players */
        for (i = 0; i < sim.getNumPlayers(); i++) {
            /* Move choice log position to end */
            sim.getPlayer(i).choice_pos = sim.getPlayer(i).choice_size;
        }

        /* Check for first-level simulation */
        if (!sim.simulation) {
            /* Set simulation flag */
            sim.simulation = true;

            /* Lose real random seed */
            sim.random_seed = 1;

            /* Remember whose point-of-view to use */
            sim.sim_who = who;

            for (i = 0; i < sim.getNumPlayers(); i++) {
                sim.getPlayer(i).control = this;
            }
        }

        return sim;
    }


    @Override
    public void wait_answer(Game game, int who) {
        System.out.println("EmptyAI.wait_answer:29");
    }

    @Override
    public void explore_sample(Game game, int who, int draw, int keep, int discard_any) {
        System.out.println("EmptyAI.explore_sample:34");
    }

    @Override
    public int verify_choice(Game game, int who, int type, int[] list, int nl, int[] special, int ns, int arg1, int arg2, int arg3) {
        System.out.println("EmptyAI.verify_choice:39");
        return 0;
    }

    @Override
    public void game_over(Game game, int who) {
        System.out.println("EmptyAI.game_over:45");
    }

    @Override
    public void shutdown(Game game, int who) {
        System.out.println("EmptyAI.shutdown:50");
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    /*
 * Size of evaluator neural net.
 */
    public static final int EVAL_MISC = 77;
    public static final int EVAL_PLAYER = 138;
    public static final int EVAL_HIDDEN = 50;

    /*
    * Size of role predictor neural net.
    */
    public static final int ROLE_MISC = 76;
    public static final int ROLE_PLAYER = 114;
    public static final int ROLE_HIDDEN = 50;

    /*
    * Number of outputs for role predictor network (basic and advanced).
    */
    public static final int ROLE_OUT = 7;
    public static final int ROLE_OUT_ADV = 23;
    public static final int ROLE_OUT_EXP3 = 15;
    public static final int ROLE_OUT_ADV_EXP3 = 76;

    /*
    * Amount of current turn to complete simulation of.
    */
    public static final int COMPLETE_ROUND = 0;
    public static final int COMPLETE_PHASE = 1;
    public static final int COMPLETE_DEVSET = 2;

}
