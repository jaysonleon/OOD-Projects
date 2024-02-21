package provider.model;

/**
 * An interface to represent the functionality of a model of any Reversi game.
 */
public interface ReversiModel extends ReadOnlyReversiModel {
  /**
   * Initializes this game and notifies the players to begin play.
   */
  void startGame();

  /**
   * Places a disc on the given location for the current player's turn. After placing the disc, it
   * changes all affected discs to the current player's color, then changes the turn to the other
   * player.
   *
   * @param location where to place the next disc
   * @throws IllegalStateException    if the game is over or the move is invalid
   * @throws IllegalArgumentException if the given location is not on the board
   */
  void placeDisc(HexCoordinate location) throws IllegalStateException, IllegalArgumentException,
          IllegalMoveException;

  /**
   * Flips the current turn to the other player with no side effects.
   *
   * @throws IllegalStateException if the game is not in progress
   */
  void passTurn() throws IllegalStateException;

  /**
   * Registers the given listener for notifications from the White player.
   *
   * @param listener the listener to register
   */
  void addWhiteNotificationListener(ModelNotifications listener);

  /**
   * Registers the given listener for notifications from the Black player.
   *
   * @param listener the listener to register
   */
  void addBlackNotificationListener(ModelNotifications listener);
}
