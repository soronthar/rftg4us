package rftg.learner;

import rftg.bundle.cards.CardsDesigns;
import rftg.game.Engine;
import rftg.game.Game;
import rftg.game.Player;
import rftg.game.ai.EmptyAI;

public class Learner {
    boolean verbose = false;

    void myMain(String[] args) {
        Game game = new Game();

        int expansion = 0;
        boolean advanced = false;
        int num_players = 3;
        byte[] buff = new byte[1024];
        double factor = 1.0;
        int n = 20;

        game.randomSeed = 1;
//        game.randomSeed=System.currentTimeMillis();

        CardsDesigns designs = new CardsDesigns();
        designs.loadFrom("cards.txt");
//TODO: use a proper library for CLI parsing.
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if ("-v".equals(arg)) {
                verbose = true;
            } else if ("-p".equals(arg)) {
                i++;
                num_players = Integer.parseInt(args[i]);
            } else if ("-a".equals(arg)) {
                advanced = true;
            } else if ("-e".equals(arg)) {
                i++;
                expansion = Integer.parseInt(args[i]);
            } else if ("-n".equals(arg)) {
                i++;
                n = Integer.parseInt(args[i]);
            } else if ("-r".equals(arg)) {
                i++;
                game.randomSeed = Integer.parseInt(args[i]);
            } else if ("-f".equals(arg)) {
                factor = Integer.parseInt(args[i]);
            }
        }
        game.playerCount = num_players;

        game.expansionLevel = expansion;

        game.advanced = advanced;

        /* Assume no options disabled */
        game.goalDisabled = false; //TODO: flip this madness...
        game.takeoverDisabled = false;

        game.initPlayers();

        /* Call initialization functions */
        for (int i = 0; i < num_players; i++) {
            Player player = game.players[i];
            /* Set player name */
            player.name = "Player " + i;

            /* Set player interfaces to AI functions */
            player.control = new EmptyAI();

            /* Initialize AI */
            player.control.init(game, i, factor);

            /* Clear choice log size and position */
            player.choice_size = 0;
            player.choice_pos = 0;
        }
        Engine engine = new Engine();
        /* Play a number of games */
        for (int i = 0; i < n; i++) {
            /* Initialize game */
            engine.init_game(game);

            /* Begin game */
            engine.begin_game(game);

            /* Play game rounds until finished */
            while (engine.game_round(game)) ;

            /* Score game */
            engine.score_game(game);

            /* Print result */
            for (int j = 0; j < num_players; j++) {
                /* Print score */
                Player player = game.players[j];
                System.out.printf("%s: %d\n", player.name, player.end_vp);
            }

            /* Declare winner */
            engine.declare_winner(game);

            /* Call player game over functions */
            for (int j = 0; j < num_players; j++) {
                Player player = game.players[j];

                /* Call game over function */
                player.control.game_over(game, j);

                /* Clear choice log */
                player.choice_size = 0;
                player.choice_pos = 0;
            }
        }

        /* Call interface shutdown functions */
        for (int i = 0; i < num_players; i++) {
            /* Call shutdown function */
            game.players[i].control.shutdown(game, i);
        }
    }


    public static void main(String[] args) {
        new Learner().myMain(args);
    }
}
