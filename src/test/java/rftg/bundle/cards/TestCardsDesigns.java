package rftg.bundle.cards;

import junit.framework.TestCase;
import rftg.bundle.cards.powers.ProducePowers;
import rftg.bundle.cards.powers.SettlePowers;
import rftg.game.Constants;
import rftg.game.cards.Design;
import rftg.game.cards.Power;
import rftg.game.cards.VPBonus;

public class TestCardsDesigns extends TestCase {

    public void testLoadPlanet() {
        CardsDesigns designs = new CardsDesigns();
        assertEquals(0, designs.count());
        designs.loadFrom("cards.txt");

        assertEquals(Constants.MAX_DESIGN, designs.count());

        Design planetDesign = designs.get(2);

        assertEquals("planetDesign Name", "Alpha Centauri", planetDesign.name);
        assertEquals("planetDesign index", 2, planetDesign.index);
        assertEquals("planetDesign Type", 1, planetDesign.type);
        assertEquals("planetDesign Cost", 2, planetDesign.cost);
        assertEquals("planetDesign VP", 0, planetDesign.vp);
        assertEquals("planetDesign Good_Type", GoodType.RARE, planetDesign.good_type);

        assertEquals("planetDesign Expansion 1", 1, planetDesign.expand[0]);
        assertEquals("planetDesign Expansion 2", 1, planetDesign.expand[1]);
        assertEquals("planetDesign Expansion 3", 1, planetDesign.expand[2]);
        assertEquals("planetDesign Expansion 4", 1, planetDesign.expand[3]);
        assertEquals("planetDesign Flags",
                Flags.WINDFALL.getValue() | Flags.START.getValue() | Flags.START_BLUE.getValue(),
                planetDesign.flags);

        assertTrue("planetDesign Flags.WINDFALL", planetDesign.hasFlag(Flags.WINDFALL));
        assertTrue("planetDesign Flags.START", planetDesign.hasFlag(Flags.START));
        assertTrue("planetDesign Flags.START_BLUE", planetDesign.hasFlag(Flags.START_BLUE));

        assertEquals("planetDesign Num_Power", 2, planetDesign.num_power);
        Power[] powers = planetDesign.powers;
        assertEquals("planetDesign powers[0].phase", 3, powers[0].phase);
        assertEquals("planetDesign powers[0].code",
                (SettlePowers.REDUCE.getValue() | SettlePowers.RARE.getValue()), powers[0].code);

        assertTrue("planetDesign SettlePowers.REDUCE", powers[0].hasFlag(SettlePowers.REDUCE));
        assertTrue("planetDesign SettlePowers.RARE", powers[0].hasFlag(SettlePowers.RARE));

        assertEquals("planetDesign powers[0].times", 0, powers[0].times);
        assertEquals("planetDesign powers[0].value", 1, powers[0].value);

        assertEquals("planetDesign powers[1].phase", 3, powers[1].phase);
        assertEquals("planetDesign powers[1].times", 0, powers[1].times);
        assertEquals("planetDesign powers[1].value", 1, powers[1].value);
        assertEquals("planetDesign powers[1].code",
                (SettlePowers.EXTRA_MILITARY.getValue() | SettlePowers.RARE.getValue()), powers[1].code);

        assertTrue("planetDesign SettlePowers.EXTRA_MILITARY", powers[1].hasFlag(SettlePowers.EXTRA_MILITARY));
        assertTrue("planetDesign SettlePowers.RARE", powers[1].hasFlag(SettlePowers.RARE));

        assertEquals("planetDesign Num_Vp_Bonus", 0, planetDesign.num_vp_bonus);
    }

    public void testLoadImprovement() {
        CardsDesigns designs = new CardsDesigns();
        assertEquals(0, designs.count());
        designs.loadFrom("cards.txt");

        assertEquals(Constants.MAX_DESIGN, designs.count());

        Design developDesign = designs.get(39);
        assertEquals("developDesign Name", "Pan-Galactic League", developDesign.name);
        assertEquals("developDesign index", 39, developDesign.index);
        assertEquals("developDesign Type", 2, developDesign.type);
        assertEquals("developDesign Cost", 6, developDesign.cost);
        assertEquals("developDesign VP", 0, developDesign.vp);

        assertEquals("developDesign Good_Type", GoodType.NONE, developDesign.good_type);

        assertEquals("developDesign Expansion 1", 1, developDesign.expand[0]);
        assertEquals("developDesign Expansion 2", 1, developDesign.expand[1]);
        assertEquals("developDesign Expansion 3", 1, developDesign.expand[2]);
        assertEquals("developDesign Expansion 4", 1, developDesign.expand[3]);

        assertEquals("developDesign Flags", Flags.NONE.getValue(), developDesign.flags);

        assertEquals("developDesign Num_Power", 2, developDesign.num_power);
        Power[] powers = developDesign.powers;
        assertEquals("developDesign powers[0].phase", 3, powers[0].phase);
        assertEquals("developDesign powers[0].code", SettlePowers.EXTRA_MILITARY.getValue(), powers[0].code);

        assertTrue("developDesign SettlePowers.EXTRA_MILITARY", powers[0].hasFlag(SettlePowers.EXTRA_MILITARY));
        assertEquals("developDesign powers[0].value", -1, powers[0].value);
        assertEquals("developDesign powers[0].times", 0, powers[0].times);

        assertEquals("developDesign powers[1].phase", 5, powers[1].phase);
        assertEquals("developDesign powers[1].times", 0, powers[1].times);
        assertEquals("developDesign powers[1].value", 1, powers[1].value);
        assertEquals("developDesign powers[1].code", ProducePowers.DRAW_WORLD_GENE.getValue(), powers[1].code);
        assertTrue("developDesign ProducePowers.DRAW_WORLD_GENE", powers[1].hasFlag(ProducePowers.DRAW_WORLD_GENE));

        assertEquals("developDesign Num_Vp_Bonus", 4, developDesign.num_vp_bonus);
        VPBonus[] vpBonuses = developDesign.bonuses;

        assertEquals("developDesign vpBonuses[0].", 2, vpBonuses[0].point);
        assertEquals("developDesign vpBonuses[0].", VPBonusType.GENE_PRODUCTION, vpBonuses[0].type);
        assertEquals("developDesign vpBonuses[0].", "N/A", vpBonuses[0].name);

        assertEquals("developDesign vpBonuses[1].", 2, vpBonuses[1].point);
        assertEquals("developDesign vpBonuses[1].", VPBonusType.GENE_WINDFALL, vpBonuses[1].type);
        assertEquals("developDesign vpBonuses[1].", "N/A", vpBonuses[1].name);

        assertEquals("developDesign vpBonuses[2].", 1, vpBonuses[2].point);
        assertEquals("developDesign vpBonuses[2].", VPBonusType.MILITARY, vpBonuses[2].type);
        assertEquals("developDesign vpBonuses[2].", "N/A", vpBonuses[2].name);

        assertEquals("developDesign vpBonuses[3].", 3, vpBonuses[3].point);
        assertEquals("developDesign vpBonuses[3].", VPBonusType.NAME, vpBonuses[3].type);
        assertEquals("developDesign vpBonuses[3].", "Contact Specialist", vpBonuses[3].name);


    }
}
