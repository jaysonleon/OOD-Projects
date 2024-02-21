package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a game of Whitehead Klondike Solitaire. In this version, the cascade is dealt faceUp.
 * To move a card in the cascade: if the destinationPile is empty and numCards = 1, any card can
 * be moved.
 * If the destination pile is not empty, and numCards = 1,
 * the destCard's rank - the srcCard's rank = 1, AND the cards must be the same color.
 * If the destination pile is empty and numCards > 1, the cards must be in a valid build.
 * A valid build consists of cards of the same suit, in order, with the top card being 1 rank
 * higher than the previous card.
 * If numCards = 1, the card can be moved if it is a valid move. A valid move is defined as when
 * the destCard's rank - the srcCard's rank = 1 and the colors are the SAME.
 * The rules for moving to the foundation remain unchanged from BasicKlondike.
 */
public class WhiteheadKlondike extends AbstractModel {
  /**
   * Constructor for WhiteheadKlondike, instantiates fields.
   */
  public WhiteheadKlondike() {
    super();
    this.rand = new Random(3);
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    super.startGame(deck, shuffle, numPiles, numDraw);
    // flip all cards in cascade
    for (int i = 0; i < this.getNumPiles(); i++) {
      for (int j = 0; j < this.getPileHeight(i); j++) {
        this.cascade.get(i).get(j).faceUp();
      }
    }
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) throws IllegalStateException {
    // check for if game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid srcPile and destPile
    if (srcPile >= this.getNumPiles() || srcPile < 0 || destPile >= this.getNumPiles()
            || destPile < 0 || srcPile == destPile) {
      throw new IllegalArgumentException("Invalid move");
    }
    // check for valid numCards
    if (numCards > this.cascade.get(srcPile).size() || numCards < 1) {
      throw new IllegalArgumentException("Invalid number of cards");
    }
    // check for if there is enough cards to move from srcPile to destPile
    if (this.cascade.get(srcPile).stream().filter(Card::isVisible).count() < numCards) {
      throw new IllegalStateException("Not enough cards to move");
    }
    // if the destPile is empty, and numCards = 1, any card can be moved
    if (this.cascade.get(destPile).isEmpty()) {
      if (numCards == 1) {
        this.cascade.get(destPile).add(this.cascade.get(srcPile).remove(
                this.cascade.get(srcPile).size() - 1));
      } else {
        // if destPile is empty, and numCards > 1,
        // srcCards to be moved must be same suit and in order
        List<Card> srcCards = new ArrayList<>(this.cascade.get(srcPile).subList(
                this.cascade.get(srcPile).size() - numCards, this.cascade.get(srcPile).size()));
        if (!sameSuitHelper(srcCards)) {
          throw new IllegalStateException("Invalid move");
        }
        for (int j = numCards; j > 0; j--) {
          this.cascade.get(destPile).add(this.cascade.get(srcPile).remove(
                  this.cascade.get(srcPile).size() - j));
        }
      }
    } else {
      // if destPile is not empty, and numCards = 1, card can be moved if it is valid move
      if (numCards == 1) {
        if (this.cascade.get(srcPile).get(this.cascade.get(srcPile).size() - 1).isValidMoveCasWh(
                this.cascade.get(destPile).get(this.cascade.get(destPile).size() - 1))) {
          this.cascade.get(destPile).add(this.cascade.get(srcPile).remove(
                  this.cascade.get(srcPile).size() - 1));
        } else {
          throw new IllegalStateException("Invalid move");
        }
      } else {
        // if destPile is not empty, and numCards > 1,
        // srcCards to be moved must be same suit and in order
        List<Card> srcCards = this.cascade.get(srcPile).subList(this.cascade.get(srcPile).size()
                - numCards, this.cascade.get(srcPile).size());
        if (!sameSuitHelper(srcCards)) {
          throw new IllegalStateException("Invalid move");
        }
        if (this.cascade.get(srcPile).get(
                this.cascade.get(srcPile).size() - numCards).isValidMoveCasWh(
                        this.cascade.get(destPile).get(this.cascade.get(destPile).size() - 1))) {
          for (int i = numCards; i > 0; i--) {
            this.cascade.get(destPile).add(this.cascade.get(srcPile).remove(
                    this.cascade.get(srcPile).size() - i));
          }
        } else {
          throw new IllegalStateException("Invalid move");
        }
      }
    }
    // handle flipping of cards under srcPile (in cascade) after move
    if (!this.cascade.get(srcPile).isEmpty()
            && this.cascade.get(srcPile).stream().noneMatch(Card::isVisible)) {
      this.cascade.get(srcPile).get(this.cascade.get(srcPile).size() - 1).faceUp();
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    // check for if game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid destPile
    if (destPile >= this.getNumPiles() || destPile < 0) {
      throw new IllegalArgumentException("Invalid destination pile");
    }
    // check if draw is empty
    if (this.draw.isEmpty()) {
      throw new IllegalStateException("No draw cards");
    }

    if (this.cascade.get(destPile).isEmpty()) {
      this.cascade.get(destPile).add(this.draw.remove(0));
    } else {
      if (this.draw.get(0).isValidMoveCasWh(this.cascade.get(destPile).get(
              this.cascade.get(destPile).size() - 1))) {
        this.cascade.get(destPile).add(this.draw.remove(0));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }

    super.flippingDrawCardsHelper();
  }

  /**
   * Iterate through the given list and return true if the visible cards have the same suit.
   *
   * @param pile the given list of cards
   * @return true iff the visible cards in the given list are the same suit.
   */
  private static boolean sameSuitHelper(List<Card> pile) {
    List<Card> visibleCards = pile.stream().filter(Card::isVisible).collect(Collectors.toList());
    if (visibleCards.size() == 1) {
      return true;
    }
    Suit suit = visibleCards.get(0).getSuit();
    for (Card c : visibleCards) {
      if (c.getSuit() != suit) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return true if there is still at least one valid move in the cascade.
   * The move can either be from one pile to another, or from one cascade pile
   * to an empty pile. Had to overwrite this method to fit logic for whitehead solitaire.
   *
   * @return true iff there is at least one valid move in the cascade
   */
  @Override
  protected boolean movesLeftInCasToCas() {
    for (int srcPile = 0; srcPile < this.getNumPiles(); srcPile++) {
      for (int destPile = this.getNumPiles() - 1; destPile >= 0; destPile--) {
        if (this.cascade.get(srcPile).isEmpty()) {
          continue;
        }

        if (this.cascade.get(destPile).isEmpty()) {
          return true;
        }

        if (this.cascade.get(srcPile).get(this.getPileHeight(srcPile) - 1).isValidMoveCasWh(
                this.cascade.get(destPile).get(this.getPileHeight(destPile) - 1))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Return true iff there is at least one valid move left from the draw pile
   * to a cascade pile.
   *
   * @return true iff there is at least one valid move left from the draw pile
   *     to a cascade pile
   */
  @Override
  protected boolean movesLeftInDrawToCas() {
    if (this.draw.isEmpty()) {
      return false;
    }

    for (int destPile = this.getNumPiles() - 1; destPile >= 0; destPile--) {
      if (this.cascade.get(destPile).isEmpty()) {
        return true;
      }

      if (this.draw.get(0).isValidMoveCasWh(
              this.cascade.get(destPile).get(this.getPileHeight(destPile) - 1))) {
        return true;
      }
    }
    return false;
  }

}
