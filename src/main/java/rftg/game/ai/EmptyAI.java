package rftg.game.ai;

import rftg.game.Decisions;
import rftg.game.Game;

public class EmptyAI implements Decisions {
    @Override
    public void init(Game game, int who, double factor) {
    }

    @Override
    public void notify_rotation(Game game, int who) {
    }

    @Override
    public void prepare_phase(Game game, int who, int phase, int arg) {
    }

    @Override
    public void make_choice(Game game, int who, int type, int[] list, int nl, int[] special, int ns, int arg1, int arg2, int arg3) {
    }

    @Override
    public void wait_answer(Game game, int who) {
    }

    @Override
    public void explore_sample(Game game, int who, int draw, int keep, int discard_any) {
    }

    @Override
    public int verify_choice(Game game, int who, int type, int[] list, int nl, int[] special, int ns, int arg1, int arg2, int arg3) {
        return 0;
    }

    @Override
    public void game_over(Game game, int who) {
    }

    @Override
    public void shutdown(Game game, int who) {
    }
}
