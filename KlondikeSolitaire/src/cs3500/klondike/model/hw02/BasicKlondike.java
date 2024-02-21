package cs3500.klondike.model.hw02;

import java.util.Random;

import cs3500.klondike.model.hw04.AbstractModel;

/**
 * Represents a basic game of Klondike Solitaire. In this version, the casacde is dealt faceDown,
 *  with the top card (index size - 1), is faceUp.
 *  A valid cascade move is when the destCard's rank - the srcCard's rank = 1,
 *  AND the cards are opposite colors. A valid foundation move must start with an ace,
 *  then the next card's rank must be 1 higher than the previous card's rank,
 *  AND the SAME suit. This game will have an infinite number of discards.
 *  The game is over when either all the foundation piles are full,
 *  OR if there are no more moves to be made.
 */
public class BasicKlondike extends AbstractModel {

  /**
   * Constructor for BasicKlondike, instantiates fields.
   */
  public BasicKlondike() {
    super();
    this.rand = new Random(1);
  }

  /**
   * Custom constructor for randomness.
   *
   * @param seed the random seed
   */
  public BasicKlondike(int seed) {
    super();
    this.rand = new Random(seed);
  }
}

