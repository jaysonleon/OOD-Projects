package cs3500.klondike.model.hw02;

/**
 * Represents a suit in the game of cs3500.klondike.Klondike Solitaire.
 */
public enum Suit {
  CLUB("♣"), DIAMOND("♢"), HEART("♡"), SPADE("♠");

  private final String suit;

  Suit(String suit) {
    this.suit = suit;
  }

  @Override
  public String toString() {
    return this.suit;
  }
}
