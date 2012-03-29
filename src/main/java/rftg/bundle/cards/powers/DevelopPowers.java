package rftg.bundle.cards.powers;

import rftg.game.Constants;

public enum DevelopPowers implements PowersInterface {
    DRAW(Constants.P2_DRAW),
    REDUCE(Constants.P2_REDUCE),
    DRAW_AFTER(Constants.P2_DRAW_AFTER),
    EXPLORE(Constants.P2_EXPLORE),
    DISCARD_REDUCE(Constants.P2_DISCARD_REDUCE),
    SAVE_COST(Constants.P2_SAVE_COST),
    PRESTIGE(Constants.P2_PRESTIGE),
    PRESTIGE_REBEL(Constants.P2_PRESTIGE_REBEL),
    PRESTIGE_SIX(Constants.P2_PRESTIGE_SIX),
    CONSUME_RARE(Constants.P2_CONSUME_RARE);

    private long value;

    DevelopPowers(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
