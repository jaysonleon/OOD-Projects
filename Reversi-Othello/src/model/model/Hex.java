package model.model;

import java.util.Objects;

import model.player.Player;

/**
 * Represents a hexagonal cell in the board. Represented by a q, r, s coordinate system. The
 * coordinate system is based on the axial coordinate system, where q is the column, r is the row,
 * and s is the diagonal. The state of the hex is either empty, or occupied by a player.
 */
public class Hex implements Tile {

  // the x position of the hex on the board
  private final int q;
  // the y position of the hex on the board
  private final int r;
  // the z position of the hex on the board
  // INVARIANT 1: s = -q - r
  // this is maintained by the methods since the q,r,s fields are final
  //  and do not change after instantiation
  private final int s;
  // the state of the hex, the state is either Empty, or occupied by a Player
  private Player state;

  /**
   * Verifies coordinates of Hex, construct Hex object with default empty state.
   */
  public Hex(int q, int r) {
    this.q = q;
    this.r = r;
    this.s = -q - r;
    this.state = Player.EMPTY;
  }

  // getters
  public int getQ() {
    return this.q;
  }

  public int getR() {
    return this.r;
  }

  public int getS() {
    return this.s;
  }

  /**
   * Changes player on a hex.
   *
   * @param player player to change on hex
   * @throws IllegalArgumentException if player is not valid
   */
  public void changePlayer(Player player) {
    if (player == this.state) {
      return;
    }
    this.state = player;
  }

  /**
   * Places player on an empty hex.
   *
   * @param player player to place on empty hex
   * @throws IllegalStateException if hex is already occupied
   */
  public void placePlayerEmpty(Player player) {
    if (this.state != Player.EMPTY) {
      throw new IllegalStateException("Hex is already occupied");
    }

    this.state = player;
  }

  /**
   * Returns true if the hex has a player, false otherwise.
   *
   * @return true if the hex has a player, false otherwise
   */
  public boolean hasPlayer() {
    return this.state != Player.EMPTY;
  }

  /**
   * Returns the player at the given hex, or {@code Player.EMPTY} if there is no player.
   *
   * @return the player at given hex, or empty if there is no player
   */
  public Player getPlayerAt() {
    switch (this.state) {
      case BLACK:
        return Player.BLACK;
      case WHITE:
        return Player.WHITE;
      default:
        return Player.EMPTY;
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Hex)) {
      return false;
    }
    Hex otherHex = (Hex) other;
    return ((this.q == otherHex.q
            && this.r == otherHex.r
            && this.s == otherHex.s)
            && this.state == otherHex.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.q, this.r, this.s, this.state);
  }

  @Override
  public String toString() {
    return this.state.toString();
  }
}