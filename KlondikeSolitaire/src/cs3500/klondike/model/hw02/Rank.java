package cs3500.klondike.model.hw02;

/**
 * Represents the rank of a card in the game of cs3500.klondike.Klondike Solitaire.
 */
public enum Rank {
  ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"),
  EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K");
  private final String rank;

  Rank(String rank) {
    this.rank = rank;
  }

  @Override
  public String toString() {
    return this.rank;
  }

  /**
   * Return the associated int value of the given rank.
   * @param r the rank to evaluate
   * @return the equivalent integer value for the rank of this card
   */
  public static int getRank(Rank r) {
    switch (r) {
      case ACE : return 1;
      case TWO : return 2;
      case THREE : return 3;
      case FOUR : return 4;
      case FIVE: return 5;
      case SIX : return 6;
      case SEVEN : return 7;
      case EIGHT : return 8;
      case NINE : return 9;
      case TEN : return 10;
      case JACK : return 11;
      case QUEEN : return 12;
      case KING : return 13;
      default:
        throw new IllegalArgumentException("Invalid rank");
    }
  }

  /**
   * Convert the given int to the corresponding rank.
   * @param i the int to convert
   * @return the rank corresponding to the given int
   */
  public static Rank convertInt(int i) {
    switch (i) {
      case 1 : return ACE;
      case 2 : return TWO;
      case 3 : return THREE;
      case 4 : return FOUR;
      case 5 : return FIVE;
      case 6 : return SIX;
      case 7 : return SEVEN;
      case 8 : return EIGHT;
      case 9 : return NINE;
      case 10 : return TEN;
      case 11 : return JACK;
      case 12 : return QUEEN;
      case 13 : return KING;
      default:
        throw new IllegalArgumentException("Invalid rank");
    }
  }
}
