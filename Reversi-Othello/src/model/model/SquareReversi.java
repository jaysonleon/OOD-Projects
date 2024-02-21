package model.model;

/**
 * Represents a game of Reversi, played on a regular square board.
 * It contains methods for initializing the game, making moves, and checking the gameState.
 */
public class SquareReversi extends AbstractReversi {
  /**
   * Default constructor, creates a 8x8 board, with sideLength = 8.
   */
  public SquareReversi() {
    super(DEFAULT_SQUARE_SIDE_LEN, false);
  }

  /**
   * Constructor that takes in a sideLength, creates a board with dim = sideLength.
   *
   * @param sideLength length of one side of the board
   */
  public SquareReversi(int sideLength) {
    super(sideLength, false);
  }
}
