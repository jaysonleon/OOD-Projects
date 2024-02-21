package cs3500.klondike.model.hw04.hw03;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for examplar.
 */
public class ExamplarModelTests {

  private KlondikeModel model;

  /**
   * Initialize data for testing.
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();

  }

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

  // invalid movePileToFoundation: 2S -> AH (Foundation)
  @Test
  public void exampleInvalidMovePileToFoundation() {
    List<Card> cards = build(List.of("2♠", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "A♣"));
    setUp();
    model.startGame(cards, false, 3, 1);
    model.moveToFoundation(2, 0);
    Assert.assertThrows(IllegalStateException.class, () -> model.moveToFoundation(0, 0));
  }

  // valid movePile: AS -> 2H
  @Test
  public void exampleValidMovePile() {
    List<Card> cards = build(List.of("A♠", "A♣", "A♢", "A♡", "2♠", "2♡", "2♣", "2♢"));
    setUp();
    model.startGame(cards, false, 3, 1);
    model.movePile(0, 1, 2);
    Assert.assertEquals(4, model.getPileHeight(2));
  }

  // valid moveDraw: AH -> 2S
  @Test
  public void exampleValidMoveDraw() {
    List<Card> cards = build(List.of("2♠", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "A♣"));
    setUp();
    model.startGame(cards, false, 3, 1);
    model.moveDraw(0);
    Assert.assertEquals(2, model.getPileHeight(0));
  }

  // valid moveDrawToFoundation: AH -> Foundation
  @Test
  public void exampleMoveDrawToFoundation() {
    List<Card> cards = build(List.of("2♠", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "A♣"));
    setUp();
    model.startGame(cards, false, 3, 1);
    model.moveDrawToFoundation(0);
    Assert.assertEquals(1, model.getScore());
  }

  // invalid movePile: 2S -> 2C
  @Test
  public void exampleInvalidMovePile() {
    List<Card> cards = build(List.of("2♠", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "A♣"));
    setUp();
    model.startGame(cards, false, 3, 1);
    Assert.assertThrows(IllegalStateException.class, () -> model.movePile(0, 1, 1));
  }

  // Invalid moveToFoundation: 2S -> empty foundation pile
  @Test
  public void exampleMoveToFoundationMtFoundPile() {
    List<Card> cards = build(List.of("2♠", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "A♣"));
    setUp();
    model.startGame(cards, false, 3, 1);
    Assert.assertThrows(IllegalStateException.class, () -> model.moveToFoundation(0, 0));
  }

  // test for mutation in discardDraw
  @Test
  public void exampleDiscardDrawMutation() {
    List<Card> cards = build(List.of("A♠", "A♢", "A♡", "A♣"));
    setUp();
    model.startGame(cards, false, 2, 1);
    model.discardDraw();
    Assert.assertEquals("A♣", model.getDrawCards().get(0).toString());
  }
}
