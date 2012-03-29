package rftg.bundle.game;

import junit.framework.TestCase;
import rftg.game.Game;
import rftg.game.GameConf;
import rftg.game.Player;

public class TestGame extends TestCase {

    public void testInit() {
        GameConf conf = GameConf.DEFAULT_CONF;
        Game game = new Game(conf);

        assertEquals("game.advanced", conf.advanced, game.advanced);
        assertEquals("game.goal_disabled", conf.goal_disabled, game.goal_disabled);
        assertEquals("game.takeover_disabled", conf.takeover_disabled, game.takeover_disabled);
        assertEquals("game.expanded", conf.expanded, game.expanded);
        assertEquals("game.getNumPlayers()", conf.num_players, game.getNumPlayers());
        assertEquals("game.random_seed", conf.seed, game.random_seed);


        for (int i = 0; i < game.getNumPlayers(); i++) {
            Player player = game.getPlayer(i);
            assertTrue(Integer.toString(i), player.control.isInitialized());
        }


    }
}
