package provider.view;

import provider.model.HexCoordinate;
import provider.model.ReversiModel;

/**
 * An interface representing all the potential features that a game of Reversi needs to have when a
 * player interacts with it.
 */
public interface ReversiFeatures {
  /**
   * Notifies listeners when a disc should be placed on the board.
   *
   * @param location the location to place a disc at
   * @see ReversiModel#placeDisc(HexCoordinate)
   */
  void placeDisc(HexCoordinate location);

  /**
   * Notifies listeners to pass the current player's turn and move to the next player.
   *
   * @see ReversiModel#passTurn()
   */
  void passTurn();
}
