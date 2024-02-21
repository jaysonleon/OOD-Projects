package provider.strategy;

import java.util.ArrayList;
import java.util.List;

import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * A strategy that composes a list of other strategies to find the best possible move.
 */
public class CombinedStrategy extends AbstractStrategy {
  /**
   * A list of strategies that are evaluated in the given order (to give higher priority to certain
   * strategies).
   */
  private final List<ReversiStrategy> strategies;

  public CombinedStrategy(List<ReversiStrategy> strategies) {
    this.strategies = strategies;
  }

  /**
   * Tries to find a valid move from strategies in the order they are given in the list of this
   * strategy's list of strategies.
   *
   * @param model  the model to evaluate from
   * @param player the player to evaluate for
   * @return either an empty list, meaning no valid moves were found for this combined strategy, or
   *         the list of moves returned by the first valid strategy
   */
  @Override
  public List<HexCoordinate> chooseHexCoord(ReadOnlyReversiModel model, PlayerSymbol player) {
    for (ReversiStrategy strategy : this.strategies) {
      if (!strategy.chooseHexCoord(model, player).isEmpty()) {
        return strategy.chooseHexCoord(model, player);
      }
    }

    return new ArrayList<>();
  }
}
