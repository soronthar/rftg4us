package rftg.game.cards;

import rftg.bundle.cards.powers.*;

public class Power {
    public int phase;
    public long code;
    public int value;
    public int times;

    //(LAPP) adding interface to fix warning of using anonymous Enum type
    public boolean hasFlag(PowersInterface power) {
        long value = -1;
        switch (phase) {
            case 1:
                value = ((ExplorePowers) power).getValue();
                break;
            case 2:
                value = ((DevelopPowers) power).getValue();
                break;
            case 3:
                value = ((SettlePowers) power).getValue();
                break;
            case 4:
                value = ((ConsumePowers) power).getValue();
                break;
            case 5:
                value = ((ProducePowers) power).getValue();
                break;

        }
        return (code & value) == value;
    }
}
