package cs3500.klondike.model.hw04.hw03;

import cs3500.klondike.model.hw02.KlondikeCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;

import org.junit.Assert;
import org.junit.Test;


/**
 * Tests for methods in the KlondikeCard interface.
 */
public class TestKlondikeCard {

  Card aS = new KlondikeCard(Suit.SPADE, Rank.ACE);
  Card twoS = new KlondikeCard(Suit.SPADE, Rank.TWO);
  Card threeS = new KlondikeCard(Suit.SPADE, Rank.THREE);
  Card fourS = new KlondikeCard(Suit.SPADE, Rank.FOUR);
  Card aH = new KlondikeCard(Suit.HEART, Rank.ACE);
  Card twoH = new KlondikeCard(Suit.HEART, Rank.TWO);
  Card threeH = new KlondikeCard(Suit.HEART, Rank.THREE);
  Card fourH = new KlondikeCard(Suit.HEART, Rank.FOUR);
  Card fiveS = new KlondikeCard(Suit.SPADE, Rank.FIVE);
  Card fiveH = new KlondikeCard(Suit.HEART, Rank.FIVE);
  Card kingS = new KlondikeCard(Suit.SPADE, Rank.KING);

  @Test
  public void testToStringCard() {
    Card aS = new KlondikeCard(Suit.SPADE, Rank.ACE);
    Assert.assertEquals("A♠", aS.toString());
  }

  @Test
  public void testEquals() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.TWO);
    KlondikeCard c2 = new KlondikeCard(Suit.SPADE, Rank.TWO);
    Assert.assertEquals(true, c1.equals(c2));
  }

  @Test
  public void testEquals2() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.TWO);
    Card c2 = new KlondikeCard(Suit.SPADE, Rank.THREE);
    Assert.assertFalse(c1.equals(c2));
  }

  @Test
  public void testSameCardSameHash() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.ACE);
    Card c2 = new KlondikeCard(Suit.SPADE, Rank.ACE);
    Assert.assertEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  public void testDiffRankHash() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.ACE);
    Card c2 = new KlondikeCard(Suit.SPADE, Rank.KING);
    Assert.assertNotEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  public void testDiffSuitHash() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.ACE);
    Card c2 = new KlondikeCard(Suit.HEART, Rank.ACE);
    Assert.assertNotEquals(c1.hashCode(), c2.hashCode());
  }

  @Test
  public void testIsVisibleFaceDown() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.ACE);
    Assert.assertFalse(c1.isVisible());
  }

  @Test
  public void testIsVisibleFaceUp() {
    Card c1 = new KlondikeCard(Suit.SPADE, Rank.ACE);
    c1.faceUp();
    Assert.assertTrue(c1.isVisible());
  }

  @Test
  public void testIsValidMoveCas() {
    // this rank > other rank (same color)
    Assert.assertFalse(twoS.isValidMoveCas(aS));
    // this rank > other rank (diff color)
    Assert.assertFalse(twoS.isValidMoveCas(aH));
    // this rank < other rank (same color)
    Assert.assertFalse(aS.isValidMoveCas(twoS));
    // this rank < other rank (diff color)
    Assert.assertTrue(aS.isValidMoveCas(twoH));
  }

  @Test
  public void testIsValidMoveFound() {
    // this rank < other rank (same suit)
    Assert.assertFalse(twoS.isValidMoveFound(threeH));
    // this rank - other rank = 1 (diff suit)
    Assert.assertFalse(twoS.isValidMoveFound(aH));
    // this rank - other rank = 1 (same suit)
    Assert.assertTrue(twoS.isValidMoveFound(aS));

  }
}
