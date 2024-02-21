package strategy;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;

/**
 * Represents a strategy for an AI player to choose the best available move
 * that captures as many pieces on this turn as possible.
 * Ties are broken by choosing the top-left most hex that is a valid move.
 */
public class CaptureMax implements FallibleReversiStrategy {
  @Override
  public Optional<Point> chooseMove(ReadonlyReversiModel model, Player player) {
    List<Point> moves = new ArrayList<>();
    for (int r = 0; r < model.getHeight(); r++) {
      for (int q = 0; q < model.getWidth(); q++) {
        if (model.getTileAt(q, r) == null) {
          continue;
        }
        if (model.isValidMove(q, r, player)) {
          moves.add(new Point(q, r));
        }
      }
    }

    if (moves.isEmpty()) {
      return Optional.empty();
    }
    Point bestMove = moves.get(0);
    int bestScore = 0;
    for (Point p : moves) {
      int score = model.moveScore(p.x, p.y);

      if (score > bestScore) {
        bestScore = score;
        bestMove = p;
      }

    }
    return Optional.of(bestMove);
  }
}
