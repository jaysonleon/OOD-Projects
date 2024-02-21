package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.Card;

import java.util.List;
import java.util.Random;

/**
 * Represents a game of Klondike Solitaire with a limited number of times the player can discard
 *  a draw card.
 *  In this version, the rules for moving cards are the same as in BasicKlondike.
 *  Cards are also dealt in the same manner as in BasicKlondike.
 *  After a draw card is discarded the allowed number of times,
 *  the card is removed from the draw pile, and can no longer be used.
 */
public class LimitedDrawKlondike extends AbstractModel {
  private final int numTimesRedrawAllowed;
  // the maximum number of remaining draws, at the start of the game, is the number of cards
  //  in the draw pile, times the number of times the player is allowed to redraw. Once this
  //  number reaches 0, the player can no longer redraw and discarding a draw card will
  //  remove it from the draw pile entirely.
  private int remainingDraws;

  /**
   * Constructor for LimitedDrawKlondike, instantiates fields.
   * @param numTimesRedrawAllowed the number of times the player is allowed to discard a draw card.
   */
  public LimitedDrawKlondike(int numTimesRedrawAllowed) {
    super();
    if (numTimesRedrawAllowed < 0) {
      throw new IllegalArgumentException("Number of times to redraw cannot be negative");
    }
    this.numTimesRedrawAllowed = numTimesRedrawAllowed;
    this.rand = new Random(2);
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    super.startGame(deck, shuffle, numPiles, numDraw);
    this.remainingDraws = this.draw.size() * this.numTimesRedrawAllowed;
  }

  @Override
  public void moveDraw(int destPile) {
    super.moveDraw(destPile);
    this.remainingDraws--;
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    super.moveDrawToFoundation(foundationPile);
    this.remainingDraws--;
  }

  @Override
  public void discardDraw() {
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }

    if (this.draw.isEmpty()) {
      throw new IllegalStateException("No draw cards");
    }
    if (remainingDraws > 0) {
      this.draw.get(0).faceDown();
      this.draw.add(this.draw.remove(0));
      remainingDraws--;
    }
    else {
      this.draw.remove(0);
      remainingDraws--;
    }

    flippingDrawCardsHelper();
  }
}

