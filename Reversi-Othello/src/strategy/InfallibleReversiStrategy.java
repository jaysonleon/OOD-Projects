package strategy;

import java.awt.Point;

import model.player.Player;
import model.model.ReadonlyReversiModel;

/**
 * Represents a strategy for an AI player to choose a move in a Reversi game.
 * An interface describing strategies whose return value cannot fail:
 * they will always return a non-null Point, or else
 * throw an exception if they're called on a game that cannot have a move
 */
public interface InfallibleReversiStrategy {
  /**
   * Chooses a move for the given player in the given model.
   * @param model the model
   * @param player the player
   * @return a Point with the q and r coordinates of the determined move
   * @throws IllegalStateException if there are no valid moves for the given player
   */
  Point chooseMove(ReadonlyReversiModel model, Player player) throws IllegalStateException;

}
