package rftg.learner;

import rftg.game.Engine;
import rftg.game.Game;
import rftg.game.GameConf;
import rftg.game.Player;
import rftg.misc.CLIParser;

public class Learner {


    void myMain(String[] args) {
        CLIParser parser = new CLIParser();
        GameConf conf = parser.parse(args);
        Game game = new Game(conf);

        Engine engine = new Engine();
        /* Play a number of games */
        for (int i = 0; i < conf.n; i++) {
            /* Initialize game */
            engine.init_game(game);

            /* Begin game */
            engine.begin_game(game);

            /* Play game rounds until finished */
            while (engine.game_round(game)) ;

            /* Score game */
            engine.score_game(game);

            /* Print result */
            for (int j = 0; j < game.getNumPlayers(); j++) {
                /* Print score */
                Player player = game.getPlayer(j);
                System.out.printf("%s: %d\n", player.name, player.end_vp);
            }

            /* Declare winner */
            engine.declare_winner(game);

            /* Call player game over functions */
            for (int j = 0; j < game.getNumPlayers(); j++) {
                Player player = game.getPlayer(j);

                /* Call game over function */
                player.control.game_over(game, j);

                /* Clear choice log */
                player.choice_size = 0;
                player.choice_pos = 0;
            }
        }

        /* Call interface shutdown functions */
        for (int i = 0; i < game.getNumPlayers(); i++) {
            /* Call shutdown function */
            game.getPlayer(i).control.shutdown(game, i);
        }
    }


    public static void main(String[] args) {
        new Learner().myMain(args);
    }
}
