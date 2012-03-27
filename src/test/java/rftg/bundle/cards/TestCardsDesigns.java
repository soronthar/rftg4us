package rftg.bundle.cards;

import junit.framework.TestCase;
import rftg.game.Constants;
import rftg.game.cards.Design;
import rftg.game.cards.Power;

public class TestCardsDesigns extends TestCase {

    public void testLoad() {
        CardsDesigns designs = new CardsDesigns();
        assertEquals(0, designs.count());
        designs.loadFrom("cards.txt");

        assertEquals(Constants.MAX_DESIGN, designs.count());

        Design firstDesign = designs.get(2);

        assertEquals("Name", "Alpha Centauri", firstDesign.name);
        assertEquals("index", 2, firstDesign.index);
        assertEquals("Type", 1, firstDesign.type);
        assertEquals("Cost", 2, firstDesign.cost);
        assertEquals("VP", 0, firstDesign.vp);
        assertEquals("Good_Type", GoodType.RARE, firstDesign.good_type);

        assertEquals("Expansion 1", 1, firstDesign.expand[0]);
        assertEquals("Expansion 2", 1, firstDesign.expand[1]);
        assertEquals("Expansion 3", 1, firstDesign.expand[2]);
        assertEquals("Expansion 4", 1, firstDesign.expand[3]);

        assertTrue("Flags.WINDFALL", firstDesign.hasFlag(Flags.WINDFALL));
        assertTrue("Flags.START", firstDesign.hasFlag(Flags.START));
        assertTrue("Flags.START_BLUE", firstDesign.hasFlag(Flags.START_BLUE));

        assertEquals("Num_Power", 2, firstDesign.num_power);
        Power[] powers = firstDesign.powers;
        assertEquals("powers[0].phase", 3, powers[0].phase);
        assertEquals("powers[0].times", -1, powers[0].times);
        assertEquals("powers[0].value", -1, powers[0].value);
        assertEquals("powers[0].code", -1, powers[0].code);

        assertEquals("powers[1].phase", -1, powers[1].phase);
        assertEquals("powers[1].times", -1, powers[1].times);
        assertEquals("powers[1].value", -1, powers[1].value);
        assertEquals("powers[1].code", -1, powers[1].code);


        assertEquals("Num_Vp_Bonus", 1, firstDesign.num_vp_bonus);
//        firstDesign.bonuses
//        firstDesign.expand
//        firstDesign.power


    }
}
