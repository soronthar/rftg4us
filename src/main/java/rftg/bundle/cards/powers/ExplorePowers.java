package rftg.bundle.cards.powers;

import rftg.game.Constants;

public enum ExplorePowers implements PowersInterface {
    DRAW(Constants.P1_DRAW),
    KEEP(Constants.P1_KEEP),
    DISCARD_ANY(Constants.P1_DISCARD_ANY),
    DISCARD_PRESTIGE(Constants.P1_DISCARD_PRESTIGE);

    private long value;

    ExplorePowers(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
