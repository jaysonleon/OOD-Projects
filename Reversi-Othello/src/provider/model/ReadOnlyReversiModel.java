package provider.model;

import java.util.List;
import java.util.Optional;

/**
 * An interface that contains the functionality of a Reversi Model that cannot be mutated. Only
 * contains observation methods.
 */
public interface ReadOnlyReversiModel {
  /**
   * Returns which player's tile is on the given location.
   *
   * @param location A coordinate on the board to look at
   * @return either empty (representing a blank tile) or the PlayerSymbol of the disc on the given
   *         location
   * @throws IllegalArgumentException if the given location is not on the board
   */
  Optional<PlayerSymbol> getPlayerOnTile(HexCoordinate location)
          throws IllegalArgumentException;

  /**
   * Returns which player's turn it currently is.
   *
   * @return which player's turn it is
   * @throws IllegalStateException if the game is over
   */
  PlayerSymbol getTurn() throws IllegalStateException;

  /**
   * Determines if there can be any more moves played by either player.
   *
   * @return if the game is over
   */
  boolean isGameOver();

  /**
   * Finds the size of this board. Size is defined as the number of tiles from the left or right of
   * the center of the board to the left or right edge.
   *
   * @return the radius of the board
   */
  int getBoardSize();

  /**
   * Determines if a move is valid at the given coordinates for the player on the current turn.
   *
   * @param location the coordinates to check the validity of a move for
   * @param player   the player symbol to check if the given move is valid
   * @return whether the given coordinates are a valid move for the player on the current turn
   * @throws IllegalArgumentException if the given coordinates are outside of the board
   * @throws IllegalStateException    if the game is over
   */
  boolean moveValid(HexCoordinate location, PlayerSymbol player)
          throws IllegalArgumentException;

  /**
   * Determines the number of discs gained if the given player were to place a disc at the given
   * location.
   *
   * @param location hypothetical location for the given player to place a disc
   * @param player   the player looking to place a disc
   * @return the number of discs the given player would gain
   */
  int potentialDiscsToGain(HexCoordinate location, PlayerSymbol player);

  /**
   * A player's score is equal to the number of discs they have on the board.
   *
   * @param player the player to get the score of
   * @return the score of the given player
   */
  int getScore(PlayerSymbol player);

  /**
   * Determines if a player has any legal moves to take.
   *
   * @param player the player to check moves for
   * @return whether the given player can make any additional moves in this turn or not
   */
  boolean doesPlayerHaveMoves(PlayerSymbol player);

  /**
   * Returns the locations of the up to 6 neighbors a single tile can have. Neighbors are defined as
   * tiles that share an edge.
   *
   * @param location the location of the hexagon to check neighbors for
   * @return the locations of the tiles that are neighbors of the given tile
   */
  List<HexCoordinate> getNeighbors(HexCoordinate location);
}
