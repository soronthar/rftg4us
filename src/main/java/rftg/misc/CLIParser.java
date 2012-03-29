package rftg.misc;

import rftg.game.GameConf;

public class CLIParser {
    //TODO: use a proper library for CLI parsing.

    public GameConf parse(String[] args) {
        GameConf conf = new GameConf();
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if ("-v".equals(arg)) {
                conf.verbose = true;
            } else if ("-p".equals(arg)) {
                i++;
                conf.num_players = Integer.parseInt(args[i]);
            } else if ("-a".equals(arg)) {
                conf.advanced = true;
            } else if ("-e".equals(arg)) {
                i++;
                conf.expanded = Integer.parseInt(args[i]);
            } else if ("-n".equals(arg)) {
                i++;
                conf.n = Integer.parseInt(args[i]);
            } else if ("-r".equals(arg)) {
                i++;
                conf.seed = Integer.parseInt(args[i]);
            } else if ("-f".equals(arg)) {
                conf.factor = Integer.parseInt(args[i]);
            } else if ("-c".equals(arg)) {
                i++;
                conf.cardFile = args[i];
            }
        }
        return conf;
    }
}
