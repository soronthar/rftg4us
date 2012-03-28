package rftg.game;

public class Constants {
    /**
     * Maximum number of players.
     */
    public static final int MAX_PLAYER = 6;

    /*
    * Maximum number of expansion levels.
    */
    public static final int MAX_EXPANSION = 4;

    /*
    * Number of card designs.
    */
    public static final int MAX_DESIGN = 191;

    /*
    * Number of cards in the deck.
    */
    public static final int MAX_DECK = 228;

    /*
    * Number of powers per card.
    */
    public static final int MAX_POWER = 5;

    /*
    * Number of special VP bonuses per card.
    */
    public static final int MAX_VP_BONUS = 6;

    /*
    * Maximum number of pending takeovers.
    */
    public static final int MAX_TAKEOVER = 12;

    /*
    * Number of intermediate goals.
    */
    public static final int MAX_GOAL = 20;

    /*
    * Number of turn phases.
    */
    public static final int MAX_PHASE = 7;

    /*
    * Number of available actions.
    */
    public static final int MAX_ACTION = 10;

    /*
    * Number of Search categories.
    */
    public static final int MAX_SEARCH = 9;

    /*
    * Number of card locations.
    */
    public static final int MAX_WHERE = 7;


    /*
    * Round phases.
    */
    public static final int PHASE_ACTION = 0;
    public static final int PHASE_EXPLORE = 1;
    public static final int PHASE_DEVELOP = 2;
    public static final int PHASE_SETTLE = 3;
    public static final int PHASE_CONSUME = 4;
    public static final int PHASE_PRODUCE = 5;
    public static final int PHASE_DISCARD = 6;

    /*
    * Player action choices.
    */
    public static final int ACT_SEARCH = 0;
    public static final int ACT_EXPLORE_5_0 = 1;
    public static final int ACT_EXPLORE_1_1 = 2;
    public static final int ACT_DEVELOP = 3;
    public static final int ACT_DEVELOP2 = 4;
    public static final int ACT_SETTLE = 5;
    public static final int ACT_SETTLE2 = 6;
    public static final int ACT_CONSUME_TRADE = 7;
    public static final int ACT_CONSUME_X2 = 8;
    public static final int ACT_PRODUCE = 9;

    public static final int ACT_MASK = 0x7f;
    public static final int ACT_PRESTIGE = 0x80;

    /*
    * Card types.
    */
    public static final int TYPE_WORLD = 1;
    public static final int TYPE_DEVELOPMENT = 2;

    /*
    * Card flags.
    */
    public static final int FLAG_MILITARY = 0x1;
    public static final int FLAG_WINDFALL = 0x2;
    public static final int FLAG_START = 0x4;

    public static final int FLAG_START_RED = 0x8;
    public static final int FLAG_START_BLUE = 0x10;

    public static final int FLAG_REBEL = 0x20;
    public static final int FLAG_UPLIFT = 0x40;
    public static final int FLAG_ALIEN = 0x80;
    public static final int FLAG_TERRAFORMING = 0x100;
    public static final int FLAG_IMPERIUM = 0x200;
    public static final int FLAG_CHROMO = 0x400;

    public static final int FLAG_PRESTIGE = 0x800;

    public static final int FLAG_STARTHAND_3 = 0x1000;
    public static final int FLAG_START_SAVE = 0x2000;
    public static final int FLAG_DISCARD_TO_12 = 0x4000;
    public static final int FLAG_GAME_END_14 = 0x8000;
    public static final int FLAG_TAKE_DISCARDS = 0x10000;
    public static final int FLAG_SELECT_LAST = 0x20000;


    /*
    * Good types (and cost).
    */
    public static final int GOOD_ANY = 1;
    public static final int GOOD_NOVELTY = 2;
    public static final int GOOD_RARE = 3;
    public static final int GOOD_GENE = 4;
    public static final int GOOD_ALIEN = 5;

    /*
    * Card locations.
    */
    public static final int WHERE_DECK = 0;
    public static final int WHERE_DISCARD = 1;
    public static final int WHERE_HAND = 2;
    public static final int WHERE_ACTIVE = 3;
    public static final int WHERE_GOOD = 4;
    public static final int WHERE_SAVED = 5;
    public static final int WHERE_ASIDE = 6;

    /*
    * Search categories.
    */
    public static final int SEARCH_DEV_MILITARY = 0;
    public static final int SEARCH_MILITARY_WINDFALL = 1;
    public static final int SEARCH_PEACEFUL_WINDFALL = 2;
    public static final int SEARCH_CHROMO_WORLD = 3;
    public static final int SEARCH_ALIEN_WORLD = 4;
    public static final int SEARCH_CONSUME_TWO = 5;
    public static final int SEARCH_MILITARY_5 = 6;
    public static final int SEARCH_6_DEV = 7;
    public static final int SEARCH_TAKEOVER = 8;


/*
 * Card power effects by phase.
 */

    /* Phase one -- Explore */
    public static final long P1_DRAW = 1;
    public static final long P1_KEEP = 1 << 1;
    public static final long P1_DISCARD_ANY = 1 << 2;
    public static final long P1_DISCARD_PRESTIGE = 1 << 3;


    /* Phase two -- Develop */
    public static final long P2_DRAW = 1;
    public static final long P2_REDUCE = 1 << 1;
    public static final long P2_DRAW_AFTER = 1 << 2;
    public static final long P2_EXPLORE = 1 << 3;
    public static final long P2_DISCARD_REDUCE = 1 << 4;
    public static final long P2_SAVE_COST = 1 << 5;
    public static final long P2_PRESTIGE = 1 << 6;
    public static final long P2_PRESTIGE_REBEL = 1 << 7;
    public static final long P2_PRESTIGE_SIX = 1 << 8;
    public static final long P2_CONSUME_RARE = 1 << 9;


    /* Phase three -- Settle */
    public static final long P3_REDUCE = 1;
    public static final long P3_NOVELTY = 1L << 1;
    public static final long P3_RARE = 1L << 2;
    public static final long P3_GENE = 1L << 3;
    public static final long P3_ALIEN = 1L << 4;
    public static final long P3_DISCARD = 1L << 5;
    public static final long P3_REDUCE_ZERO = 1L << 6;
    public static final long P3_MILITARY_HAND = 1L << 7;
    public static final long P3_EXTRA_MILITARY = 1L << 8;
    public static final long P3_AGAINST_REBEL = 1L << 9;
    public static final long P3_AGAINST_CHROMO = 1L << 10;
    public static final long P3_PER_MILITARY = 1L << 11;
    public static final long P3_PER_CHROMO = 1L << 12;
    public static final long P3_IF_IMPERIUM = 1L << 13;
    public static final long P3_PAY_MILITARY = 1L << 14;
    public static final long P3_PAY_DISCOUNT = 1L << 15;
    public static final long P3_PAY_PRESTIGE = 1L << 16;
    public static final long P3_CONQUER_SETTLE = 1L << 17;
    public static final long P3_NO_TAKEOVER = 1L << 18;
    public static final long P3_DRAW_AFTER = 1L << 19;
    public static final long P3_EXPLORE_AFTER = 1L << 20;
    public static final long P3_PRESTIGE = 1L << 21;
    public static final long P3_PRESTIGE_REBEL = 1L << 22;
    public static final long P3_SAVE_COST = 1L << 23;
    public static final long P3_PLACE_TWO = 1L << 24;
    public static final long P3_PLACE_MILITARY = 1L << 25;
    public static final long P3_CONSUME_RARE = 1L << 26;
    public static final long P3_CONSUME_GENE = 1L << 27;
    public static final long P3_CONSUME_PRESTIGE = 1L << 28;
    public static final long P3_AUTO_PRODUCE = 1L << 29;
    public static final long P3_PRODUCE_PRESTIGE = 1L << 30;
    public static final long P3_TAKEOVER_REBEL = 1L << 31;
    public static final long P3_TAKEOVER_IMPERIUM = 1l << 32;
    public static final long P3_TAKEOVER_MILITARY = 1l << 33;
    public static final long P3_TAKEOVER_PRESTIGE = 1l << 34;
    public static final long P3_DESTROY = 1l << 35;
    public static final long P3_TAKEOVER_DEFENSE = 1l << 36;
    public static final long P3_PREVENT_TAKEOVER = 1l << 37;
    public static final long P3_UPGRADE_WORLD = 1l << 38;

    /* Mask of takeover powers */
    public static final long P3_TAKEOVER_MASK = P3_TAKEOVER_REBEL | P3_TAKEOVER_IMPERIUM |
            P3_TAKEOVER_MILITARY;//| P3_PRESTIGE_TAKEOVER;

    /* Phase four -- Consume */
    public static final long P4_TRADE_ANY = 1;
    public static final long P4_TRADE_NOVELTY = 1L << 1;
    public static final long P4_TRADE_RARE = 1L << 2;
    public static final long P4_TRADE_GENE = 1L << 3;
    public static final long P4_TRADE_ALIEN = 1L << 4;
    public static final long P4_TRADE_THIS = 1L << 5;
    public static final long P4_TRADE_BONUS_CHROMO = 1L << 6;
    public static final long P4_NO_TRADE = 1L << 7;
    public static final long P4_TRADE_ACTION = 1L << 8;
    public static final long P4_TRADE_NO_BONUS = 1L << 9;
    public static final long P4_CONSUME_ANY = 1L << 10;
    public static final long P4_CONSUME_NOVELTY = 1L << 11;
    public static final long P4_CONSUME_RARE = 1L << 12;
    public static final long P4_CONSUME_GENE = 1L << 13;
    public static final long P4_CONSUME_ALIEN = 1L << 14;
    public static final long P4_CONSUME_THIS = 1L << 15;
    public static final long P4_CONSUME_TWO = 1L << 16;
    public static final long P4_CONSUME_3_DIFF = 1L << 17;
    public static final long P4_CONSUME_N_DIFF = 1L << 18;
    public static final long P4_CONSUME_ALL = 1L << 19;
    public static final long P4_CONSUME_PRESTIGE = 1L << 20;
    public static final long P4_GET_CARD = 1L << 21;
    public static final long P4_GET_2_CARD = 1L << 22;
    public static final long P4_GET_VP = 1L << 23;
    public static final long P4_GET_PRESTIGE = 1L << 24;
    public static final long P4_DRAW = 1L << 25;
    public static final long P4_DRAW_LUCKY = 1L << 26;
    public static final long P4_DISCARD_HAND = 1L << 27;
    public static final long P4_ANTE_CARD = 1L << 28;
    public static final long P4_VP = 1L << 29;
    public static final long P4_RECYCLE = 1L << 30;

    /* Mask of trade powers */
    public static final long P4_TRADE_MASK = P4_TRADE_ANY | P4_TRADE_NOVELTY | P4_TRADE_RARE |
            P4_TRADE_GENE | P4_TRADE_ALIEN | P4_TRADE_THIS |
            P4_TRADE_BONUS_CHROMO | P4_NO_TRADE;


    /* Phase five -- Produce */
    public static final long P5_PRODUCE = 1;
    public static final long P5_WINDFALL_ANY = 1L << 1;
    public static final long P5_WINDFALL_NOVELTY = 1L << 2;
    public static final long P5_WINDFALL_RARE = 1L << 3;
    public static final long P5_WINDFALL_GENE = 1L << 4;
    public static final long P5_WINDFALL_ALIEN = 1L << 5;
    public static final long P5_NOT_THIS = 1L << 6;
    public static final long P5_DISCARD = 1L << 7;
    public static final long P5_DRAW = 1L << 8;
    public static final long P5_DRAW_IF = 1L << 9;
    public static final long P5_PRESTIGE_IF = 1L << 10;
    public static final long P5_DRAW_EACH_NOVELTY = 1L << 11;
    public static final long P5_DRAW_EACH_RARE = 1L << 12;
    public static final long P5_DRAW_EACH_GENE = 1L << 13;
    public static final long P5_DRAW_EACH_ALIEN = 1L << 14;
    public static final long P5_DRAW_WORLD_GENE = 1L << 15;
    public static final long P5_DRAW_MOST_RARE = 1L << 16;
    public static final long P5_DRAW_MOST_PRODUCED = 1L << 17;
    public static final long P5_DRAW_DIFFERENT = 1L << 18;
    public static final long P5_PRESTIGE_MOST_CHROMO = 1L << 19;
    public static final long P5_DRAW_MILITARY = 1L << 20;
    public static final long P5_DRAW_REBEL = 1L << 21;
    public static final long P5_DRAW_CHROMO = 1L << 22;
    public static final long P5_TAKE_SAVED = 1L << 23;


    /*
    * Special victory point flags.
    */
    public static final int VP_NOVELTY_PRODUCTION = 0;
    public static final int VP_RARE_PRODUCTION = 1;
    public static final int VP_GENE_PRODUCTION = 2;
    public static final int VP_ALIEN_PRODUCTION = 3;

    public static final int VP_NOVELTY_WINDFALL = 4;
    public static final int VP_RARE_WINDFALL = 5;
    public static final int VP_GENE_WINDFALL = 6;
    public static final int VP_ALIEN_WINDFALL = 7;

    public static final int VP_DEVEL_EXPLORE = 8;
    public static final int VP_WORLD_EXPLORE = 9;
    public static final int VP_DEVEL_TRADE = 10;
    public static final int VP_WORLD_TRADE = 11;
    public static final int VP_DEVEL_CONSUME = 12;
    public static final int VP_WORLD_CONSUME = 13;

    public static final int VP_SIX_DEVEL = 14;
    public static final int VP_DEVEL = 15;
    public static final int VP_WORLD = 16;

    public static final int VP_REBEL_FLAG = 17;
    public static final int VP_ALIEN_FLAG = 18;
    public static final int VP_TERRAFORMING_FLAG = 19;
    public static final int VP_UPLIFT_FLAG = 20;
    public static final int VP_IMPERIUM_FLAG = 21;
    public static final int VP_CHROMO_FLAG = 22;

    public static final int VP_MILITARY = 23;
    public static final int VP_TOTAL_MILITARY = 24;
    public static final int VP_NEGATIVE_MILITARY = 25;

    public static final int VP_REBEL_MILITARY = 26;

    public static final int VP_THREE_VP = 27;
    public static final int VP_KIND_GOOD = 28;
    public static final int VP_PRESTIGE = 29;

    public static final int VP_NAME = 30;


    /*
    * The intermediate goal tiles.
    */
    public static final int GOAL_FIRST_5_VP = 0;
    public static final int GOAL_FIRST_4_TYPES = 1;
    public static final int GOAL_FIRST_3_ALIEN = 2;
    public static final int GOAL_FIRST_DISCARD = 3;
    public static final int GOAL_FIRST_PHASE_POWER = 4;
    public static final int GOAL_FIRST_SIX_DEVEL = 5;

    public static final int GOAL_FIRST_3_UPLIFT = 6;
    public static final int GOAL_FIRST_4_GOODS = 7;
    public static final int GOAL_FIRST_8_ACTIVE = 8;

    public static final int GOAL_FIRST_NEG_MILITARY = 9;
    public static final int GOAL_FIRST_2_PRESTIGE = 10;
    public static final int GOAL_FIRST_4_MILITARY = 11;

    public static final int GOAL_MOST_MILITARY = 12;
    public static final int GOAL_MOST_BLUE_BROWN = 13;
    public static final int GOAL_MOST_DEVEL = 14;
    public static final int GOAL_MOST_PRODUCTION = 15;

    public static final int GOAL_MOST_EXPLORE = 16;
    public static final int GOAL_MOST_REBEL = 17;

    public static final int GOAL_MOST_PRESTIGE = 18;
    public static final int GOAL_MOST_CONSUME = 19;

    /*
    * Choice types we can send to players.
    */
    public static final int CHOICE_ACTION = 0;
    public static final int CHOICE_START = 1;
    public static final int CHOICE_DISCARD = 2;
    public static final int CHOICE_SAVE = 3;
    public static final int CHOICE_DISCARD_PRESTIGE = 4;
    public static final int CHOICE_PLACE = 5;
    public static final int CHOICE_PAYMENT = 6;
    public static final int CHOICE_SETTLE = 7;
    public static final int CHOICE_TAKEOVER = 8;
    public static final int CHOICE_DEFEND = 9;
    public static final int CHOICE_TAKEOVER_PREVENT = 10;
    public static final int CHOICE_UPGRADE = 11;
    public static final int CHOICE_TRADE = 12;
    public static final int CHOICE_CONSUME = 13;
    public static final int CHOICE_CONSUME_HAND = 14;
    public static final int CHOICE_GOOD = 15;
    public static final int CHOICE_LUCKY = 16;
    public static final int CHOICE_ANTE = 17;
    public static final int CHOICE_KEEP = 18;
    public static final int CHOICE_WINDFALL = 19;
    public static final int CHOICE_PRODUCE = 20;
    public static final int CHOICE_DISCARD_PRODUCE = 21;
    public static final int CHOICE_SEARCH_TYPE = 22;
    public static final int CHOICE_SEARCH_KEEP = 23;
    public static final int CHOICE_OORT_KIND = 24;

}
