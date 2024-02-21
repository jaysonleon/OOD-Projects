package model.model;

/**
 * Represents a board for a game of Reversi.
 * It contains methods for initializing the board, making moves, and checking the gameState.
 */
public interface Board {

  /**
   * Returns the height of the board.
   *
   * @return the height of the board
   */
  int getHeight();

  /**
   * Returns the width of the board.
   *
   * @return the width of the board
   */
  int getWidth();

  /**
   * Returns the shortest side length of the board.
   *
   * @return the side length of the board
   */
  int getDim();

  /**
   * Returns the tile at the given coordinates.
   *
   * @param q the q coordinate
   * @param r the r coordinate
   * @return the tile at the given coordinates
   */
  Tile getTileAt(int q, int r);

  /**
   * Returns true if the given object is a Board with the same dimensions and tiles as this Board.
   *
   * @param other the other object
   * @return true if the given object is a Board with the same dimensions and tiles as this Board
   */
  boolean equals(Object other);

  /**
   * Returns the hash code of this Board.
   *
   * @return the hash code of this Board
   */
  int hashCode();

  /**
   * Returns a string representation of this Board.
   *
   * @return a string representation of this Board
   */
  String toString();

  /**
   * Sets the tile at the given coordinates to the given tile.
   *
   * @param q the q coordinate
   * @param r the r coordinate
   */
  void setTileAt(int q, int r);

  /**
   * Adds the starting cells to the board.
   *
   * @param dim        the dimension of the board
   * @param sideLength the side length of the board
   */
  void addStartingCells(int dim, int sideLength);
}
