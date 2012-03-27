package rftg.game.cards;

import rftg.bundle.cards.powers.SettlePowers;

public class Power {
    public int phase;
    public long code;
    public int value;
    public int times;

    public boolean hasFlag(SettlePowers power) {
        return (code & power.getValue()) == power.getValue();
    }
}
