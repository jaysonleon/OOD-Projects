package model.model;

import model.player.Player;

/**
 * Represents a read-only version of the Reversi game.
 */
public interface ReadonlyReversiModel {
  // the default sideLength of the game -> will give a 11x11 board
  int DEFAULT_HEX_SIDE_LEN = 6;
  int DEFAULT_SQUARE_SIDE_LEN = 8;

  int[][] HEX_DIRECTIONS = {
          {1, 0}, {1, -1}, {0, -1}, {-1, 0}, {-1, 1}, {0, 1}
  };

  int[][] SQUARE_DIRECTIONS = {
          {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1,1}
  };

  /**
   * Returns the height of the board.
   *
   * @return the height of the board
   * @throws IllegalStateException if {@code getStatus() != Status.Playing}
   */
  int getHeight();

  /**
   * Returns the width of the board.
   *
   * @return the width of the board
   * @throws IllegalStateException if {@code getStatus() != Status.Playing}
   */
  int getWidth();

  /**
   * Returns the shortest side length of the board.
   *
   * @return the side length of the board
   */
  int getSideLen();

  /**
   * Returns the status of the game.
   *
   * @return the status of the game
   */
  Status getStatus();

  /**
   * Returns the current board state.
   *
   * @return the current board state
   */
  Board getBoard();

  /**
   * Returns the score for the given player.
   *
   * @param player the player
   * @return the score for the given player
   */
  int getScore(Player player);

  /**
   * Returns the player whose turn it is.
   * @return the player whose turn it is
   */
  Player getTurn();

  /**
   * The game can either be in progress(Playing) or it can be over with(Won) or without a
   * winner(Stalemate).
   */

  enum Status { Playing, Over }

  /**
   * Return the hex at the given coordinates.
   * or {@code null} if there is no hex at the given coordinate
   *
   * @return the hex at the given coordinates
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  Tile getTileAt(int q, int r);

  /**
   * Return the next player's turn.
   *
   * @return the next player's turn
   */
  Player nextTurn();

  /**
   * Determines which player is victorious. A player wins if they have more discs that their
   * opponent.
   *
   * @return the winner, or {@code Player.EMPTY} if there is a tie
   * @throws IllegalStateException if {@code getStatus() == null / Status.Playing}
   */
  Player determineWinner();

  /**
   * Make a deep copy of the current board state.
   *
   * @return a deep copy of the current board state
   */
  Board copyBoard();

  /**
   * Returns true if the game is over, false otherwise.
   * @param q the q coordinate (row)
   * @param r the r coordinate (column)
   * @param player the player
   * @return true if the game is over, false otherwise
   */
  boolean isValidMove(int q, int r, Player player);

  /**
   * Returns the score of the given move for the current player. The 'score' of the move is the
   *  number of hexes that will be added for the current player.
   * @param q the q coordinate (row)
   * @param r the r coordinate (column)
   * @return the score of the given move for the current player
   */
  int moveScore(int q, int r);

  /**
   * Returns true if the given player has any valid moves left in the game.
   *
   * @param player the player
   * @return true if the given player has any valid moves left in the game
   */
  boolean hasValidMoves(Player player);

  /**
   * Returns true if this game of reversi is hex-based or not.
   * @return true if this game of reversi is hex-based
   */
  boolean isHex();
}
