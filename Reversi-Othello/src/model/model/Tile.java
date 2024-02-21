package model.model;

import model.player.Player;

/**
 * Represents a Tile in a board used in a game of Reversi.
 */
public interface Tile {
  /**
   * Returns the q coordinate of the tile.
   *
   * @return the q coordinate of the tile
   */
  int getQ();

  /**
   * Returns the r coordinate of the tile.
   *
   * @return the r coordinate of the tile
   */
  int getR();

  /**
   * Returns the s coordinate of the tile (if it has one).
   *
   * @return the s coordinate of the tile
   * @throws UnsupportedOperationException if the tile does not have an s coordinate
   */
  int getS() throws UnsupportedOperationException;

  /**
   * Changes the player on the tile.
   *
   * @param player the player to change to
   */
  void changePlayer(Player player);

  /**
   * Places the given player on the tile if it is empty.
   *
   * @param player the player to place on the tile
   */
  void placePlayerEmpty(Player player);

  /**
   * Returns true if the tile has a player, false otherwise.
   */
  boolean hasPlayer();

  /**
   * Returns the player at the given tile, or {@code Player.EMPTY} if there is no player.
   *
   * @return the player at the given tile
   */
  Player getPlayerAt();

  /**
   * Returns true if the given object is a Tile with the same coordinates and player as this Tile.
   *
   * @param other the other object
   * @return true if the given object is a Tile with the same coordinates and player as this Tile
   */
  boolean equals(Object other);

  /**
   * Returns the hash code of this Tile.
   *
   * @return the hash code of this Tile
   */
  int hashCode();

  /**
   * Returns a string representation of this Tile.
   *
   * @return a string representation of this Tile
   */
  String toString();
}
