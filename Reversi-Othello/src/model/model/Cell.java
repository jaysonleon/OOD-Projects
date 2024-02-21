package model.model;

import model.player.Player;

/**
 * Represents a SquareCell in a Square board used in a game of Reversi.
 */
public class Cell implements Tile {
  private final int x;
  private final int y;
  private Player state;

  /**
   * Constructs a SquareCell object with the given x and y coordinates.
   *
   * @param x x coordinate of the cell
   * @param y y coordinate of the cell
   */
  public Cell(int x, int y) {
    this.x = x;
    this.y = y;
    this.state = Player.EMPTY;
  }

  /**
   * Convenience constructor for testing purposes.
   *
   * @param x     x coordinate of the cell
   * @param y     y coordinate of the cell
   * @param state state of the cell
   */
  public Cell(int x, int y, Player state) {
    this.x = x;
    this.y = y;
    this.state = state;
  }

  @Override
  public int getQ() {
    return this.x;
  }

  @Override
  public int getR() {
    return this.y;
  }

  @Override
  public int getS() {
    throw new UnsupportedOperationException("cell does not have S coordinate");
  }

  @Override
  public void changePlayer(Player player) {
    if (player == this.state) {
      return;
    }
    this.state = player;
  }

  @Override
  public void placePlayerEmpty(Player player) {
    if (this.state != Player.EMPTY) {
      throw new IllegalStateException("Cell is not empty");
    }

    this.state = player;
  }

  @Override
  public boolean hasPlayer() {
    return this.state != Player.EMPTY;
  }

  @Override
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
    if (!(other instanceof Cell)) {
      return false;
    }
    Cell otherCell = (Cell) other;
    return (this.x == otherCell.x && this.y == otherCell.y) && this.state == otherCell.state;
  }

  @Override
  public int hashCode() {
    return this.x * 31 + this.y * 31 + this.hashCodeHelper(this.state);
  }

  private int hashCodeHelper(Player p) {
    switch (p) {
      case BLACK:
        return 1;
      case WHITE:
        return 2;
      default:
        return 0;
    }
  }

  public String toString() {
    return this.state.toString();
  }
}
