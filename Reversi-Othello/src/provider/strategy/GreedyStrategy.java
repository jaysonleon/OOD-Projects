package provider.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * A class representing a greedy strategy, prioritizing the move that captures the most amount of
 * discs within one turn for the player.
 */
public class GreedyStrategy extends AbstractStrategy {

  /**
   * A method that returns a list of the highest value locations to move to. It is ordered in
   * decreasing order of potential score gain within the turn, then to break ties the coordinates
   * are ordered by topmost and leftmost location.
   *
   * @param model the model to evaluate from
   * @param player the player to evaluate for
   * @return a list of potential moves the player can make in order of score, then by topmost and
   *         leftmost location. If it's an empty list, then this strategy has found no moves to
   *         make.
   */
  @Override
  public List<HexCoordinate> chooseHexCoord(ReadOnlyReversiModel model, PlayerSymbol player) {
    if (model.isGameOver()) {
      return new ArrayList<>();
    }

    if (!model.doesPlayerHaveMoves(player)) {
      return new ArrayList<>();
    }
    // all potential moves the player could make
    Map<HexCoordinate, Integer> potentialMoves = getAllValidMovesByValue(model, player);

    // the maximum increase in score from one move
    int maxValue = potentialMoves.values().stream().max(Comparator.naturalOrder()).get();

    List<HexCoordinate> bestMoves =
            potentialMoves.entrySet().stream().filter(s -> s.getValue().equals(maxValue)).map(
                    Map.Entry::getKey).collect(Collectors.toList());

    // if there are multiple moves that get the best possible score increase, break ties by
    // topmost leftmost
    if (bestMoves.size() > 1) {
      HexCoordinate tieBrokenBest = bestMoves.get(1);
      for (HexCoordinate m : bestMoves) {
        if (m.s == tieBrokenBest.s) {
          if (m.r < tieBrokenBest.r) {
            tieBrokenBest = m;
          }
        } else if (m.r == tieBrokenBest.r) {
          if (m.s > tieBrokenBest.s) {
            tieBrokenBest = m;
          }
        }
      }

      return List.of(tieBrokenBest);

    } else {
      return List.of(bestMoves.get(0));
    }
  }
}
