package model.model;

import java.util.Objects;

import model.player.Player;

/**
 * Represents a regular board used to play reversi. A regular board has a hexagonal shape, and
 * is big enough to play at least one more per player.
 */
public class HexBoard implements Board {

  // the dimension of the board, since height = width in this representation of reversi.
  private final int dim;
  // 2D array of Hex objects, null if no Hex at that position.
  // Hexes are stored in a 2D array, but the board is a hexagonal shape.
  // To index throughout the board, use the r and q coordinates as indicies, in that order.
  // r = row, q = column. 0,0 is the top left corner of the board.
  // The top-left most hex in a regular board is (0,5).
  private Hex[][] board;

  @Override
  public int getHeight() {
    return this.dim;
  }

  @Override
  public int getWidth() {
    return this.dim;
  }

  @Override
  public int getDim() {
    return this.dim;
  }

  /**
   * Board object constructor.
   *
   * @param dim dimension of the board
   */
  public HexBoard(int dim) {
    this.dim = dim;
    board = new Hex[dim][dim];
  }

  /**
   * Return the hex at the given position, or {@code null} if there is no hex at the given position.
   *
   * @param q the q coordinate
   * @param r the r coordinate
   * @return the hex at the given position, or {@code null} if there is no hex at the given position
   */
  public Tile getTileAt(int q, int r) {
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      return null;
    }
    return board[r][q];
  }

  /**
   * Adds pieces to the board based on the sideLength,
   * (default is 6, can be specified by user).
   *
   * @param board      the board
   * @param q          the q coordinate
   * @param r          the r coordinate
   * @param sideLength the side length of the board
   */
  public void addStartingPieces(Hex[][] board, int q, int r, int sideLength) {
    if (q == sideLength - 1 && r == sideLength - 2) {
      board[r][q].placePlayerEmpty(Player.BLACK);
    } else if (q == sideLength - 2 && r == sideLength - 1) {
      board[r][q].placePlayerEmpty(Player.WHITE);
    } else if (q == sideLength - 2 && r == sideLength) {
      board[r][q].placePlayerEmpty(Player.BLACK);
    } else if (q == sideLength - 1 && r == sideLength) {
      board[r][q].placePlayerEmpty(Player.WHITE);
    } else if (q == sideLength && r == sideLength - 1) {
      board[r][q].placePlayerEmpty(Player.BLACK);
    } else if (q == sideLength && r == sideLength - 2) {
      board[r][q].placePlayerEmpty(Player.WHITE);
    }
  }

  /**
   * Adds hex objects to the board based on the sideLength,
   * (default is 6, can be specified by user).
   */
  @Override
  public void addStartingCells(int dim, int sideLength) {
    for (int r = 0; r < dim; r++) {
      for (int q = 0; q < dim; q++) {
        if (q + r < dim - sideLength || q + r > dim + sideLength - 2) {
          this.board[r][q] = null;

        } else {
          this.board[r][q] = new Hex(q, r);
          this.addStartingPieces(board, q, r, sideLength);
        }
      }
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof HexBoard)) {
      return false;
    }
    HexBoard otherHexBoard = (HexBoard) other;
    if (this.dim != otherHexBoard.dim) {
      return false;
    }

    for (int r = 0; r < this.dim; r++) {
      for (int q = 0; q < this.dim; q++) {
        if (this.getTileAt(q, r) == null && otherHexBoard.getTileAt(q, r) == null) {
          continue;
        }
        if (this.getTileAt(q, r) == null || otherHexBoard.getTileAt(q, r) == null) {
          return false;
        }
        if (!this.getTileAt(q, r).equals(otherHexBoard.getTileAt(q, r))) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 0;
    for (int r = 0; r < this.dim; r++) {
      for (int q = 0; q < this.dim; q++) {
        if (this.getTileAt(q, r) != null) {
          result += 1;
        }
      }
    }
    return Objects.hash(result);
  }

  /**
   * Set the hex at the given position to the given hex.
   *
   * @param q the q coordinate
   * @param r the r coordinate
   * @throws IllegalArgumentException if the coordinates are invalid
   */
  public void setTileAt(int q, int r) {
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      throw new IllegalArgumentException("Invalid coordinates");
    }

    board[r][q] = new Hex(q, r);
  }
}
