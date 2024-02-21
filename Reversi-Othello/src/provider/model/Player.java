package provider.model;

import provider.view.ReversiFeatures;

/**
 * An interface that represents the notifications a Reversi player can create.
 */
public interface Player {
  /**
   * Notifies this player's listeners that the player wants to place a disc.
   *
   * @param model a model to reference to make a move
   */
  void placeDisc(ReadOnlyReversiModel model);

  /**
   * Notifies this player's listeners that the player wants to pass their turn.
   *
   * @param model a model to reference to make a move
   */
  void passTurn(ReadOnlyReversiModel model);

  /**
   * Determines this player's symbol (either Black or White).
   *
   * @return this player's {@link PlayerSymbol}
   */
  PlayerSymbol getSymbol();

  /**
   * Registers the given listener for notifications from this player.
   *
   * @param listener the listener to register
   */
  void addFeaturesListener(ReversiFeatures listener);
}


