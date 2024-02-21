package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeCard;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;

/**
 * Testing for game moves.
 */
public class TestBasicMoves extends Constants {
  private List<Card> cards3;
  private List<Card> cards4;
  Card fiveH = new KlondikeCard(Suit.HEART, Rank.FIVE);

  @Before
  public void setUp() {
    Card fiveS = new KlondikeCard(Suit.SPADE, Rank.FIVE);
    cards2 = List.of(fourS, fourH, threeS, threeH, twoS, twoH, aS, aH);
    cards3 = List.of(aS, twoS, threeS, fourS, fiveS, aH, twoH, threeH, fourH, fiveH);
    cards4 = List.of(fourS, threeH, fourH, aS, aH, twoS, twoH, threeS);
    game = new BasicKlondike();
    game2 = new BasicKlondike();
  }

  @Test
  public void testGetDeck() {
    List<Card> cards = game.getDeck();
    Assert.assertEquals(52, cards.size());
  }

  @Test
  public void testStartGameValid() {
    game.startGame(cards, false, 3, 2);
    Assert.assertTrue(game.isCardVisible(0, 0));
    Assert.assertFalse(game.isCardVisible(1, 0));
    Assert.assertTrue(game.isCardVisible(1, 1));
    Assert.assertFalse(game.isCardVisible(2, 0));
    Assert.assertFalse(game.isCardVisible(2, 1));
    Assert.assertTrue(game.isCardVisible(2, 2));
    Assert.assertEquals(2, game.getNumFoundations());
    Assert.assertEquals(2, game.getDrawCards().size());
  }

  @Test
  public void testStartGameThrows() {
    game.startGame(cards, false, 3, 2);
    Assert.assertThrows(IllegalStateException.class, () -> game.startGame(cards,
            false, 3, 2));
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

    // legal move AS -> 2H
    game.movePile(0, 1, 2);

    Assert.assertEquals(4, game.getPileHeight(2));
  }

  @Test
  public void testMovePileThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(0, 1, 1));
    game.startGame(cards, false, 3, 2);
    // invalid srcPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(-1, 1, 0));
    // invalid srcPile >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(3, 1, 0));

    // invalid numCards < 1
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(1, 0, 0));
    // invalid numCards > cards that can be moved
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(2, 3, 0));

    // invalid destPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(1, 1, -1));
    // invalid destPile >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(1, 2, 3));

    // invalid srcPile = destPile
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, 0));

    // types of invalid moves
    // srcCard rank > destCard rank 2 -> A, same suit/diff suits
    // invalid move: 2H -> AS
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(1, 1, 0));


    // Invalid: 2H -> 4S
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(2, 1, 1));
    // AS -> F0 ()
    game.moveToFoundation(0, 0);
    // Invalid move, 4S -> empty
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(2, 1, 0));

    // destCard rank - srcCard rank > 1 AS -> 4S
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(1, 1, 2));
    // destCard rank == srcCard rank
    Assert.assertThrows(IllegalStateException.class, () ->
            game.movePile(1, 1, 2));
    // Illegal move: srcPile is empty,
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.movePile(0, 1, 1));
  }

  @Test
  public void testMoveDrawValid() {
    game.startGame(cards, false, 3, 2);
    // Valid move: 3H -> 4S
    game.moveDraw(1);
    // Valid move: AS -> F0 ()
    game.moveToFoundation(0, 0);

    // Valid move: draw: 4H -> empty src
    game.moveDraw(0);
    Assert.assertEquals(3, game.getPileHeight(2));
  }

  @Test
  public void testMoveDrawThrows() {
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(2));

    game.startGame(cards, false, 3, 2);
    // Illegal move: destinationPile # < 0
    Assert.assertThrows(IllegalArgumentException.class, () -> game.moveDraw(-1));
    // Illegal move: destinationPile # >= this.numPiles
    Assert.assertThrows(IllegalArgumentException.class, () -> game.moveDraw(3));

    // AS -> F0 ()
    game.moveToFoundation(0, 0);

    // Illegal Move:
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(2, 1));

    // 3H -> 4S
    game.moveDraw(1);
    // 4H -> empty src
    game.moveDraw(0);

    // no draw cards
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));
  }

  @Test
  public void testMoveDrawIllegalMoves() {
    game.startGame(cards, false, 3, 2);
    // move not allowable
    // types of illegal moves

    game.moveToFoundation(0, 0);
    game.discardDraw();
    // 4H -> cascade 0 ()
    game.moveDraw(0);
    // Invalid move: draw color == dest color 3H -> 4H
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(0));

    game.discardDraw();
  }

  @Test
  public void testMoveDrawThrows2() {
    game.startGame(cards2, false, 3, 2);

    // Invalid move: AS -> 3S
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(1));

    // AS -> AH
    game.discardDraw();
    // Invalid move: AH -> 3S
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDraw(1));
  }

  @Test
  public void testMoveToFoundationValid() {
    game.startGame(cards, false, 3, 2);
    // Legal move: AS -> F0 ()
    game.moveToFoundation(0, 0);
    Assert.assertEquals(1, game.getScore());

  }

  @Test
  public void testMoveToFoundationThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 0));

    game.startGame(cards, false, 3, 2);

    // Invalid: srcPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(-1, 0));
    // Invalid: srcPile >= this.getNumPiles()
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(3, 0));
    // Invalid: foundPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(0, -1));
    // Invalid: foundPile >= this.getNumFoundations()
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveToFoundation(0, 2));

    // Illegal move: 2H -> F0 ()
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(2, 0));

    // AS -> F0 ()
    game.moveToFoundation(0, 0);
    // Illegal move: empty src -> F0 (AS)
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(0, 0));
    // Illegal move: 2H -> F0 (AS)
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(2, 0));
    // Illegal move: 3S -> F0 (AS)
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveToFoundation(1, 0));
  }

  @Test
  public void testMoveDrawToFoundValid() {
    game.startGame(cards2, false, 3, 2);

    // AS -> F0 ()
    game.moveDrawToFoundation(0);
    Assert.assertEquals(1, game.getScore());

    // AH -> F1 ()
    game.moveDrawToFoundation(1);
    Assert.assertEquals(2, game.getScore());
  }

  @Test
  public void testMoveDrawToFoundThrows() {
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));

    game.startGame(cards2, false, 3, 2);
    // Invalid: foundPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDrawToFoundation(-1));
    // Invalid: foundPile > this.getNumFoundations
    Assert.assertThrows(IllegalArgumentException.class, () ->
            game.moveDrawToFoundation(2));
  }

  @Test
  public void testMoveDrawToFoundThrows2() {
    List<Card> deck = List.of(aS, twoH, fourS, aH, fourH, threeH, twoS, threeS);
    game.startGame(deck, false, 3, 2);


    // Illegal Moves:
    // Illegal move: 2S -> F0 ()
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));

    // AH -> F1 ()
    game.moveToFoundation(1, 1);

    // 2S -> 3S, 2S
    game.discardDraw();
    // Illegal move: 3S -> F0 (AS)
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
    game.discardDraw();
    game.moveToFoundation(0, 0);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(0);

    // no draw cards
    Assert.assertThrows(IllegalStateException.class, () -> game.moveDrawToFoundation(0));

  }

  @Test
  public void testMoveDrawToFoundThrowsNoDraw() {
    game.startGame(cards2, false, 3, 2);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(1);

    // no draw cards
    Assert.assertThrows(IllegalStateException.class, () ->
            game.moveDrawToFoundation(0));
  }

  @Test
  public void testDiscardDrawValid() {
    game.startGame(cards3, false, 3, 2);
    // draw = 2H, 3H -- 4H, 5H
    List<Card> ans1 = List.of(twoH, threeH);

    // 3H -> back of draw
    game.discardDraw();
    // draw.get(0) = 4H
    // draw.get(1) = 3H
    List<Card> ans2 = List.of(threeH, fourH);
    Assert.assertEquals(ans2, game.getDrawCards());

    // 4H -> back of draw.get(0)
    game.discardDraw();
    List<Card> ans3 = List.of(fourH, fiveH);
    Assert.assertEquals(ans3, game.getDrawCards());

    game.discardDraw();
    game.discardDraw();
    Assert.assertEquals(ans1, game.getDrawCards());
  }

  @Test
  public void testGetNumRows() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getNumRows());

    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(3, game.getNumRows());

    game.movePile(0, 1, 2);
    Assert.assertEquals(4, game.getNumRows());
  }

  @Test
  public void testGetNumPiles() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getNumPiles());

    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(3, game.getNumPiles());
  }

  @Test
  public void testGetNumDraw() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getNumDraw());

    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(2, game.getNumDraw());

    game.discardDraw();
    Assert.assertEquals(2, game.getNumDraw());
  }

  @Test
  public void testGetScore() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getNumDraw());

    game.startGame(cards2, false, 3, 2);
    Assert.assertEquals(0, game.getScore());

    game.moveDrawToFoundation(0);
    Assert.assertEquals(1, game.getScore());

    game.moveDrawToFoundation(1);
    Assert.assertEquals(2, game.getScore());

    game.moveToFoundation(2, 1);
    Assert.assertEquals(3, game.getScore());

    game.moveToFoundation(2, 0);
    Assert.assertEquals(4, game.getScore());
  }

  @Test
  public void testGetPileHeightValid() {
    game.startGame(cards2, false, 3, 2);
    Assert.assertEquals(1, game.getPileHeight(0));
    Assert.assertEquals(2, game.getPileHeight(1));
    Assert.assertEquals(3, game.getPileHeight(2));

    // AS -> 2H
    game.moveDraw(2);
    Assert.assertEquals(2, game.getPileHeight(1));
    Assert.assertEquals(4, game.getPileHeight(2));
  }

  @Test
  public void testGetPileHeightThrows() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getPileHeight(0));

    game.startGame(cards2, false, 3, 2);
    // Invalid: pileNum < 0
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getPileHeight(-1));
    // Invalid: pileNum >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getPileHeight(3));
  }

  @Test
  public void testIsCardVisibleValid() {
    game.startGame(cards, false, 3, 2);
    Assert.assertTrue(game.isCardVisible(0, 0));
    Assert.assertFalse(game.isCardVisible(1, 0));
    Assert.assertTrue(game.isCardVisible(1, 1));
    Assert.assertFalse(game.isCardVisible(2, 0));
    Assert.assertFalse(game.isCardVisible(2, 1));
    Assert.assertTrue(game.isCardVisible(2, 2));
  }

  @Test
  public void testIsCardVisibleThrows() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getPileHeight(0));

    game.startGame(cards, false, 3, 2);
    // Invalid: pileNum < 0
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getPileHeight(-1));
    // Invalid: pileNum >= numPiles
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getPileHeight(3));
  }


  @Test
  public void testGetCardAt2ArgsValid() {
    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(aS, game.getCardAt(0, 0));
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getCardAt(1, 0));

    game.moveToFoundation(0, 0);

    Assert.assertEquals(fourS, game.getCardAt(1, 1));
  }

  @Test
  public void testGetCardAt1ArgValid() {
    game.startGame(cards2, false, 3, 2);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(1);
    Assert.assertEquals(aS, game.getCardAt(0));
    Assert.assertEquals(aH, game.getCardAt(1));

    game.moveToFoundation(2, 1);
    Assert.assertEquals(twoH, game.getCardAt(1));
  }

  @Test
  public void testGetCardAt1ArgThrows() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getCardAt(0));

    game.startGame(cards2, false, 3, 2);
    // Invalid: foundPile < 0
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getCardAt(-1));
    // Invalid: foundPile >= game.getNumFoundations
    Assert.assertThrows(IllegalArgumentException.class, () -> game.getCardAt(2));
  }

  @Test
  public void testGetDrawCardsValid() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getDrawCards());

    game.startGame(cards3, false, 3, 2);
    List<Card> ans1 = List.of(twoH, threeH);
    Assert.assertEquals(ans1, game.getDrawCards());

    game.discardDraw();
    List<Card> ans2 = List.of(threeH, fourH);
    Assert.assertEquals(ans2, game.getDrawCards());

    game.discardDraw();
    List<Card> ans3 = List.of(fourH, fiveH);
    Assert.assertEquals(ans3, game.getDrawCards());

    game.discardDraw();
    List<Card> ans4 = List.of(fiveH, twoH);
    Assert.assertEquals(ans4, game.getDrawCards());

    game.discardDraw();
    Assert.assertEquals(ans1, game.getDrawCards());
  }

  @Test
  public void testGetNumFoundations() {
    Assert.assertThrows(IllegalStateException.class, () -> game.getNumFoundations());

    game.startGame(cards, false, 3, 2);
    Assert.assertEquals(2, game.getNumFoundations());

    game2.startGame(game.getDeck(), false, 7, 3);
    Assert.assertEquals(4, game2.getNumFoundations());
  }

  @Test
  public void testIsGameOver() {
    game.startGame(cards2, false, 3, 2);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(2, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(2, 0);
    game.moveToFoundation(2, 0);
    game.moveToFoundation(1, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    game.moveToFoundation(0, 0);
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testShuffle() {
    game.startGame(cards, true, 3, 2);
    KlondikeModel game3 = new BasicKlondike(5);
    game3.startGame(cards2, true, 3, 2);
    Assert.assertNotEquals(game.getDrawCards(), game3.getDrawCards());
  }

  @Test
  public void testGameOver2() {
    game.startGame(cards4, false, 2, 3);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(1);
    game.moveDrawToFoundation(0);
    game.moveDrawToFoundation(1);
    Assert.assertFalse(game.isGameOver());
    game.moveDrawToFoundation(0);
    game.moveToFoundation(0, 0);
    game.movePile(1, 1, 0);
    game.moveToFoundation(1, 1);
    game.moveToFoundation(0, 1);
    Assert.assertTrue(game.isGameOver());
  }

  @Test
  public void testIsGameOverNewMethod() {
    List<Card> deck = List.of(aS, twoH, aH, twoS);
    game.startGame(deck, false, 2, 1);
    game.moveToFoundation(0, 0);
    game.moveDrawToFoundation(0);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    Assert.assertFalse(game.isGameOver());
    game.moveToFoundation(1, 1);
    Assert.assertTrue(game.isGameOver());
  }
}
