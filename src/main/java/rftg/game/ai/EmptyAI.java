package rftg.game.ai;

import rftg.game.Constants;
import rftg.game.Decisions;
import rftg.game.Game;

public class EmptyAI implements Decisions {
    @Override
    public void init(Game game, int who, double factor) {
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
        int rv;

        switch (type) {
            case Constants.CHOICE_DISCARD:
                /* Choose discards */
//                ai_choose_discard(g, who, list, nl, arg1);
                rv = 0;

                break;
            default:
                break;
        }
        System.out.println("EmptyAI.make_choice:24 -" + type);
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
}
