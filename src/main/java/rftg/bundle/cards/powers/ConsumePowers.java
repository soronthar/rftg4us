package rftg.bundle.cards.powers;

import rftg.game.Constants;

public enum ConsumePowers {
    TRADE_ANY(Constants.P4_TRADE_ANY),
    TRADE_NOVELTY(Constants.P4_TRADE_NOVELTY),
    TRADE_RARE(Constants.P4_TRADE_RARE),
    TRADE_GENE(Constants.P4_TRADE_GENE),
    TRADE_ALIEN(Constants.P4_TRADE_ALIEN),
    TRADE_THIS(Constants.P4_TRADE_THIS),
    TRADE_BONUS_CHROMO(Constants.P4_TRADE_BONUS_CHROMO),
    NO_TRADE(Constants.P4_NO_TRADE),
    TRADE_ACTION(Constants.P4_TRADE_ACTION),
    TRADE_NO_BONUS(Constants.P4_TRADE_NO_BONUS),
    CONSUME_ANY(Constants.P4_CONSUME_ANY),
    CONSUME_NOVELTY(Constants.P4_CONSUME_NOVELTY),
    CONSUME_RARE(Constants.P4_CONSUME_RARE),
    CONSUME_GENE(Constants.P4_CONSUME_GENE),
    CONSUME_ALIEN(Constants.P4_CONSUME_ALIEN),
    CONSUME_THIS(Constants.P4_CONSUME_THIS),
    CONSUME_TWO(Constants.P4_CONSUME_TWO),
    CONSUME_3_DIFF(Constants.P4_CONSUME_3_DIFF),
    CONSUME_N_DIFF(Constants.P4_CONSUME_N_DIFF),
    CONSUME_ALL(Constants.P4_CONSUME_ALL),
    CONSUME_PRESTIGE(Constants.P4_CONSUME_PRESTIGE),
    GET_CARD(Constants.P4_GET_CARD),
    GET_2_CARD(Constants.P4_GET_2_CARD),
    GET_VP(Constants.P4_GET_VP),
    GET_PRESTIGE(Constants.P4_GET_PRESTIGE),
    DRAW(Constants.P4_DRAW),
    DRAW_LUCKY(Constants.P4_DRAW_LUCKY),
    DISCARD_HAND(Constants.P4_DISCARD_HAND),
    ANTE_CARD(Constants.P4_ANTE_CARD),
    VP(Constants.P4_VP),
    RECYCLE(Constants.P4_VP);

    private long value;

    ConsumePowers(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
