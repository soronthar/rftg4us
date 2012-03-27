package rftg.bundle.cards.powers;

import rftg.game.Constants;

public enum SettlePowers {
    REDUCE(Constants.P3_REDUCE),
    NOVELTY(Constants.P3_NOVELTY),
    RARE(Constants.P3_RARE),
    GENE(Constants.P3_GENE),
    ALIEN(Constants.P3_ALIEN),
    DISCARD(Constants.P3_DISCARD),
    REDUCE_ZERO(Constants.P3_REDUCE_ZERO),
    MILITARY_HAND(Constants.P3_MILITARY_HAND),
    EXTRA_MILITARY(Constants.P3_EXTRA_MILITARY),
    AGAINST_REBEL(Constants.P3_AGAINST_REBEL),
    AGAINST_CHROMO(Constants.P3_AGAINST_CHROMO),
    PER_MILITARY(Constants.P3_PER_MILITARY),
    PER_CHROMO(Constants.P3_PER_CHROMO),
    IF_IMPERIUM(Constants.P3_IF_IMPERIUM),
    PAY_MILITARY(Constants.P3_PAY_MILITARY),
    PAY_DISCOUNT(Constants.P3_PAY_DISCOUNT),
    PAY_PRESTIGE(Constants.P3_PAY_PRESTIGE),
    CONQUER_SETTLE(Constants.P3_CONQUER_SETTLE),
    NO_TAKEOVER(Constants.P3_NO_TAKEOVER),
    DRAW_AFTER(Constants.P3_DRAW_AFTER),
    EXPLORE_AFTER(Constants.P3_EXPLORE_AFTER),
    PRESTIGE(Constants.P3_PRESTIGE),
    PRESTIGE_REBEL(Constants.P3_PRESTIGE_REBEL),
    SAVE_COST(Constants.P3_SAVE_COST),
    PLACE_TWO(Constants.P3_PLACE_TWO),
    PLACE_MILITARY(Constants.P3_PLACE_MILITARY),
    CONSUME_RARE(Constants.P3_CONSUME_RARE),
    CONSUME_GENE(Constants.P3_CONSUME_GENE),
    CONSUME_PRESTIGE(Constants.P3_CONSUME_PRESTIGE),
    AUTO_PRODUCE(Constants.P3_AUTO_PRODUCE),
    PRODUCE_PRESTIGE(Constants.P3_PRODUCE_PRESTIGE),
    TAKEOVER_REBEL(Constants.P3_TAKEOVER_REBEL),
    TAKEOVER_IMPERIUM(Constants.P3_TAKEOVER_IMPERIUM),
    TAKEOVER_MILITARY(Constants.P3_TAKEOVER_MILITARY),
    TAKEOVER_PRESTIGE(Constants.P3_TAKEOVER_PRESTIGE),
    DESTROY(Constants.P3_DESTROY),
    TAKEOVER_DEFENSE(Constants.P3_TAKEOVER_DEFENSE),
    PREVENT_TAKEOVER(Constants.P3_PREVENT_TAKEOVER),
    UPGRADE_WORLD(Constants.P3_UPGRADE_WORLD);


    private long value;

    SettlePowers(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
