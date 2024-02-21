package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Examplar for the extended model.
 */
public class ExamplarExtendedModelTests {
  private static BasicKlondike bk;
  private static LimitedDrawKlondike ldk;
  private static WhiteheadKlondike wk;

  @Before
  public void setUp() {
    bk = new BasicKlondike();
    ldk = new LimitedDrawKlondike(1);
    wk = new WhiteheadKlondike();
  }

  private static List<Card> build(List<String> cardsWanted) {
    List<Card> d = bk.getDeck();
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
  public void exampleLDDiscard() {
    List<Card> cards = build(List .of("2♠", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "A♣"));
    ldk.startGame(cards, false, 3, 2);
    ldk.discardDraw();
    ldk.discardDraw();
    ldk.discardDraw();
    ldk.discardDraw();
    Assert.assertEquals(0, ldk.getDrawCards().size());
  }

  @Test
  public void exampleWhValidMPP() {
    List<Card> cards = build(List.of("A♣", "2♢", "2♡", "2♣", "A♠", "A♢", "A♡", "2♠"));
    wk.startGame(cards, false, 3, 2);
    wk.movePile(0, 1, 1);
    Assert.assertEquals(0, wk.getPileHeight(0));
    Assert.assertEquals(3, wk.getPileHeight(1));
  }

  @Test
  public void exampleWhInvalidMPP() {
    List<Card> cards = build(List.of("A♣", "2♢", "2♣", "2♡", "A♠", "A♢", "A♡", "2♠"));
    wk.startGame(cards, false, 3, 2);
    Assert.assertThrows(IllegalStateException.class, () ->
            wk.movePile(0,1,1));
  }

  @Test
  public void exampleWhValidMPPBuild() {
    List<Card> cards = build(List.of("A♣", "2♢", "2♡", "2♣", "A♠", "A♢", "A♡", "2♠"));
    wk.startGame(cards, false, 3, 2);
    wk.movePile(0,1,1);
    wk.movePile(1,2,0);
    Assert.assertEquals(2, wk.getPileHeight(0));
  }

  @Test
  public void exampleWhValidMPPBuild2() {
    List<Card> cards = build(List.of("A♠", "2♢", "2♡", "2♣", "A♣", "A♢", "A♡", "2♠"));
    wk.startGame(cards, false, 3, 2);
    wk.movePile(0,1,1);
    Assert.assertThrows(IllegalStateException.class, () ->
            wk.movePile(1,2,0));
  }
}