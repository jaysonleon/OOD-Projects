package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.view.KlondikeTextualView;

/**
 * Tests for LimitedDrawKlondike.
 */
public class TestLimitedDrawMoves extends Constants {
  private List<Card> cards3;

  @Before
  public void setUp() {
    cards3 = List.of(aC, twoC, aD, twoD, aH, twoH, aS, twoS);
    game = new LimitedDrawKlondike(1);
    game2 = new LimitedDrawKlondike(2);
  }

  @Test
  public void testInvalidConstructorThrows() {
    Assert.assertThrows(IllegalArgumentException.class, () -> new LimitedDrawKlondike(-1));
  }

  @Test
  public void testGetDeck() {
    Assert.assertEquals(52, game.getDeck().size());
  }

  @Test
  public void testStartGameValid() {
    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(3, game.getNumPiles());
    Assert.assertEquals(2, game.getNumDraw());
    Assert.assertEquals(1, game.getPileHeight(0));
    Assert.assertEquals(2, game.getPileHeight(1));
    Assert.assertEquals(3, game.getPileHeight(2));
    Assert.assertEquals(2, game.getDrawCards().size());
  }

  @Test
  public void testStartGameThrows() {
    game.startGame(cards, false, 3, 2);
    Assert.assertThrows(IllegalStateException.class, () ->
            game.startGame(cards, false, 3, 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> game2.startGame(cards,
            false, 6, 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> game2.startGame(cards,
            false, 0, 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> game2.startGame(cards,
            false, 3, 0));
  }

  @Test
  public void testMovePileValid() {
    game.startGame(cards, false, 3, 2);
    game.movePile(0, 1, 2);
    Assert.assertEquals(4, game.getPileHeight(2));
    game.movePile(1, 1, 0);
    Assert.assertEquals(1, game.getPileHeight(0));
  }

  @Test
  public void testMovePileThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(0, 1, 2));
    // srcPile < 0
    game.startGame(cards, false, 3, 2);
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(3, 1, 2));
    // srcPile > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(-1, 1, 2));
    // numCards < 1
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 0, 1));
    // destPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, -1));
    // destPile > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, 3));
    // srcPile == destPile
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, 0));
    // numCards > number of visible cards
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 3, 2));
    // invalid move: same color
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(0, 1, 1));
    // invalid move: not one rank less
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(2, 1, 1));
  }

  @Test
  public void testMoveDrawValid() {
    game.startGame(cards, false, 3, 2);
    game.moveDraw(1);
    Assert.assertEquals(3, game.getPileHeight(1));
    game.movePile(0, 1, 2);
    game.moveDraw(0);
    Assert.assertEquals(1, game.getPileHeight(0));
  }

  @Test
  public void testMoveDrawThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(1));
    game.startGame(cards2, false, 3, 2);
    // destPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDraw(-1));
    // destPile > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDraw(3));
    // invalid move: same color
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(1));
    // invalid move: not one rank less, same color
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(0));
    // invalid move; not one rank less, different color
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(2));
  }

  @Test
  public void testMoveDrawThrowsAfterDiscardAll() {
    game.startGame(cards, false, 3, 2);
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(0));
  }

  @Test
  public void testMoveToFoundationValid() {
    game.startGame(cards, false, 3, 2);
    game.moveToFoundation(0, 0);
    Assert.assertEquals(1, game.getScore());
    game.movePile(1, 1, 0);
    game.moveToFoundation(1, 0);
    Assert.assertEquals(2, game.getScore());
  }

  @Test
  public void testMoveToFoundationThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 0));
    game.startGame(cards, false, 3, 2);
    // srcPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(-1, 0));
    // srcPile > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(3, 0));
    // foundationPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(0, -1));
    // foundationPile > number of foundation piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(0, 4));
    // invalid move: not an ace to empty foundation
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(1, 0));
    game.moveToFoundation(0, 0);
    // invalid move: not one rank higher, same suit
    game.discardDraw();
    game.moveDraw(0);
    game.moveDraw(1);
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(1, 0));
    // invalid move: not one rank higher, different suit
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(2, 0));
  }

  @Test
  public void testMoveDrawToFoundationValid() {
    game.startGame(cards2, false, 3, 2);
    game.moveToFoundation(0, 0);
    game.movePile(1, 1, 0);
    game.moveToFoundation(1, 0);
    game.moveDrawToFoundation(0);
    Assert.assertEquals(3, game.getScore());
  }

  @Test
  public void testMoveDrawToFoundationThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
    game.startGame(cards2, false, 3, 2);
    // foundationPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDrawToFoundation(-1));
    // foundationPile > number of foundation piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDrawToFoundation(4));
    // invalid move: not an ace to empty foundation
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
    game.moveToFoundation(0, 0);
    // invalid move: not one rank higher, same suit
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
    // invalid move: not one rank higher, different suit
    game.discardDraw();
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
  }

  @Test
  public void testMoveDrawToFoundThrowsAfterDiscardAll() {
    game.startGame(cards, false, 3, 2);
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    game.discardDraw();
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
  }

  @Test
  public void testDiscardDrawValid() {
    game.startGame(cards, false, 3, 2);
    game.discardDraw();
    game.discardDraw();
    Assert.assertEquals(2, game.getDrawCards().size());
    game.discardDraw();
    Assert.assertEquals(1, game.getDrawCards().size());
    game.discardDraw();
    Assert.assertEquals(0, game.getDrawCards().size());
  }

  @Test
  public void testGetNumDraw() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getNumDraw());
    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(2, game.getNumDraw());

    game.discardDraw();
    Assert.assertEquals(2, game.getNumDraw());
  }

  @Test
  public void testGetScore() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getScore());
    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(0, game.getScore());

    game.discardDraw();
    Assert.assertEquals(0, game.getScore());

    game.moveToFoundation(0, 0);
    Assert.assertEquals(1, game.getScore());

    game.movePile(1, 1, 0);
    game.moveToFoundation(1, 0);
    Assert.assertEquals(2, game.getScore());
  }

  @Test
  public void testGetPileHeight() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getPileHeight(0));

    game.startGame(cards, false, 3, 2);
    // pileNum < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getPileHeight(-1));
    // pileNum > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getPileHeight(3));


    Assert.assertEquals(1, game.getPileHeight(0));
    Assert.assertEquals(2, game.getPileHeight(1));
    Assert.assertEquals(3, game.getPileHeight(2));

    game.movePile(0, 1, 2);
    Assert.assertEquals(4, game.getPileHeight(2));
  }

  @Test
  public void testIsCardVisible() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.isCardVisible(0, 0));

    game.startGame(cards, false, 3, 2);
    // pileNum < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.isCardVisible(-1, 0));
    // pileNum > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.isCardVisible(3, 0));
    // card < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.isCardVisible(0, -1));
    // card > number of cards in pile
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.isCardVisible(0, 1));


    Assert.assertTrue(game.isCardVisible(0, 0));
    Assert.assertTrue(game.isCardVisible(1, 1));
    Assert.assertTrue(game.isCardVisible(2, 2));
    Assert.assertFalse(game.isCardVisible(1, 0));
    Assert.assertFalse(game.isCardVisible(2, 0));
    Assert.assertFalse(game.isCardVisible(2, 1));
  }

  @Test
  public void testGetCardAt2Args() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getCardAt(0, 0));

    game.startGame(cards, false, 3, 2);
    // pileNum < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getCardAt(-1, 0));
    // pileNum > number of piles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getCardAt(3, 0));
    // card < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getCardAt(0, -1));
    // card > number of cards in pile
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getCardAt(0, 1));

    Assert.assertEquals(aS, game.getCardAt(0, 0));
    Assert.assertEquals(fourS, game.getCardAt(1, 1));
    Assert.assertEquals(twoH, game.getCardAt(2, 2));
  }

  @Test
  public void testGetCardAt1Arg() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getCardAt(0));

    game.startGame(cards2, false, 3, 2);
    // card < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getCardAt(-1));

    Assert.assertNull(game.getCardAt(1));

    game.moveToFoundation(0, 0);
    Assert.assertEquals(aS, game.getCardAt(0));
    game.movePile(1, 1, 0);
    game.moveToFoundation(1, 0);
    Assert.assertEquals(twoS, game.getCardAt(0));
    game.moveDrawToFoundation(0);
    Assert.assertEquals(threeS, game.getCardAt(0));
    game.moveToFoundation(0, 0);
    Assert.assertEquals(fourS, game.getCardAt(0));
  }

  @Test
  public void testGetNumFoundations() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getNumFoundations());
    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(2, game.getNumFoundations());
    game2.startGame(cards3, false, 3, 2);
    Assert.assertEquals(4, game2.getNumFoundations());
  }

  @Test
  public void testIsGameOver() {
    List<Card> deck = List.of(aS, twoH, aH, twoS);
    Assert.assertThrows(IllegalStateException.class, () ->
            game.isGameOver());
    game.startGame(deck, false, 2, 1);
    game.moveToFoundation(0, 0);
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testGameToCompletion() {
    game.startGame(cards3, false, 3, 2);
    // AC -> F0
    game.moveToFoundation(0, 0);
    Assert.assertFalse(game.isGameOver());
    // 2H -> empty C1
    game.movePile(2, 1, 0);
    // AH -> F1
    game.moveToFoundation(2, 1);
    // AD -> F2
    game.moveToFoundation(2, 2);
    // AS -> F3
    game.moveDrawToFoundation(3);
    // 2S -> F3: AS
    game.moveDrawToFoundation(3);
    // 2D -> F2: AD
    game.moveToFoundation(1, 2);
    // 2C -> F0: AC
    game.moveToFoundation(1, 0);
    // 2H -> F1: AH
    game.moveToFoundation(0, 1);
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testShuffle() {
    KlondikeTextualView v1 = new KlondikeTextualView(game);
    KlondikeTextualView v2 = new KlondikeTextualView(game2);
    game.startGame(cards, true, 3, 2);
    game2.startGame(cards, false, 3, 2);
    Assert.assertNotEquals(v1.toString(), v2.toString());
  }
}
