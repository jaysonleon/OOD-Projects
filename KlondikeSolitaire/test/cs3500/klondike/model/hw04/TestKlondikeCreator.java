package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Test;

import cs3500.klondike.model.hw02.BasicKlondike;


/**
 * Tests for methods in gameType enum and KlondikeCreator class.
 */
public class TestKlondikeCreator {

  @Test
  public void testToGameType() {
    Assert.assertEquals(GameType.BASIC, GameType.toGameType("basic"));
    Assert.assertEquals(GameType.LIMITED, GameType.toGameType("limited"));
    Assert.assertEquals(GameType.WHITEHEAD, GameType.toGameType("whitehead"));
    Assert.assertThrows(IllegalArgumentException.class, () -> GameType.toGameType("hello"));
  }

  @Test
  public void testCreate() {
    Assert.assertEquals(BasicKlondike.class, KlondikeCreator.create(
            GameType.toGameType("basic")).getClass());
    Assert.assertEquals(LimitedDrawKlondike.class, KlondikeCreator.create(
            GameType.toGameType("limited")).getClass());
    Assert.assertEquals(WhiteheadKlondike.class, KlondikeCreator.create(
            GameType.toGameType("whitehead")).getClass());
    Assert.assertThrows(IllegalArgumentException.class, () ->
            KlondikeCreator.create(GameType.toGameType("hello")));
  }
}
