package model.player;

/**
 * Represents the features that the player can call on the model.
 */
public interface PlayerFeatures {
  /**
   * Make a move for this player, at the given coordinates.
   * @param row the row of the move
   * @param col the column of the move
   */
  void makeMove(int row, int col);

  /**
   * Pass the turn for this player.
   */
  void pass();
}
