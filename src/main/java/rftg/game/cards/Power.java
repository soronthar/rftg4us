package rftg.game.cards;

import rftg.bundle.cards.powers.*;

public class Power {
    public int phase;
    public long code;
    public int value;
    public int times;

    public boolean hasFlag(PowersInterface... powers) {
        long value = -1;
        boolean result = true;
        for (PowersInterface power : powers) {

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
            result = result && ((code & value) == value);
        }

        return result;
    }
}
