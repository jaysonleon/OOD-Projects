package provider.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * An abstract class for ReversiStrategies that contains useful helpers for all strategies.
 */
abstract class AbstractStrategy implements ReversiStrategy {
  /**
   * A helper method that chooses the topmost-leftmost HexCoordinate from a given list. This methods
   * priority goes in this order from highest to lowest priority:
   *
   * <p>- Leftmost (meaning the maximum s value)
   *
   * <p>- Topmost (meaning the minimum r value)
   *
   * @param coords the list of HexCoordinates to filter down to the topmost-leftmost coordinate
   * @return the topmost-leftmost HexCoordinate within this list
   * @throws IllegalArgumentException if given an empty coords list
   */
  protected HexCoordinate chooseTopmostLeftmost(List<HexCoordinate> coords)
          throws IllegalArgumentException {
    if (coords.isEmpty()) {
      throw new IllegalArgumentException("Cannot find a topmost leftmost tile in an empty list");
    }

    return coords.stream().max((a, b) -> {
      if (a.s == b.s) {
        return b.r - a.r;
      } else {
        return a.s - b.s;
      }
    }).get();
  }

  /**
   * Finds all the moves that a player can make and their associated score increases.
   *
   * @param model  the model to evaluate from
   * @param player the player to find potential moves for
   * @return a map between locations of moves to make and their associated increases with score
   */
  protected Map<HexCoordinate, Integer> getAllValidMovesByValue(
          ReadOnlyReversiModel model, PlayerSymbol player) {
    Map<HexCoordinate, Integer> possibleCoordsToScore = new HashMap<>();

    // loops through all spots on the board to find useful spots
    for (int q = -model.getBoardSize(); q <= model.getBoardSize(); q++) {
      int startingR = Math.max(-model.getBoardSize(), -q - model.getBoardSize());
      int endingR = Math.min(model.getBoardSize(), -q + model.getBoardSize());
      for (int r = startingR; r <= endingR; r++) {

        HexCoordinate curCoord = new HexCoordinate(q, r, -q - r);
        if (!model.moveValid(curCoord, player)) {
          continue;
        }

        int discsGained = model.potentialDiscsToGain(curCoord, player);

        // if there are no discs to change at a location, it is invalid to play there
        if (discsGained > 0) {
          possibleCoordsToScore.put(curCoord, discsGained);
        }
      }
    }

    return possibleCoordsToScore;
  }

  /**
   * Finds all the corners of the board associated with the given model.
   *
   * @param model the model to evaluate from
   * @return a list of HexCoordinates that represent the six corners of the board of the given
   *         model
   */
  protected List<HexCoordinate> getCorners(ReadOnlyReversiModel model) {
    List<HexCoordinate> corners = new ArrayList<>();
    List<Integer> coordValues = Arrays.asList(model.getBoardSize(), -model.getBoardSize(), 0);
    // this essentially finds all valid permutations of the three different coordinate numbers
    // used to find corners on the board.
    for (int axis = 0; axis < 3; axis++) {
      corners.add(new HexCoordinate(coordValues.get(0), coordValues.get(1), coordValues.get(2)));
      corners.add(new HexCoordinate(coordValues.get(1), coordValues.get(0), coordValues.get(2)));
      // logically shifts the coordinateValues list to get permutations of coordinates
      Collections.rotate(coordValues, -1);
    }

    return corners;
  }
}