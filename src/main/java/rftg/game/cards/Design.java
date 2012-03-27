package rftg.game.cards;

import rftg.bundle.cards.Flags;
import rftg.bundle.cards.GoodType;

import static rftg.game.Constants.*;

public class Design {
    public String name;
    public int index;
    public int type;  //TODO: convert this to an enumeration, perhaps?
    public int cost;
    public int vp;
    public int[] expand = new int[MAX_EXPANSION];
    public GoodType good_type;
    public int flags;
    public int dup;
    public int num_power = 0;
    public Power[] powers = new Power[MAX_POWER];

    public int num_vp_bonus;

    public VPBonus[] bonuses = new VPBonus[MAX_VP_BONUS];

    public Design(int index, String name) {
        this.name = name;
        this.index = index;
    }

    public boolean hasFlag(Flags flag) {
        return (this.flags & flag.getValue()) == flag.getValue();
    }
}
