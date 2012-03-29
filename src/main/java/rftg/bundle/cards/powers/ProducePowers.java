package rftg.bundle.cards.powers;

import rftg.game.Constants;

public enum ProducePowers implements PowersInterface {
    PRODUCE(Constants.P5_PRODUCE),
    WINDFALL_ANY(Constants.P5_WINDFALL_ANY),
    WINDFALL_NOVELTY(Constants.P5_WINDFALL_NOVELTY),
    WINDFALL_RARE(Constants.P5_WINDFALL_RARE),
    WINDFALL_GENE(Constants.P5_WINDFALL_GENE),
    WINDFALL_ALIEN(Constants.P5_WINDFALL_ALIEN),
    NOT_THIS(Constants.P5_NOT_THIS),
    DISCARD(Constants.P5_DISCARD),
    DRAW(Constants.P5_DRAW),
    DRAW_IF(Constants.P5_DRAW_IF),
    PRESTIGE_IF(Constants.P5_PRESTIGE_IF),
    DRAW_EACH_NOVELTY(Constants.P5_DRAW_EACH_NOVELTY),
    DRAW_EACH_RARE(Constants.P5_DRAW_EACH_RARE),
    DRAW_EACH_GENE(Constants.P5_DRAW_EACH_GENE),
    DRAW_EACH_ALIEN(Constants.P5_DRAW_EACH_ALIEN),
    DRAW_WORLD_GENE(Constants.P5_DRAW_WORLD_GENE),
    DRAW_MOST_RARE(Constants.P5_DRAW_MOST_RARE),
    DRAW_MOST_PRODUCED(Constants.P5_DRAW_MOST_PRODUCED),
    DRAW_DIFFERENT(Constants.P5_DRAW_DIFFERENT),
    PRESTIGE_MOST_CHROMO(Constants.P5_PRESTIGE_MOST_CHROMO),
    DRAW_MILITARY(Constants.P5_DRAW_MILITARY),
    DRAW_REBEL(Constants.P5_DRAW_REBEL),
    DRAW_CHROMO(Constants.P5_DRAW_CHROMO),
    TAKE_SAVED(Constants.P5_TAKE_SAVED);


    private long value;

    ProducePowers(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
