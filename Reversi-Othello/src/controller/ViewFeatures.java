package controller;

/**
 * Represents the features for the Reversi game.
 */
public interface ViewFeatures {
  /**
   * Passes the turn to the next player.
   */
  void passTurn();

  /**
   * Places a piece at the given coordinates.
   *
   */
  void placePiece();

  /**
   * Places a piece at the given coordinates. Used to help adapt provider's code to our code.
   *
   * @param q the q coordinate of the hex
   * @param r the r coordinate of the hex
   */
  void placePieceHelper(int q, int r);

  /**
   * Selects and highlights the hexagon at the given coordinates.
   */
  void selectHex();

  /**
   * Exits the program.
   */
  void exitProgram();
}
