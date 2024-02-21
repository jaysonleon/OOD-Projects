package provider.model;

/**
 * All possible notifications a Reversi model can produce for listeners.
 */
public interface ModelNotifications {
  /**
   * Notifies listeners that any update has occurred and to refresh its data. This includes any
   * disc placements, turn changes, game status updates, etc.
   */
  void updateOccurred();

  /**
   * This should only be called on the listeners for the player whose turn it currently is as it
   * will execute a move on the board for an AI player.
   */
  void notifyMakeMove();

  /**
   * Notifies listeners that the turn on the game has changed.
   *
   * @param currentTurn the symbol of the player whose turn it is
   */
  void notifyTurnChanged(PlayerSymbol currentTurn);

  /**
   * Notifies listeners that the game has ended.
   *
   * @param winner the symbol of the winner of the game. If {@code null}, then it is a tie
   */
  void gameOver(PlayerSymbol winner);
}
