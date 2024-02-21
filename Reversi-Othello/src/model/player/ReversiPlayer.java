package model.player;

/**
 * Represents a player in the game of Reversi.
 */
public interface ReversiPlayer {
  /**
   * Make a move for this player, at the given coordinates.
   * @param row the row of the move
   * @param col the column of the move
   */
  void makeMove(int row, int col);

  void makeMove();

  /**
   * Pass the turn for this player.
   */
  void pass();

  void addFeatures(PlayerFeatures f);

  Player getPiece();

  boolean isHuman();
}
