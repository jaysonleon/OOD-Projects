package model.model;

/**
 * Represents a game of Reversi, played on a regular hexagonal board.
 * It contains methods for initializing the game, making moves, and checking the gameState.
 */
public class HexReversi extends AbstractReversi {
  /**
   * Default constructor, creates a 11x11 board, with sideLength = 6.
   */
  public HexReversi() {
    super(DEFAULT_HEX_SIDE_LEN, true);
  }

  /**
   * Constructor that takes in a sideLength, creates a board with dim = sideLength.
   *
   * @param sideLength length of one side of the board
   */
  public HexReversi(int sideLength) {
    super(sideLength, true);
  }
}
