package cs3500.klondike.model.hw04.hw03;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

/**
 * Tests for Controller portion of Examplar.
 */
public class ExamplarControllerTests {

  /**
   * Build a deck of cards with the given cards.
   * @param cardsWanted the list of the strings of the cards wanted in the deck
   * @return a list of cards with only the wanted cards
   */
  public static List<Card> build(List<String> cardsWanted) {
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
  public void testBogusInputs1() {
    StringReader reader = new StringReader("#^$ 5 1 7\n q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(m, m.getDeck(), false, 7, 1);

    String[] lines = log.toString().split("\n");
    Assert.assertTrue(Arrays.toString(lines).contains("Game quit!"));
  }

  @Test
  public void testInvalidIndex1() {
    StringReader reader = new StringReader("mpp 0 1 7\n q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(m, m.getDeck(), false, 7, 1);

    String[] lines = log.toString().split("\n");
    Assert.assertTrue(Arrays.toString(lines).contains("Game quit!")
            && Arrays.toString(lines).contains("Invalid move. Play again."));
  }

  @Test
  public void testValidMove() {
    StringReader reader = new StringReader("mpp 0 1 1\n q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(m, m.getDeck(), false, 6, 3);
    String[] lines = log.toString().split("\n");

    Assert.assertTrue(Arrays.toString(lines).contains("Invalid move. Play again."));
  }

  @Test
  public void testMoveDrawInvalidIndex() {
    StringReader reader = new StringReader("md 8 q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(m, m.getDeck(), false, 6, 3);
    String[] lines = log.toString().split("\n");

    Assert.assertTrue(Arrays.toString(lines).contains("Invalid move. Play again."));
  }

  @Test
  public void testQuitFull() {
    StringReader reader = new StringReader("mpp 5 1 6\n q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    KlondikeTextualView v = new KlondikeTextualView(m);
    c.playGame(m, m.getDeck(), false, 6, 3);

    Assert.assertTrue(log.toString().contains("Game quit!\nState of game when quit:\n"
            + v + "\nScore: 0"));
  }

  @Test
  public void testMoveDrawValid() {
    List<Card> deck = build(List.of( "2♠", "A♣", "A♢", "A♠", "2♣", "2♢",  "A♡", "2♡"));

    StringReader reader = new StringReader("md 1 q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(m, deck, false, 3, 1);

    Assert.assertTrue(log.toString().contains(m.getCardAt(0, 1).toString()));
  }
}
