package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;

/**
 * Tests for WhiteheadKlondike with the controller.
 */
public class TestWhiteheadController extends Constants {

  @Before
  public void setUp() {
    game = new WhiteheadKlondike();
    m = new WhiteheadKlondike();

  }

  @Test
  public void testWhiteheadGameToCompletion() {
    deck = List.of(aS, twoS, aC, twoC);
    StringReader reader = new StringReader("mpf 2 1 mpf 1 2 mpf 2 2 mdf 1");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, deck, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("You win!"));
  }

  @Test 
  public void testWhiteheadGameToCompletionNoWin() {
    deck = List.of(twoS, aS, threeS, fourS, fiveS, aH, twoH, threeH, fourH, fiveH);
    StringReader reader = new StringReader("mpp 1 1 2 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(game, deck, false, 4, 1);
    Assert.assertTrue(gameLog.toString().contains("Game over. Score: 0"));
  }

  @Test
  public void testWhiteheadMPPWithInvalidArgs() {
    StringReader reader = new StringReader("mpp 1 1 1 mpp 0 1 2 mpp 1 0 2 mpp 2 1 0 mpp 1 1 3 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("movePile (0, 1, 2)"));
  }

  @Test
  public void testWhiteheadMPFWithInvalidArgs() {
    StringReader reader = new StringReader("mpf 0 0 mpf 0 1 mpf 1 0 mpf 2 1 mpf 1 3 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("moveToFound (0, 2)"));
  }

  @Test
  public void testWhiteheadMDWithInvalidArgs() {
    StringReader reader = new StringReader("md 0 md -1 md 2 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 3, 1);
    Assert.assertTrue(log.toString().contains("moveDraw (1)"));
  }

  @Test
  public void testWhiteheadMDFWithInvalidArgs() {
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
