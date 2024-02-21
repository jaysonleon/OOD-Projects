package strategy;

import java.awt.Point;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;

/**
 * Represents a strategy for an AI player to choose the best available move, that may fail.
 */
public interface FallibleReversiStrategy {
  /**
   * Chooses a move for the given player in the given model.
   * @param model the model
   * @param player the player
   * @return a Point with the q and r coordinates of the determined move
   */
  Optional<Point> chooseMove(ReadonlyReversiModel model, Player player);

}
