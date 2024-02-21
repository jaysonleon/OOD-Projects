package provider.strategy;

import java.util.ArrayList;
import java.util.List;

import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * A Strategy that is used to prioritize moves that would allow the Player to capture a corner
 * tile.
 */
public class TakeCornerStrategy extends AbstractStrategy {
  /**
   * Determines if, in the next turn, a player could take a corner tile.
   *
   * @param model  the model to evaluate from
   * @param player the player to evaluate for
   * @return a list of potential corners to take in the next turn. If the list is empty then there
   *         are no corners to take in the next turn
   * @implNote in the future, this may be changed to order corners by how many potential
   *         points the player could gain by playing there
   */
  @Override
  public List<HexCoordinate> chooseHexCoord(ReadOnlyReversiModel model, PlayerSymbol player) {
    if (model.isGameOver()) {
      return new ArrayList<>();
    }

    List<HexCoordinate> cornerCoord = new ArrayList<>();
    if (!model.doesPlayerHaveMoves(player)) {
      return new ArrayList<>();
    }

    List<HexCoordinate> corners = getCorners(model);

    for (HexCoordinate c : corners) {
      if (model.moveValid(c, player)) {
        cornerCoord.add(c);
      }
    }

    return cornerCoord;
  }
}
