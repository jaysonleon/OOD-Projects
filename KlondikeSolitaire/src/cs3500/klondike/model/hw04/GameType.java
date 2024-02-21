package cs3500.klondike.model.hw04;

/**
 * Represents a gameType for Klondike Solitaire.
 */
public enum GameType {
  BASIC(), LIMITED(), WHITEHEAD();

  /**
   * Constructor for a gameType.
   */
  GameType() {
  }

  /**
   * Return the associated gameType to the given name.
   * @param name a string representation of a gameType.
   * @return the gameType associated with the given name.
   */
  public static GameType toGameType(String name) {
    switch (name) {
      case "basic":
        return BASIC;
      case "limited":
        return LIMITED;
      case "whitehead":
        return WHITEHEAD;
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }
}
