package it.polimi.ingsw.server.model.cards.deck;

import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.storage.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeckTest {

    private int randomIntBetween(Random r, int min, int max) {
        return r.nextInt(max - min) + min;
    }

    private <T extends Enum<T>> T randomEnumEntry(Random r, Class<T> myEnum, int[] exclude) {
        int index;
        boolean flag;
        do{
            index = randomIntBetween(r, 0 ,myEnum.getEnumConstants().length - 1);
            final int temp = index;
            flag = exclude != null && IntStream.of(exclude).anyMatch(n -> n == temp);
        }while(flag);
        return myEnum.getEnumConstants()[index];
    }

    private List<DevelopmentCard> createRandomDevelopmentCards(int n) {
        Random rand = new Random();
        List<DevelopmentCard> l =  new ArrayList<>();
        for(int i = 0; i < n; i++) {
            l.add(new DevelopmentCard(0,
                    i + 1,  // Cards generated in the same batch are always different
                    randomEnumEntry(rand, CardColors.class, null),
                    randomIntBetween(rand, 1, 3),
                    new Production(
                            new ArrayList<>(){{
                                add(new Resource(
                                        randomEnumEntry(rand, ResourceType.class, new int[]{4}),
                                        randomIntBetween(rand, 1, 4)));
                            }},
                            new ArrayList<>(){{
                                add(new Resource(
                                        randomEnumEntry(rand, ResourceType.class, null),
                                        randomIntBetween(rand, 1, 4)));
                            }}
                    ),
                    new ArrayList<>(){{
                        add(new Resource(
                                randomEnumEntry(rand, ResourceType.class, new int[]{4}),
                                randomIntBetween(rand, 1, 4)));
                    }}));
        }
        return l;
    }

    //TODO: Test solo con DevelopmentCard
    @Test
    public void testIsEmpty() {
        Deck d0 = new Deck();
        Deck d1 = new Deck(createRandomDevelopmentCards(1));
        Deck d2 = new Deck(createRandomDevelopmentCards(2));

        assertTrue(d0.isEmpty());
        assertFalse(d1.isEmpty());
        assertFalse(d2.isEmpty());
    }

    @Test
    public void testGet() {
        List<DevelopmentCard> dCards = createRandomDevelopmentCards(10);
        Deck deck = new Deck(dCards);

        for(int i = 0; i < dCards.size(); i++) {
            DevelopmentCard card1 = dCards.get(i);
            DevelopmentCard card2 = (DevelopmentCard) deck.get(i);

            assertEquals(card1, card2);
        }
    }

    @Test
    public void testIndexOf() {
        List<DevelopmentCard> dCards = createRandomDevelopmentCards(10);
        Deck deck = new Deck(dCards);

        for(int i = 0; i < dCards.size(); i++)
            assertEquals(deck.get(deck.indexOf(deck.get(i))), deck.get(i));
    }

    @Test
    public void testGetCards() {
        List<DevelopmentCard> dCards = createRandomDevelopmentCards(10);
        Deck deck = new Deck(dCards);
        List<Card> cards = deck.getCards();

        for(int i = 0; i < dCards.size(); i++)
            assertEquals(dCards.get(i), cards.get(i));
    }

    @Test
    public void testExtract() {
        List<DevelopmentCard> dCards = createRandomDevelopmentCards(4);
        Deck deck = new Deck(dCards);
        List<Card> extracted;

        extracted = deck.extract(new int[]{0, 1});
        assertEquals(deck.size(), 2);
        assertEquals(dCards.get(0), extracted.get(0));
        assertEquals(dCards.get(1), extracted.get(1));

        extracted = deck.extract(new int[]{0});
        assertEquals(deck.size(), 1);
        assertEquals(dCards.get(2), extracted.get(0));

        extracted = deck.extract(new int[]{0});
        assertTrue(deck.isEmpty());
        assertEquals(dCards.get(3), extracted.get(0));
    }

    @Test
    public void testAddOnTop() {
        List<DevelopmentCard> cards = createRandomDevelopmentCards(2);
        List<DevelopmentCard> dCards = List.of(cards.get(0));
        Deck deck1 = new Deck(dCards);
        Deck deck2 = new Deck();

        DevelopmentCard added = cards.get(1);
        deck1.addOnTop(added);
        deck2.addOnTop(added);
        DevelopmentCard fromDeck1 = (DevelopmentCard) deck1.get(0);
        DevelopmentCard fromDeck2 = (DevelopmentCard) deck2.get(0);

        assertEquals(deck1.size(), 2);
        assertEquals(deck2.size(), 1);
        assertEquals(added, fromDeck1);
        assertEquals(added, fromDeck2);
    }

    @Test
    public void testAdd() {
        List<DevelopmentCard> cards = createRandomDevelopmentCards(2);
        List<DevelopmentCard> dCards = List.of(cards.get(0));
        Deck deck1 = new Deck(dCards);
        Deck deck2 = new Deck();

        DevelopmentCard added = cards.get(1);
        deck1.add(added);
        deck2.add(added);
        DevelopmentCard fromDeck1 = (DevelopmentCard) deck1.get(1);
        DevelopmentCard fromDeck2 = (DevelopmentCard) deck2.get(0);

        assertEquals(deck1.size(), 2);
        assertEquals(deck2.size(), 1);
        assertEquals(added, fromDeck1);
        assertEquals(added, fromDeck2);
    }

    @Test
    public void testSize() {
        Deck deck0 = new Deck();
        Deck deck1 = new Deck(createRandomDevelopmentCards(10));

        assertEquals(deck0.size(), 0);
        assertEquals(deck1.size(), 10);
    }

    @Test
    public void testShuffle() {
        //TODO: Random test?
    }

    @Test
    public void testIterator() {
        int i = 0;
        Deck deck = new Deck(createRandomDevelopmentCards(10));
        for(Card card : deck){
            assertEquals(deck.get(i++), card);
        }
    }
}