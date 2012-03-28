package rftg.bundle.cards;

import rftg.game.Constants;

public enum Flags {
    MILITARY(Constants.FLAG_MILITARY),
    WINDFALL(Constants.FLAG_WINDFALL),
    START(Constants.FLAG_START),
    START_RED(Constants.FLAG_START_RED),
    START_BLUE(Constants.FLAG_START_BLUE),
    REBEL(Constants.FLAG_REBEL),
    UPLIFT(Constants.FLAG_UPLIFT),
    ALIEN(Constants.FLAG_ALIEN),
    TERRAFORMING(Constants.FLAG_TERRAFORMING),
    IMPERIUM(Constants.FLAG_IMPERIUM),
    CHROMO(Constants.FLAG_CHROMO),
    PRESTIGE(Constants.FLAG_PRESTIGE),
    STARTHAND_3(Constants.FLAG_STARTHAND_3),
    START_SAVE(Constants.FLAG_START_SAVE),
    DISCARD_TO_12(Constants.FLAG_DISCARD_TO_12),
    GAME_END_14(Constants.FLAG_GAME_END_14),
    TAKE_DISCARDS(Constants.FLAG_TAKE_DISCARDS),
    SELECT_LAST(Constants.FLAG_SELECT_LAST),
    NONE(0);

    private int value;

    Flags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
