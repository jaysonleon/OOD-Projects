package cs3500.klondike.model.hw04.hw03;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Test class for the controller.
 */
public class TestController {
  KlondikeController c;
  BasicKlondike m;
  Mock mock;
  StringBuilder log;

  @Before
  public void setUp() {
    c = new KlondikeTextualController(new InputStreamReader(System.in), System.out);
    m = new BasicKlondike();
    log = new StringBuilder();
    mock = new Mock(log);
  }

  /**
   * Build a deck of cards with the given cards.
   * @param cardsWanted the list of the strings of the cards wanted in the deck
   * @return a list of cards with only the wanted cards
   */
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
  public void testPlayGameThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            c.playGame(null, m.getDeck(), true, 7, 3));
  }

  @Test
  public void testPlayGame() {
    List<Card> deck = build(List.of("2♠", "A♣", "A♢", "A♠", "2♣", "2♢",  "A♡", "2♡"));
    StringReader reader = new StringReader("md 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);

    con.playGame(mock, deck, false, 3, 1);
    Assert.assertTrue(log.toString().contains("moveDraw (0)"));
  }

  @Test
  public void testMoveDrawToFound() {
    List<Card> deck = build(List.of("2♣", "A♣", "2♢", "A♢"));
    StringReader reader = new StringReader("mdf 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveDrawToFound (0)"));
  }

  @Test
  public void testMovePileToFound() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpf 2 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveToFound (1, 0)"));
  }

  @Test
  public void testMovePile() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 2 1 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("movePile (1, 1, 0)"));
  }

  @Test
  public void testMoveDraw() {
    List<Card> deck = build(List.of("2♣", "A♣", "2♢", "A♢"));
    StringReader reader = new StringReader("md 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveDraw (0)"));
  }

  @Test
  public void testMovePileInvalidMove() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 2 1 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("movePile (1, 1, 0)"));
  }

  @Test
  public void testMoveDrawInvalidMove() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("md 2 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveDraw (1)"));
  }

  @Test
  public void testMovePileToFoundInvalidMove() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpf 1 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveToFound (0, 0)"));
  }

  @Test
  public void testMoveDrawToFoundInvalidMove() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mdf 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveDrawToFound (0)"));
  }

  @Test
  public void testGameToCompletion() {
    List<Card> deck = build(List.of("2♣", "A♣", "2♢", "A♢"));
    StringReader reader = new StringReader("mdf 1 mpf 2 1 mpf 2 2 mpf 1 2 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveDrawToFound (0)"));
    Assert.assertTrue(log.toString().contains("moveToFound (1, 0)"));
    Assert.assertTrue(log.toString().contains("moveToFound (1, 1)"));
    Assert.assertTrue(log.toString().contains("moveToFound (0, 1)"));
  }

  @Test
  public void testGameToCompletionMessage() {
    List<Card> deck = build(List.of("A♢", "2♢", "A♣", "2♣"));
    StringReader reader = new StringReader("mpf 1 1 mpf 2 2 mpf 2 1 mdf 2");
    KlondikeModel m = new BasicKlondike();
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("You win!"));
  }

  @Test
  public void testGameToEndNoWinMock() {
    List<Card> deck = build(List.of("A♣", "2♣", "3♣", "4♣", "5♣", "A♢", "2♢", "3♢", "4♢", "5♢"));
    StringReader reader = new StringReader("mpf 1 1 mpp 4 1 1 mpp 4 1 2 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, deck, false, 4, 1);
    Assert.assertTrue(log.toString().contains("moveToFound (0, 0)"));
    Assert.assertTrue(log.toString().contains("movePile (3, 1, 0)"));
    Assert.assertTrue(log.toString().contains("movePile (3, 1, 1)"));
  }

  @Test
  public void testGameToEndNoWinMessage() {
    List<Card> deck = build(List.of("A♣", "2♣", "3♣", "4♣", "5♣", "A♢", "2♢", "3♢", "4♢", "5♢"));
    StringReader reader = new StringReader("mpf 1 1 mpp 4 1 1 mpp 4 1 2 q");
    StringBuilder log = new StringBuilder();
    KlondikeModel m = new BasicKlondike();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(m, deck, false, 4, 1);
    Assert.assertTrue(log.toString().contains("Game over. Score: 1"));
  }

  @Test
  public void testIsGameOver() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpf 2 1 mdf 1 q");
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    KlondikeController con = new KlondikeTextualController(reader, gameLog);
    con.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("moveToFound (1, 0)"));
  }

  // need to test:
  //  inputs with 0's
  // test Invalid inputs for movePile
  @Test
  public void testMovePileWithZerosArg1() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 0 1 1 q");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMovePileWithZerosArg2() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 1 0 1 q");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMovePileWithZerosArg3() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 1 1 0 q");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMovePileWithNegArg1() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp -1 1 2 q");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMovePileWithNegArg2() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 1 -1 2 q");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMovePileWithNegArg3() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringReader reader = new StringReader("mpp 1 1 -1 q");
    StringBuilder gameLog = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  // test Invalid inputs for movePileToFoundation
  @Test
  public void testMPFoundWithZerosArg1() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mpf 0 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMPFoundWithZerosArg2() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mpf 1 0 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMPFoundWithNegArg1() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mpf -1 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMPFoundWithNegArg2() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mpf 1 -1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  // test Invalid inputs for moveDraw
  @Test
  public void testMoveDrawWithZeroArg() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("md 0 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMoveDrawWithNegArg() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("md -1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  // test Invalid inputs for moveDrawToFoundation
  @Test
  public void testMDFoundWithZeroArg() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mdf 0 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testMDFoundWithNegArg() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢"));
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mdf -1 Q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(m, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Invalid move. Play again."));
  }

  // test discardDraw
  @Test
  public void testDiscardDraw() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢", "3♢"));
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("dd q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("DD"));
  }

  @Test
  public void testNotEnoughInputs() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢", "3♢"));
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mpp 1 1\n 3 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("movePile (0, 1, 2)"));
  }

  @Test
  public void testTooManyInputs() {
    List<Card> deck = build(List.of("2♣", "A♣", "A♢", "2♢", "3♢"));
    StringBuilder log = new StringBuilder();
    Mock mock = new Mock(log);
    StringBuilder gameLog = new StringBuilder();
    StringReader reader = new StringReader("mpp 1 1 3 mpf 2 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, deck, false, 2, 1);
    Assert.assertTrue(log.toString().contains("movePile (0, 1, 2)"));
    Assert.assertTrue(log.toString().contains("moveToFound (1, 0)"));
  }
}
