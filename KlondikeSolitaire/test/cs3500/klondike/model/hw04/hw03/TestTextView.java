package cs3500.klondike.model.hw04.hw03;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextView;

/**
 * Tests for toString from TextView interface.
 */
public class TestTextView {
  private TextView view;
  private KlondikeModel game;
  private List<Card> cards;

  @Before
  public void setUp() {
    game = new BasicKlondike();
    cards = game.getDeck();
    view = new KlondikeTextualView(game);
  }

  private static List<Card> build(List<String> cardsWanted) {
    List<Card> d = new BasicKlondike().getDeck();
    List<Card> deck = new ArrayList<>();
    for (String card : cardsWanted) {
      for (Card c : d) {
        if (c.toString().equals(card)) {
          deck.add(c);
        }
      }
    }

    return deck;
  }

  @Test
  public void testToString() {
    game.startGame(cards, false, 7, 3);
    Assert.assertEquals("Draw: 8♣, 8♢, 8♡\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♣  ?  ?  ?  ?  ?  ?\n"
            + "    2♠  ?  ?  ?  ?  ?\n"
            + "       4♢  ?  ?  ?  ?\n"
            + "          5♡  ?  ?  ?\n"
            + "             6♡  ?  ?\n"
            + "                7♢  ?\n"
            + "                   7♠", view.toString());

    game.moveToFoundation(0, 0);
    Assert.assertEquals("Draw: 8♣, 8♢, 8♡\n"
            + "Foundation: A♣, <none>, <none>, <none>\n"
            + "  X  ?  ?  ?  ?  ?  ?\n"
            + "    2♠  ?  ?  ?  ?  ?\n"
            + "       4♢  ?  ?  ?  ?\n"
            + "          5♡  ?  ?  ?\n"
            + "             6♡  ?  ?\n"
            + "                7♢  ?\n"
            + "                   7♠", view.toString());

    game.discardDraw();
    Assert.assertEquals("Draw: 8♢, 8♡, 8♠\n"
            + "Foundation: A♣, <none>, <none>, <none>\n"
            + "  X  ?  ?  ?  ?  ?  ?\n"
            + "    2♠  ?  ?  ?  ?  ?\n"
            + "       4♢  ?  ?  ?  ?\n"
            + "          5♡  ?  ?  ?\n"
            + "             6♡  ?  ?\n"
            + "                7♢  ?\n"
            + "                   7♠", view.toString());
  }

  @Test
  public void testToString2() {
    game.startGame(cards, false, 3, 2);
    game.movePile(0, 1, 2);
    game.moveToFoundation(2,0);
    game.moveToFoundation(1,1);
    game.moveToFoundation(1,2);
    game.moveToFoundation(2,2);
    Assert.assertEquals("Draw: 2♡, 2♠\n"
            + "Foundation: A♣, A♠, 2♢, <none>\n"
            + "  X  X  ?\n"
            + "       2♣", view.toString());
  }


  @Test
  public void testSecConstructor() {
    BasicKlondike m = new BasicKlondike();
    TextView v = new KlondikeTextualView(m, new StringBuilder());
    m.startGame(m.getDeck(), false, 6, 3);
    Assert.assertEquals("Draw: 6♢, 6♡, 6♠\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♣  ?  ?  ?  ?  ?\n"
            + "    2♡  ?  ?  ?  ?\n"
            + "       3♠  ?  ?  ?\n"
            + "          4♠  ?  ?\n"
            + "             5♡  ?\n"
            + "                6♣", v.toString());

  }

  @Test
  public void test2() {
    List<Card> deck = build(List.of("A♠", "A♢", "A♡", "A♣", "2♠", "2♢", "2♡", "2♣"));
    BasicKlondike m = new BasicKlondike();
    TextView v = new KlondikeTextualView(m, new StringBuilder());
    m.startGame(deck, false, 3, 1);
    Assert.assertEquals("Draw: 2♡\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♠  ?  ?\n"
            + "    A♣  ?\n"
            + "       2♢", v.toString());
  }
}
