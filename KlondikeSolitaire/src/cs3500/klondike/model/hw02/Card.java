package cs3500.klondike.model.hw02;

/**
 * Represents a playing card.
 */
public interface Card {

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   * @return the formatted card
   */
  String toString();

  /**
   * Does this card have the same rank and suit as the given card.
   * @param other the card to compare to this card
   * @return true iff this card and the given card have matching suits and ranks
   */
  boolean equals(Object other);

  /**
   * Is moving this card to the given card a valid move in the cascade. A valid move
   *  in the cascade is when the card being moved's rank is 1 less than the card being moved
   *  onto, AND the suits are different colors.
   * @param other the destination card that this card is being moved to
   * @return true iff the move is valid
   */
  boolean isValidMoveCas(Card other);

  /**
   * Is moving this card to the given card a valid move in the cascade, in whitehead solitaire.
   *  A valid move is when the colors of the cards are the same, AND the rank of the card being
   *  moved is 1 less than the rank of the card being moved onto.
   * @param other the destination card that this card is being moved to
   * @return true iff the move is valid
   */
  boolean isValidMoveCasWh(Card other);

  /**
   * Is moving this card to the given card a valid move in the foundation. A valid move
   *  in the foundation is when the suits are the same, and this card's rank is exactly one more
   *  than the given card's rank.
   * @param other the current topMost card of the foundation
   * @return true iff the move is valid
   */
  boolean isValidMoveFound(Card other);

  /**
   * Get the suit of this card.
   * @return the suit of this card
   */
  Suit getSuit();

  /**
   * Return if the card is currently faceUp or faceDown in the game.
   * @return if the card is visible in the game
   */
  boolean isVisible();

  void faceUp();

  void faceDown();

  /**
   * Is this card black, or red. A black card is either a spade or a club.
   * @return true iff this card is black
   */
  boolean isBlack();
}
