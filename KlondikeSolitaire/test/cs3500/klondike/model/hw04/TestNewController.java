package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.StringReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;

/**
 * Tests for controller with basic & new models.
 */
public class TestNewController extends Constants {

  @Before
  public void setUp() {
    m = new BasicKlondike();
  }

  @Test
  public void testInvalidConstructorThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new KlondikeTextualController(null, System.out));
    Assert.assertThrows(IllegalArgumentException.class, () ->
            new KlondikeTextualController(new InputStreamReader(System.in), null));
  }

  @Test
  public void testMPPQuitsArg1() {
    StringReader reader = new StringReader("mpp q 1 3");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPPQuitsArg2() {
    StringReader reader = new StringReader("mpp 1 q 3");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPPQuitsArg3() {
    StringReader reader = new StringReader("mpp 1 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPFQuitsArg1() {
    StringReader reader = new StringReader("mpf q 1");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMPFQuitsArg2() {
    StringReader reader = new StringReader("mpf 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMDFQuitsArg1() {
    StringReader reader = new StringReader("mdf q 1");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMDFQuitsArg2() {
    StringReader reader = new StringReader("mdf 1 q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
    Assert.assertTrue(gameLog.toString().contains("Game quit!\nState of game when quit:"));
  }

  @Test
  public void testMDQuits() {
    StringReader reader = new StringReader("md q");
    KlondikeController c = new KlondikeTextualController(reader, gameLog);
    c.playGame(mock, cards, false, 2, 1);
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
