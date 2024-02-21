package cs3500.klondike.model.hw02;

import java.util.Objects;

/**
 * Represents a card in the cs3500.klondike.Klondike Game.
 *  A card has a (String) suit, (int) rank, and (boolean)
 *  faceUp.
 */
public class KlondikeCard implements Card {

  private final Suit suit;
  private final Rank rank;
  private boolean faceUp;

  /**
   * Constructor for a card, instantiates values, default is card faceDown.
   * @param suit the suit of the card
   * @param rank the rank of the card
   */
  public KlondikeCard(Suit suit, Rank rank) {
    this.rank = Objects.requireNonNull(rank);
    this.suit = Objects.requireNonNull(suit);
    this.faceUp = false;
  }

  /**
   * Convert this card to a string, it should return Rank, Suit.
   * @return the string of the rank and suit of this card.
   */
  public String toString() {
    if (this.rank.toString().equals("J")) {
      return "J" + suit;
    }
    else if (this.rank.toString().equals("Q")) {
      return "Q" + suit;
    }
    else if (this.rank.toString().equals("K")) {
      return "K" + suit;
    }
    else if (this.rank.toString().equals("A")) {
      return "A" + suit;
    }
    else {
      return rank + suit.toString();
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Card)) {
      return false;
    }

    KlondikeCard otherCard = (KlondikeCard) other;
    return this.suit.equals(otherCard.suit) && this.rank == otherCard.rank;
  }

  @Override
  public int hashCode() {
    return Objects.hash(rank, suit);
  }

  @Override
  public boolean isValidMoveCas(Card other) {
    KlondikeCard otherCard = (KlondikeCard) other;
    return Rank.getRank(otherCard.rank) - Rank.getRank(this.rank) == 1
            && this.isBlack() != other.isBlack();
  }

  @Override
  public boolean isValidMoveCasWh(Card other) {
    KlondikeCard otherCard = (KlondikeCard) other;
    return Rank.getRank(otherCard.rank) - Rank.getRank(this.rank) == 1
            && this.isBlack() == other.isBlack();
  }

  @Override
  public boolean isValidMoveFound(Card other) {
    KlondikeCard otherCard = (KlondikeCard) other;
    return Rank.getRank(this.rank) - Rank.getRank(otherCard.rank) == 1
            && this.suit == otherCard.suit;
  }

  @Override
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * Return the equivalent value of the given card's rank.
   * @param card the card to evaluate
   * @return the integer value of the given card
   */
  public static int rankToValue(Card card) {
    switch (((KlondikeCard) card).rank) {
      case ACE: return 1;
      case TWO: return 2;
      case THREE: return 3;
      case FOUR: return 4;
      case FIVE: return 5;
      case SIX: return 6;
      case SEVEN: return 7;
      case EIGHT: return 8;
      case NINE: return 9;
      case TEN: return 10;
      case JACK: return 11;
      case QUEEN: return 12;
      case KING: return 13;
      default:
        throw new IllegalArgumentException("Invalid card rank");
    }
  }

  public boolean isVisible() {
    return this.faceUp;
  }

  public void faceUp() {
    this.faceUp = true;
  }

  public void faceDown() {
    this.faceUp = false;
  }

  public boolean isBlack() {
    return this.suit.equals(Suit.SPADE) || this.suit.equals(Suit.CLUB);
  }
}
