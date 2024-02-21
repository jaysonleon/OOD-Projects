package strategy;

import java.awt.Point;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;

/**
 * Represents a strategy for an AI player to choose the best available move, that combines two
 *  fallible strategies.
 */
public class CombinedStrategy implements FallibleReversiStrategy {

  private final FallibleReversiStrategy first;
  private final FallibleReversiStrategy second;

  /**
   * Constructs a CombinedStrategy.
   * @param first the first strategy to try
   * @param second the second strategy to try
   */
  public CombinedStrategy(FallibleReversiStrategy first, FallibleReversiStrategy second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public Optional<Point> chooseMove(ReadonlyReversiModel model, Player player) {
    Optional<Point> ans = this.first.chooseMove(model, player);
    if (ans.isPresent()) {
      return ans;
    }
    return this.second.chooseMove(model, player);
  }
}
