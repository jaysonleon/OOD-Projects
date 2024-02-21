package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.view.KlondikeTextualView;

/**
 * Tests for WhiteheadKlondike.
 */
public class TestWhiteheadMoves extends Constants {

  List<Card> cards3;

  @Before
  public void setUp() {
    cards = List.of(twoS, aS, twoH, aH, twoC, aC, twoD, aD);
    cards3 = List.of(aS, twoS, aC, twoC, aH, twoH, aD, twoD);
    game = new WhiteheadKlondike();
    game2 = new WhiteheadKlondike();
  }

  @Test
  public void testGetDeck() {
    Assert.assertEquals(52, game.getDeck().size());
  }

  @Test
  public void testStartGameValid() {
    List<Card> deck = List.of(aC, twoC, aH, twoH);
    game.startGame(deck, false, 2, 2);
    Assert.assertEquals(aC, game.getCardAt(0, 0));
    Assert.assertEquals(twoC, game.getCardAt(1, 0));
    Assert.assertEquals(aH, game.getCardAt(1, 1));
    Assert.assertEquals(List.of(twoH), game.getDrawCards());
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

  // "A♣", "2♢", "2♡", "2♣", "A♠", "A♢", "A♡", "2♠"
  @Test
  public void testMovePileValid() {
    game.startGame(cards, false, 3, 2);
    game.moveToFoundation(1, 0);
    game.movePile(1, 1, 0);
    Assert.assertEquals(2, game.getPileHeight(0));
    game.movePile(2, 2, 1);
    Assert.assertEquals(2, game.getPileHeight(1));
    game.moveToFoundation(1, 1);
    game.moveToFoundation(1, 1);
    game.movePile(0, 1, 1);
    Assert.assertEquals(1, game.getPileHeight(1));
  }

  @Test
  public void testMovePileThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(0, 1, 1));
    game.startGame(cards, false, 3, 2);
    // srcPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(-1, 1, 1));
    // srcPile >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(3, 1, 1));
    // numCards < 1
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 0, 1));
    // destPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, -1));
    // destPile >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, 3));
    // srcPile == destPile
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, 0));
    // numCards > number of visible cards
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 2, 1));
    // invalid move: different color, ranks valid
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(1, 1, 0));
    // invalid move: rank higher, same color
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(0, 1, 2));
    // invalid move: invalid build (not same suits)
    game.movePile(2, 1, 0);
    game.moveToFoundation(1, 0);
    game.moveToFoundation(1, 1);
    // 2S, AC -> invalid move to empty pile since suits are different.
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(0, 2, 1));
  }

  @Test
  public void testMoveDrawValid() {
    game.startGame(cards3, false, 3, 2);
    game.moveDraw(2);
    Assert.assertEquals(4, game.getPileHeight(2));
    game.moveToFoundation(0, 0);
    game.moveDraw(0);
    Assert.assertEquals(1, game.getPileHeight(0));
  }

  @Test
  public void testMoveDrawThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(0));
    game.startGame(cards3, false, 3, 2);
    // destPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDraw(-1));
    // destPile >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDraw(3));
    // invalid move: different color, ranks valid
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDraw(1));
    // invalid move: rank higher, same color
    game.moveDraw(2);
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(2));
    game.moveToFoundation(0, 0);
    game.moveDraw(0);
    // invalid move: draw is empty
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
  }

  @Test
  public void testMoveToFoundationValid() {
    game.startGame(cards, false, 3, 2);
    game.moveToFoundation(1, 0);
    Assert.assertEquals(1, game.getScore());
    game.moveToFoundation(1, 1);
    Assert.assertEquals(2, game.getScore());
    game.moveToFoundation(0, 1);
    Assert.assertEquals(3, game.getScore());
  }

  @Test
  public void testMoveToFoundationThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 0));
    game.startGame(cards, false, 3, 2);
    // srcPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(-1, 0));
    // srcPile >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(3, 0));
    // numCards < 1
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 0));
    // numCards > number of visible cards
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 2));
    // invalid move: not ace, to empty
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 0));
    // invalid move: different suit, rank valid
    game.moveToFoundation(1, 0);
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(1, 0));
  }

  // rank + 1 same suit
  @Test
  public void testMoveToFoundationThrows2() {
    game.startGame(cards2, false, 3, 2);
    // AS -> F0
    game.moveToFoundation(0, 0);
    // 2H -> C0(empty)
    game.movePile(2, 1, 0);
    // AH -> F1
    game.moveToFoundation(2, 1);
    // invalid move: 3H -> F1: AH
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(2, 1));
  }

  @Test
  public void testMoveDrawToFoundationValid() {
    game.startGame(cards3, false, 3, 2);
    game.moveDrawToFoundation(0);
    Assert.assertEquals(1, game.getScore());
    game.moveDrawToFoundation(0);
    Assert.assertEquals(2, game.getScore());
  }

  @Test
  public void testMoveDrawToFoundationThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
    game.startGame(cards, false, 3, 2);
    // destPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDrawToFoundation(-1));
    // destPile >= numPiles
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(3));
    // invalid move: not ace, to empty
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
    // invalid move: valid rank, different suit
    game.moveToFoundation(1, 1);
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(1));
  }

  @Test
  public void testMoveDrawToFoundationThrows2() {
    game.startGame(cards2, false, 3, 2);
    // AS -> F0
    game.moveToFoundation(0, 0);
    // invalid move: 3S -> F0: AS
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
  }

  @Test
  public void testDiscardDrawValid() {
    game.startGame(cards, false, 3, 2);
    game.discardDraw();
    Assert.assertEquals(List.of(aD, twoD), game.getDrawCards());
    game.discardDraw();
    Assert.assertEquals(List.of(twoD, aD), game.getDrawCards());
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
    game.moveToFoundation(1, 0);
    Assert.assertEquals(1, game.getScore());
    game.moveToFoundation(1, 1);
    Assert.assertEquals(2, game.getScore());
    game.moveToFoundation(0, 1);
    Assert.assertEquals(3, game.getScore());
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

    game.moveToFoundation(1, 0);
    game.movePile(1, 1, 0);
    Assert.assertEquals(2, game.getPileHeight(0));
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
    Assert.assertTrue(game.isCardVisible(1, 0));
    Assert.assertTrue(game.isCardVisible(2, 0));
    Assert.assertTrue(game.isCardVisible(2, 1));
  }

  @Test
  public void testGetCardAt2Args() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getCardAt(0, 0));

    game.startGame(cards3, false, 3, 2);
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
    Assert.assertEquals(twoC, game.getCardAt(1, 1));
    Assert.assertEquals(twoH, game.getCardAt(2, 2));
  }

  @Test
  public void testGetCardAt1Arg() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getCardAt(0));

    game.startGame(cards3, false, 3, 2);
    // card < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.getCardAt(-1));

    Assert.assertNull(game.getCardAt(1));

    game.moveToFoundation(0, 0);
    Assert.assertEquals(aS, game.getCardAt(0));
    game.movePile(1, 1, 0);
    game.moveToFoundation(1, 0);
    Assert.assertEquals(twoS, game.getCardAt(0));
  }

  @Test
  public void testGetNumFoundations() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.getNumFoundations());
    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(4, game.getNumFoundations());
  }

  @Test
  public void testIsGameOver() {
    List<Card> deck = List.of(aS, twoH, aH, twoS);
    Assert.assertThrows(IllegalStateException.class, () ->
            game.isGameOver());
    game.startGame(deck, false, 2, 1);
    game.moveToFoundation(0, 0);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    game.moveToFoundation(1, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testGameToCompletion() {
    game.startGame(cards, false, 3, 2);
    // AH -> F0
    game.moveToFoundation(1, 0);
    // AS -> F1
    game.moveToFoundation(1, 1);
    // 2S -> F1: AS
    game.moveToFoundation(0, 1);
    // AC -> F2
    game.moveToFoundation(2, 2);
    // 2C -> F2: AC
    game.moveToFoundation(2, 2);
    // 2H -> F0: AH
    game.moveToFoundation(2, 0);
    game.discardDraw();
    // AD -> F3
    game.moveDrawToFoundation(3);
    // 2D -> F3: AD
    game.moveDrawToFoundation(3);
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testShuffle() {
    KlondikeTextualView v1 = new KlondikeTextualView(game);
    KlondikeTextualView v2 = new KlondikeTextualView(game2);
    game.startGame(cards, false, 3, 2);
    game2.startGame(cards, true, 3, 2);
    Assert.assertNotEquals(v1.toString(), v2.toString());
  }
}