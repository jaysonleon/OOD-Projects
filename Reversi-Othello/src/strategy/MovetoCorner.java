package strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;

/**
 * Represents a strategy for an AI player to choose the best available move, while prioritizing
 *  corners.
 */
public class MovetoCorner implements FallibleReversiStrategy {

  @Override
  public Optional<Point> chooseMove(ReadonlyReversiModel model, Player player)
          throws IllegalStateException {
    for (int r = 0; r < model.getHeight(); r++) {
      for (int q = 0; q < model.getWidth(); q++) {
        if (model.getTileAt(q, r) == null) {
          continue;
        }
        Point p = new Point(q, r);
        if (model.isValidMove(q, r, player) && getCorners(model).contains(p)) {

          return Optional.of(p);
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Gets the corners of the board.
   * @param model the model to get the corners of
   * @return the corners of the board
   */
  private static java.util.List<Point> getCorners(ReadonlyReversiModel model) {
    List<Point> corners = new ArrayList<>();
    corners.add(new Point(model.getSideLen() - 1, 0));
    corners.add(new Point(model.getWidth() - 1, 0));
    corners.add(new Point(0, model.getSideLen() - 1));
    corners.add(new Point(model.getWidth() - 1, model.getSideLen() - 1));
    corners.add(new Point(0, model.getHeight() - 1));
    corners.add(new Point(model.getSideLen() - 1, model.getHeight() - 1));
    return corners;
  }
}
