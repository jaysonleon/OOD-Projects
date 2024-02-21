package provider.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * A Strategy that finds all the valid moves a player could take and removes any tiles that border a
 * corner tile.
 */
public class AvoidHexNearCornersStrategy extends AbstractStrategy {
  /**
   * Finds the best moves to make for a player and does not consider any move that borders a corner
   * tile.
   *
   * @param model  the model to evaluate from
   * @param player the player to evaluate for
   * @return either an empty list, meaning there are no moves that can be made that don't border a
   *         corner tile or the list of moves that are not bordering a corner
   */
  @Override
  public List<HexCoordinate> chooseHexCoord(ReadOnlyReversiModel model, PlayerSymbol player) {
    if (model.isGameOver()) {
      return new ArrayList<>();
    }
    if (!model.doesPlayerHaveMoves(player)) {
      return new ArrayList<>();
    }

    List<HexCoordinate> corners = getCorners(model);
    List<HexCoordinate> neighborsToAvoid = new ArrayList<>();

    for (HexCoordinate c : corners) {
      List<HexCoordinate> validNeighbors =
              model.getNeighbors(c).stream().filter(l -> model.moveValid(l, player))
                   .collect(Collectors.toList());

      neighborsToAvoid.addAll(validNeighbors);
    }

    Map<HexCoordinate, Integer> potentialMoves = getAllValidMovesByValue(model, player);

    List<HexCoordinate> sortedByValue = potentialMoves.entrySet().stream()
                                                      .sorted(Comparator.comparingInt(
                                                              Map.Entry::getValue))
                                                      .map(Map.Entry::getKey)
                                                      .collect(Collectors.toList());
    Collections.reverse(sortedByValue);
    return sortedByValue.stream().filter(c -> !neighborsToAvoid.contains(c)).limit(1)
                        .collect(Collectors.toList());
  }
}
