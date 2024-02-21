package provider.strategy;

import java.util.List;

import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * A function interface that describes how a strategy should be implemented.
 */
public interface ReversiStrategy {
  /**
   * Determines the best tile to place a disc for the given player based on the implementation's
   * strategy.
   *
   * @param model  the model to evaluate from
   * @param player the player to evaluate for
   * @return either a list of location to place a disc for the next move of the given player or an
   *         empty list, meaning this strategy could not find any place to play next (out of moves
   *         or nothing that satisfied the conditions of the implementation)
   */
  List<HexCoordinate> chooseHexCoord(ReadOnlyReversiModel model, PlayerSymbol player);
}
