package model.model;


import model.player.Player;

/**
 * Represents the basic interface for the game of Reversi. It includes methods for initializing
 *  the game, making moves, checking the game state, and getting the winner of the game.
 */
public interface ReversiModel extends ReadonlyReversiModel {
  /**
   * Starts the game, builds the game board using the default board size, 11x11,
   * unless specified by the user. Places pieces for initial game state.
   *
   * @throws IllegalStateException if the game has already started
   */
  void startGame();

  /**
   * Play a move for the current player. A move is valid for playerB if the move being played in is
   * adjacent (in at least one direction) to a straight line of the opponent playerW's disks, at the
   * far end of which is another playerB disk.
   * The result of a legal move is that all of playerW's discs in all directions are sandwiched
   * between two discs of playerB get flipped to playerA (playerB 'captures' playerW's discs).
   *
   * @param q the y coordinate, or the column
   * @param r the x coordinate, or the row
   * @throws IllegalStateException    if {@code getStatus() != Status.Playing}
   * @throws IllegalArgumentException if the coordinates are not valid,
   *      or the hex is already occupied
   * @throws IllegalStateException    if the move is not valid
   */
  void playMove(int q, int r);

  /**
   * Pass a turn to the other player (skips your turn).
   *
   * @throws IllegalStateException if {@code getStatus() != Status.Playing}
   */
  void pass();

  /**
   * Returns true if the game is over, false otherwise.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Add the given features to this model.
   * @param features the features to add
   */
  void addFeatures(ModelFeatures features);

  /**
   * Notify the player that it is their turn.
   * @param p the player whose turn it is
   */
  void notifyPlayerTurn(Player p);
}