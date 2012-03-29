package rftg.game.cards;

import rftg.game.Card;
import rftg.game.Constants;

import java.util.*;

public class CardPile {

    protected List<Card> pile;

    public CardPile() {
        pile = new ArrayList<Card>(Constants.MAX_DECK);
    }

    public synchronized Iterator<Card> iterator() {
        return pile.iterator();
    }

    public synchronized void shuffle() {
        Collections.shuffle(pile);
    }

    public synchronized void shuffle(long randomSeed) {
        Collections.shuffle(pile, new Random(randomSeed));
    }

    /**
     * Returns a view of the top design in pile.
     *
     * @return The top design in pile or null if the pile if empty.
     */
    public synchronized Card top() {
        return pile.isEmpty() ? null : pile.get(0);
    }

    /**
     * Returns a list of the top cards in the design pile.
     *
     * @param count
     * @return Top cards in design pile. If count > size, the resulting list
     *         will be the remaining cards in pile.
     */
    public synchronized List<Card> top(int count) {
        return count > pile.size() ? pile.subList(0, pile.size()) : pile
                .subList(0, count);
    }

    /**
     * Removes the top design in pile.
     *
     * @return The top design in pile or null if the pile is empty.
     */
    public synchronized Card pop() {
        return pile.isEmpty() ? null : pile.remove(0);
    }

    public synchronized List<Card> pop(int count) {
        int to = count > pile.size() ? pile.size() : count;
        List<Card> poped = new ArrayList<Card>(pile.subList(0, to));
        pile.subList(0, to).clear();
        return poped;
    }

    /**
     * Append designs at the end.
     *
     * @param more
     */
    public synchronized void appendDesigns(Collection<Card> more) {
        pile.addAll(more);
    }

    public void add(Card card) {
        this.pile.add(card);
    }

    public int size() {
        return this.pile.size();
    }

    public Card[] getCards() {
        return pile.toArray(new Card[pile.size()]);
    }
}
