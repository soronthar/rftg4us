package rftg.game;

public interface Decisions {
    void init(Game game, int who, double factor);

    void notify_rotation(Game game, int who);

    void prepare_phase(Game game, int who, int phase, int arg);

    void make_choice(Game game, int who, int type, int list[], int nl, int special[], int ns, int arg1, int arg2, int arg3);

    void wait_answer(Game game, int who);

    void explore_sample(Game game, int who, int draw, int keep, int discard_any);

    int verify_choice(Game game, int who, int type, int list[], int nl, int special[], int ns, int arg1, int arg2, int arg3);

    void game_over(Game game, int who);

    void shutdown(Game game, int who);
}
