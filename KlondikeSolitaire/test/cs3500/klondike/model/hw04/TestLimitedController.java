package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.Card;

import static cs3500.klondike.model.hw04.hw03.ExamplarModelTests.build;

/**
 * Tests for LimitedDrawKlondike with the controller.
 */
public class TestLimitedController extends Constants {

  @Before
  public void setUp() {
    game = new LimitedDrawKlondike(2);
    m = new LimitedDrawKlondike(2);
    cards = List.of(aS, twoS, threeH, fourS, twoH, aH, threeS, fourH);
  }

  @Test
  public void testLimitedGameToCompletion() {
    StringReader reader = new StringReader("mpf 3 1 mpf 3 1 mpf 3 1 mpp 2 1 3 mpf 1 2 mpf 2 2 "
            + "mdf 2 mpf 3 2 mdf 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 3, 1);
    Assert.assertTrue(gameLog.toString().contains("You win!"));
  }

  @Test
  public void testLimitedGameToEndNoWin() {
    List<Card> deck = build(List.of("A♣", "2♣", "3♣", "4♣", "5♣", "A♢", "2♢", "3♢", "4♢", "5♢"));
    StringReader reader = new StringReader("mpf 1 1 mpp 4 1 1 mpp 4 1 2 q");
    StringBuilder log = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(reader, log);
    c.playGame(game, deck, false, 4, 1);
    Assert.assertTrue(log.toString().contains("Game over. Score: 1"));
  }

  @Test
  public void testLimitedMPPWithInvalidArgs() {
    StringReader reader = new StringReader("mpp 1 1 1 mpp 0 1 2 mpp 1 0 2 mpp 2 1 0 mpp 1 1 3 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("movePile (0, 1, 2)"));
  }

  @Test
  public void testLimitedMPFWithInvalidArgs() {
    StringReader reader = new StringReader("mpf 0 0 mpf 0 1 mpf 1 0 mpf 2 1 mpf 1 3 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("moveToFound (0, 2)"));
  }

  @Test
  public void testLimitedMDWithInvalidArgs() {
    StringReader reader = new StringReader("md 0 md -1 md 2 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("moveDraw (1)"));
  }

  @Test
  public void testLimitedMDFWithInvalidArgs() {
    StringReader reader = new StringReader("mdf 0 mdf -1 mdf 2 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("moveDrawToFound (1)"));
  }

  @Test
  public void testMPPQuitsArg1() {
    StringReader reader = new StringReader("mpp q 1 3");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPPQuitsArg2() {
    StringReader reader = new StringReader("mpp 1 q 3");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPPQuitsArg3() {
    StringReader reader = new StringReader("mpp 1 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPFQuitsArg1() {
    StringReader reader = new StringReader("mpf q 1");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPFQuitsArg2() {
    StringReader reader = new StringReader("mpf 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMDFQuitsArg1() {
    StringReader reader = new StringReader("mdf q 1");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMDFQuitsArg2() {
    StringReader reader = new StringReader("mdf 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMDQuits() {
    StringReader reader = new StringReader("md q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testFilterThroughProperly() {
    StringReader reader = new StringReader("mpp sdfas 1 @#@@ 1 \nasdad\n3\n q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("movePile (0, 1, 2)"));
  }

  @Test
  public void testIgnoreLetters() {
    StringReader reader = new StringReader("mpp asdasa 1 asdasd 1 aasdad 3\nq");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("movePile (0, 1, 2)"));
  }
}
