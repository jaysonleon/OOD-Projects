package strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;

/**
 * Represents a strategy for an AI player to choose the best available move, while prioritizing
 *  moves that are not next to a corner.
 */
public class AvoidNextToCorner implements FallibleReversiStrategy {
  // The six directions to check for next to corner
  private static final int[][] DIRECTIONS = {
          {1, 0}, {1, -1}, {0, -1}, {-1, 0}, {-1, 1}, {0, 1}
  };

  @Override
  public Optional<Point> chooseMove(ReadonlyReversiModel model, Player player) {
    for (int r = 0; r < model.getHeight(); r++) {
      for (int q = 0; q < model.getWidth(); q++) {
        if (model.getTileAt(q, r) == null) {
          continue;
        }
        if (model.isValidMove(q, r, player) && !isNextToCorner(q, r, model)) {
          return Optional.of(new Point(q, r));
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Checks if the given point is next to a corner.
   * @param x the x coordinate of the point
   * @param y the y coordinate of the point
   * @param model the model to check
   * @return true if the point is next to a corner, false otherwise
   */
  private boolean isNextToCorner(int x, int y, ReadonlyReversiModel model) {
    List<Point> corners = getCorners(model);
    List<Point> nextToCorners = new ArrayList<>();
    for (int[] direction : DIRECTIONS) {
      int dx = direction[0];
      int dy = direction[1];
      for (Point corner : corners) {
        Point nextToCorner = new Point(corner.x + dx, corner.y + dy);
        if (nextToCorner.x + nextToCorner.y < model.getWidth() - model.getSideLen()
                || nextToCorner.x + nextToCorner.y > model.getWidth() + model.getSideLen() - 2
                || nextToCorner.x < 0 || nextToCorner.y < 0
                || nextToCorner.x >= model.getWidth()
                || nextToCorner.y >= model.getHeight()) {
          continue;
        }
        nextToCorners.add(nextToCorner);
      }
    }
    for (Point nextToCorner : nextToCorners) {
      if (nextToCorner.x == x && nextToCorner.y == y) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the corners of the board.
   * @param model the model to get the corners of
   * @return the corners of the board
   */
  private static List<Point> getCorners(ReadonlyReversiModel model) {
    Point top_left_corner = new Point(model.getSideLen() - 1, 0);
    Point top_right_corner = new Point(model.getWidth() - 1, 0);
    Point mid_left_corner = new Point(0, model.getSideLen() - 1);
    Point mid_right_corner = new Point(model.getWidth() - 1, model.getSideLen() - 1);
    Point bot_left_corner = new Point(0, model.getHeight() - 1);
    Point bot_right_corner = new Point(model.getSideLen() - 1, model.getHeight() - 1);
    List<Point> corners = new ArrayList<>();
    corners.add(top_left_corner);
    corners.add(top_right_corner);
    corners.add(mid_left_corner);
    corners.add(mid_right_corner);
    corners.add(bot_left_corner);
    corners.add(bot_right_corner);
    return corners;
  }
}
