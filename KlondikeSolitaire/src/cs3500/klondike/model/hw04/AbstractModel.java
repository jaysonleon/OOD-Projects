package cs3500.klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeCard;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;

/**
 * Abstract class for the KlondikeModel interface. This class implements the methods that are
 * common to all KlondikeModel implementations.
 */
public abstract class AbstractModel implements KlondikeModel {
  protected List<Card> deckCopy;
  // the cascade is represented by a list of list of cards. Where index 0 of the first list
  //  is the leftmost cascade pile, and index 0 of the list of cards in the cascade list
  //  represents the first card in the pile, and size - 1 is the top card of the pile.
  protected List<List<Card>> cascade;
  // the foundation is represented by a list of list of cards. Where index 0 of the first list
  //  represents the left-most foundation pile, and index 0 of each list of cards represents
  //  the card with the highest rank.
  protected List<List<Card>> foundation;
  // the draw is represented by a list of cards. Index 0 is the currently available draw card.
  protected List<Card> draw;
  protected boolean gameStart;
  protected int numDraw;
  protected Random rand;

  /**
   * Constructor for AbstractModel. Instantiates common fields of all KlondikeModel implementations.
   */
  public AbstractModel() {
    this.deckCopy = new ArrayList<>();
    this.cascade = new ArrayList<>();
    this.foundation = new ArrayList<>();
    this.draw = new ArrayList<>();
    this.gameStart = false;
    this.numDraw = 0;
    this.rand = new Random(11215);
  }

  @Override
  public List<Card> getDeck() {
    List<Card> newDeck = new ArrayList<>();

    for (Rank r : Rank.values()) {
      newDeck.add(new KlondikeCard(Suit.CLUB, r));
      newDeck.add(new KlondikeCard(Suit.DIAMOND, r));
      newDeck.add(new KlondikeCard(Suit.HEART, r));
      newDeck.add(new KlondikeCard(Suit.SPADE, r));
    }

    return newDeck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    // check for if the game has started
    if (this.gameStart) {
      throw new IllegalStateException("Game has already started");
    }
    // check if deck is null
    if (deck == null) {
      throw new IllegalArgumentException("Invalid deck");
    }
    for (Card c : deck) {
      if (c == null) {
        throw new IllegalArgumentException("Invalid deck.");
      }
    }
    int aceCount = this.countAces(deck);
    // check for valid deck size
    if (deck.size() % aceCount != 0) {
      throw new IllegalArgumentException("Invalid deck");
    }
    // check for legal parameters
    if (numDraw < 1) {
      throw new IllegalArgumentException("Invalid maximum number of draw cards");
    }
    // check for legal parameters & that full cascade can be built with given sizes
    if (numPiles < 1 || deck.size() < numPiles * (numPiles + 1) / 2) {
      throw new IllegalArgumentException("Invalid number of piles");
    }
    this.deckCopy = new ArrayList<>(deck);
    // check if deck is valid
    if (this.isValidDeck()) {
      throw new IllegalArgumentException("Invalid deck.");
    }
    // instantiate fields from startGame
    this.numDraw = numDraw;
    int count = 0;
    // if requested, shuffle the deck
    if (shuffle) {
      this.deckCopy = this.randomize(this.deckCopy);
    }
    // add cascade piles based on the given numPiles
    for (int i = 0; i < numPiles; i++) {
      this.cascade.add(new ArrayList<>());
    }
    // add cards to the cascade, if the card row == col of the card, flip the card faceUp
    for (int i = 0; i < numPiles; i++) {
      for (int j = i; j < numPiles; j++) {
        this.cascade.get(j).add(this.deckCopy.get(count));
        count++;
        if (j == i) {
          this.cascade.get(j).get(i).faceUp();
        }
      }
    }
    // add number of foundation piles based on the number of aces in the deck
    for (int k = 0; k < aceCount; k++) {
      this.foundation.add(new ArrayList<>());
    }
    // deal rest of the cards into the draw pile
    for (int l = count; l < deck.size(); l++) {
      this.draw.add(this.deckCopy.get(l));
    }
    // handle # of draw cards faceUp based on numDraw
    if (this.draw.size() >= numDraw) {
      for (int m = 0; m < numDraw; m++) {
        this.draw.get(m).faceUp();
      }
    } else {
      for (Card card : this.draw) {
        card.faceUp();
      }
    }
    this.gameStart = true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {

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

    // if the destPile is empty, and the rank of the card is the highest rank in the deck,
    //  add the card to the cascade pile
    if (this.cascade.get(destPile).isEmpty()) {
      if (KlondikeCard.rankToValue(this.cascade.get(srcPile).get(this.cascade.get(srcPile).size()
              - numCards))
              == this.deckCopy.size() / this.countAces(this.deckCopy)) {
        // iterate through each card in srcPile, add if move is valid
        for (int i = numCards; i > 0; i--) {
          this.cascade.get(destPile).add(this.cascade.get(srcPile).remove(
                  this.cascade.get(srcPile).size() - i));
        }
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }
    // check for valid move to cascade
    else {
      if (this.getCardAt(srcPile, this.getPileHeight(srcPile) - numCards).isValidMoveCas(
              this.getCardAt(destPile, this.getPileHeight(destPile) - 1))) {
        for (int j = numCards; j > 0; j--) {
          this.cascade.get(destPile).add(this.cascade.get(srcPile).remove(
                  this.cascade.get(srcPile).size() - j));
        }
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }

    // handle flipping of cards under srcPile (in cascade) after move
    if (!this.cascade.get(srcPile).isEmpty()
            && this.cascade.get(srcPile).stream().noneMatch(Card::isVisible)) {
      this.cascade.get(srcPile).get(this.cascade.get(srcPile).size() - 1).faceUp();
    }
  }

  @Override
  public void moveDraw(int destPile) {
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

    // if the destPile is empty, and the draw card's rank must be the highest rank in the deck,
    //  add the card to the destPile
    if (this.cascade.get(destPile).isEmpty()) {
      if  (KlondikeCard.rankToValue(this.draw.get(0))
              == this.deckCopy.size() / this.countAces(this.deckCopy)) {
        this.cascade.get(destPile).add(this.draw.remove(0));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }
    // check for valid move
    else {
      if (this.draw.get(0).isValidMoveCas(this.getCardAt(destPile,
              this.getPileHeight(destPile) - 1))) {
        this.cascade.get(destPile).add(this.draw.remove(0));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }

    // handle flipping of cards under drawPile after move
    flippingDrawCardsHelper();
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    // check if game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid srcPile and foundationPile
    if (srcPile >= this.getNumPiles() || srcPile < 0 || foundationPile >= this.getNumFoundations()
            || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid move");
    }
    // check if srcPile is empty
    if (this.cascade.get(srcPile).isEmpty()) {
      throw new IllegalStateException("Invalid move");
    }

    // if the foundation is empty, and the card's rank is an ace, add it to the given foundationPile
    if (this.getCardAt(foundationPile) == null) {
      if (this.getCardAt(srcPile, this.getPileHeight(srcPile) - 1).toString().contains("A")) {
        this.foundation.get(foundationPile).add(this.cascade.get(srcPile).remove(
                this.getPileHeight(srcPile) - 1));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }
    // check if the move is valid
    else {
      if (this.getCardAt(srcPile, this.getPileHeight(srcPile) - 1).isValidMoveFound(
              this.getCardAt(foundationPile))) {
        this.foundation.get(foundationPile).add(0, this.cascade.get(srcPile).remove(
                this.getPileHeight(srcPile) - 1));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }

    // handle flipping of cards in cascade after move
    if (!this.cascade.get(srcPile).isEmpty() && this.cascade.get(srcPile).stream().noneMatch(
            Card::isVisible)) {
      this.cascade.get(srcPile).get(this.cascade.get(srcPile).size() - 1).faceUp();
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid foundationPile
    if (foundationPile >= this.getNumFoundations() || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }
    // check if drawPile is empty
    if (this.draw.isEmpty()) {
      throw new IllegalStateException("No draw cards");
    }
    // if the given foundationPile is empty, and the card's rank is an ace, add the
    //  card to the given foundationPile
    if (this.getCardAt(foundationPile) == null) {
      if (this.draw.get(0).toString().contains("A")) {
        this.foundation.get(foundationPile).add(this.draw.remove(0));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }
    // check for valid move, srcCard rank - (highest rank in foundationPile) == 1 &&
    //  suits must be the same
    else {
      if (this.draw.get(0).isValidMoveFound(this.getCardAt(foundationPile))) {
        this.foundation.get(foundationPile).add(0, this.draw.remove(0));
      } else {
        throw new IllegalStateException("Invalid move");
      }
    }

    // handle flipping of draw cards after cards are moved
    flippingDrawCardsHelper();
  }

  @Override
  public void discardDraw() {
    // check if game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check if drawPile is empty
    if (this.draw.isEmpty()) {
      throw new IllegalStateException("No draw cards");
    }
    // add the top card to the end of the drawPile, flip the card face down
    this.draw.add(this.draw.remove(0));
    this.draw.get(this.draw.size() - 1).faceDown();
    // check for the correct number of faceUp cards in drawPile
    flippingDrawCardsHelper();
  }

  @Override
  public int getNumRows() {
    // check if game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    int max = 0;
    // return the greatest pileSize in the cascade
    for (int i = 0; i < this.getNumPiles(); i++) {
      if (this.getPileHeight(i) > max) {
        max = this.getPileHeight(i);
      }
    }
    return max;
  }

  @Override
  public int getNumPiles() {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // return the number of piles in the cascade
    return this.cascade.size();
  }

  @Override
  public int getNumDraw() {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // return the max number of drawCards
    return this.numDraw;
  }

  @Override
  public boolean isGameOver() {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    if (this.isCascadeEmpty()) {
      return this.areFoundsFull();
    }
    if (this.movesLeftInCas() || this.movesLeftInDraw()) {
      return false;
    }
    else {
      return this.areFoundsFull() || this.draw.isEmpty();
    }
  }

  @Override
  public int getScore() {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // return the sum of the highest ranks in each foundation pile
    return this.foundation.stream().mapToInt(List::size).sum();
  }

  @Override
  public int getPileHeight(int pileNum) {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid pileNum
    if (pileNum >= this.getNumPiles() || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile");
    }
    // return the height of the given pileNum
    return this.cascade.get(pileNum).size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid pileNum
    if (pileNum >= this.getNumPiles() || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile");
    }

    // check for valid card
    if (card >= this.getPileHeight(pileNum) || card < 0) {
      throw new IllegalArgumentException("Invalid card");
    }

    // return if the card is faceUp or not
    return this.cascade.get(pileNum).get(card).isVisible();
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    // check for if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid pileNum
    if (pileNum >= this.getNumPiles() || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile");
    }
    // check for valid card
    if (card >= this.getPileHeight(pileNum) || card < 0) {
      throw new IllegalArgumentException("Invalid card");
    }
    // if this card is visible, return the card, if not throw an exception
    if (this.isCardVisible(pileNum, card)) {
      return this.cascade.get(pileNum).get(card);
    } else {
      throw new IllegalArgumentException("Card is not visible");
    }
  }

  @Override
  public Card getCardAt(int foundationPile) {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // check for valid foundationPile
    if (foundationPile >= this.getNumFoundations() || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }
    // if the given foundationPile is empty, return null, else return the topMost card
    //  in the given foundationPile
    if (this.foundation.get(foundationPile).isEmpty()) {
      return null;
    } else {
      return this.foundation.get(foundationPile).get(0);
    }
  }

  @Override
  public List<Card> getDrawCards() {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }

    // return the cards in the drawPile that are faceUp (currently available)
    return this.draw.stream().filter(Card::isVisible).collect(Collectors.toUnmodifiableList());
  }

  @Override
  public int getNumFoundations() {
    // check if the game has started
    if (!this.gameStart) {
      throw new IllegalStateException("Game has not started");
    }
    // return the number of foundation piles
    return this.foundation.size();
  }

  /**
   * Count the number of aces in the given deck of cards.
   *
   * @param deck the deck of cards to count aces
   * @return the total number of aces in the deck
   */
  protected int countAces(List<Card> deck) {
    return deck.stream().filter(c -> c.toString().contains("A")).mapToInt(c -> 1).sum();
  }

  /**
   * Return the given list of cards, randomized using this game's random seed.
   * @param deck the deck to shuffle
   * @return the given deck of cards, shuffled
   */
  protected List<Card> randomize(List<Card> deck) {
    List<Card> newDeck = new ArrayList<>(deck);
    Collections.shuffle(newDeck, this.rand);
    return newDeck;
  }

  /**
   * Helper method to flip the correct number of cards,
   *  based on the number of cards in the draw pile.
   */
  protected void flippingDrawCardsHelper() {
    if (this.draw.size() >= this.getNumDraw()) {
      for (int i = 0; i < this.getNumDraw(); i++) {
        this.draw.get(i).faceUp();
      }
    } else {
      for (Card card : this.draw) {
        card.faceUp();
      }
    }
  }

  /**
   * Return true iff the cascade has no cards in any pile.
   * @return true iff the cascade is empty (all piles are empty).
   */
  protected boolean isCascadeEmpty() {
    for (int i = 0; i < this.getNumPiles(); i++) {
      if (!this.cascade.get(i).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Is the deck in this BasicKlondike valid. A valid deck,
   *  consists of single-suit runs of the same size, starting from ace.
   * @return true iff the deck is valid
   */
  protected boolean isValidDeck() {
    if (this.deckCopy == null || this.deckCopy.isEmpty()) {
      throw new IllegalArgumentException("Invalid deck.");
    }

    List<Card> aces = this.deckCopy.stream().filter(c ->
            c.toString().contains("A")).collect(Collectors.toList());
    List<Card> runs = new ArrayList<>();
    for (Card c : aces) {
      for (int i = 1; i <= this.deckCopy.size() / aces.size(); i++) {
        runs.add(new KlondikeCard(c.getSuit(), Rank.convertInt(i)));
      }
    }
    return !new HashSet<>(runs).containsAll(this.deckCopy);
  }

  /**
   * Return true if there is still at least one valid move in the cascade.
   *  The move can either be from one pile to another, or from one cascade pile
   to an empty pile
   * @return true iff there is at least one valid move in the cascade
   */
  protected boolean movesLeftInCasToCas() {
    for (int srcPile = 0; srcPile < this.getNumPiles(); srcPile++) {
      for (int destPile = this.getNumPiles() - 1; destPile >= 0; destPile--) {
        if (this.cascade.get(srcPile).isEmpty()) {
          continue;
        }

        if (this.cascade.get(destPile).isEmpty()) {
          return KlondikeCard.rankToValue(this.getCardAt(
                  srcPile, this.cascade.get(srcPile).size() - 1))
                  == this.deckCopy.size() / this.countAces(this.deckCopy);
        }

        if (this.cascade.get(srcPile).get(this.getPileHeight(srcPile) - 1).isValidMoveCas(
                this.cascade.get(destPile).get(this.getPileHeight(destPile) - 1))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Return true iff there is at least one valid move left from the draw pile
   to a cascade pile.
   * @return true iff there is at least one valid move left from the draw pile
  to a cascade pile
   */
  protected boolean movesLeftInDrawToCas() {
    if (this.draw.isEmpty()) {
      return false;
    }

    for (int destPile = this.getNumPiles() - 1; destPile >= 0; destPile--) {
      if (this.cascade.get(destPile).isEmpty()) {
        return KlondikeCard.rankToValue(this.draw.get(0))
                == this.deckCopy.size() / this.countAces(this.deckCopy);
      }

      if (this.draw.get(0).isValidMoveCas(
              this.cascade.get(destPile).get(this.getPileHeight(destPile) - 1))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return true iff there is at least one valid move left,
   *    from the bottommost card of each cascade pile to a foundation pile.
   * @return true iff there is at least one valid move left from the cascade to the foundation
   */
  protected boolean movesLeftInCasToFound() {
    for (int srcPile = 0; srcPile < this.getNumPiles(); srcPile++) {
      for (int fPile = 0; fPile < this.getNumFoundations(); fPile++) {
        if (this.cascade.get(srcPile).isEmpty()) {
          continue;
        }
        if (this.foundation.get(fPile).isEmpty()) {
          if (this.getCardAt(srcPile, this.getPileHeight(srcPile) - 1).toString().contains(
                  "A")) {
            return true;
          }
          else {
            continue;
          }
        }

        if (this.getCardAt(srcPile, this.getPileHeight(srcPile) - 1).isValidMoveFound(
                this.getCardAt(fPile))) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Return true iff there is at least one move left from the draw pile to the foundation.
   * @return true iff there is at least one move left from the draw pile to the foundation
   */
  protected boolean movesLeftInDrawToFound() {
    if (this.draw.isEmpty()) {
      return false;
    }

    for (Card c : this.draw) {
      if (this.foundation.get(0).isEmpty()) {
        if (c.toString().contains("A")) {
          return true;
        }
        else {
          continue;
        }
      }
      if (c.isValidMoveFound(this.getCardAt(0))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return true iff there is at least one valid move left in the draw pile.
   *  A move can either be from the draw pile to a cascade pile,
   *    from the draw pile to a foundation pile, or discarding the current draw card.
   * @return true iff there is at least one valid move left in the draw pile.
   */
  protected boolean movesLeftInDraw() {
    return this.movesLeftInDrawToCas() || this.movesLeftInDrawToFound();
  }

  /**
   * Return true iff there is at least one valid move left in the cascade.
   *  A move can either be from one cascade pile to another, or from a cascade pile to
   *    a foundation pile.
   * @return true iff there is at least one valid move left in the cascade.
   */
  protected boolean movesLeftInCas() {
    return this.movesLeftInCasToCas() || this.movesLeftInCasToFound();
  }

  /**
   * Are the foundations in this game full.
   * @return true if the foundations are full, false otherwise
   */
  protected boolean areFoundsFull() {
    return this.getScore() == this.deckCopy.size();
  }
}
