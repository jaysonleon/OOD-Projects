package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Factory class for creating KlondikeModels.
 */
public class KlondikeCreator {

  /**
   * Create a KlondikeModel based on the given type.
   * @param type the type of game to create
   * @return an instance of the created KlondikeModel
   */
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(2);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        throw new IllegalArgumentException("Invalid game type");
    }
  }
}
