package rftg.game;

import rftg.game.cards.Design;

public class Card {
    /* Card's owner (if any) */
    int owner;

    /* Card's location */
    int where;

    /* Card's owner at start of phase */
    int start_owner;

    /* Card's location at start of phase */
    int start_where;

    /* Card is being placed and is not yet paid for */
    boolean unpaid;

    /* Bitmask of players who know card's location */
    int known;

    /* Card powers which have been used */
    boolean[] used;

    /* Card has produced this phase */
    boolean produced;

    /* Card design */
    Design design;

    /* Card covering us (as a good) */
    int covered;

    /* Order played on the table */
    int order;

    /* Next card index if belonging to player */
    int next;

    /* Next card index as of start of phase */
    int start_next;

}
