package model.model;

import model.player.Player;

/**
 * Represents a regular square board in a game of Reversi. The default board is 8x8.
 */
public class SquareBoard implements Board {
  private final int dim;
  // cell is a tile in the board
  // top-left corner is index 0,0
  // index using (row,col)
  private Cell[][] board;

  /**
   * Constructs a SquareBoard object with the given dimension.
   *
   * @param dim dimension of the board
   */
  public SquareBoard(int dim) {
    this.dim = dim;
    board = new Cell[dim][dim];
  }

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

  @Override
  public Tile getTileAt(int q, int r) {
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      return null;
    }
    return board[q][r];
  }

  public void addStartingPieces(Cell[][] board, int q, int r, int sideLength) {
    if ((q == (sideLength / 2) - 1) && (r == (sideLength / 2) - 1)) {
      board[q][r].placePlayerEmpty(Player.BLACK);
    } else if ((q == (sideLength / 2) - 1) && (r == (sideLength / 2))) {
      board[q][r].placePlayerEmpty(Player.WHITE);
    } else if ((q == (sideLength / 2)) && (r == (sideLength / 2) - 1)) {
      board[q][r].placePlayerEmpty(Player.WHITE);
    } else if ((q == (sideLength / 2)) && (r == (sideLength / 2))) {
      board[q][r].placePlayerEmpty(Player.BLACK);
    }
  }

  @Override
  public void addStartingCells(int dim, int sideLength) {
    for (int q = 0; q < sideLength; q++) {
      for (int r = 0; r < sideLength; r++) {
        board[q][r] = new Cell(q, r);
        this.addStartingPieces(board, q, r, sideLength);
      }
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof SquareBoard)) {
      return false;
    }

    SquareBoard otherSquareBoard = (SquareBoard) other;
    if (this.dim != otherSquareBoard.dim) {
      return false;
    }

    for (int q = 0; q < this.dim; q++) {
      for (int r = 0; r < this.dim; r++) {
        if (this.getTileAt(q, r) == null && otherSquareBoard.getTileAt(q, r) == null) {
          continue;
        }
        if (this.getTileAt(q, r) == null || otherSquareBoard.getTileAt(q, r) == null) {
          return false;
        }
        if (!this.getTileAt(q, r).equals(otherSquareBoard.getTileAt(q, r))) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + this.dim;
    for (int q = 0; q < this.dim; q++) {
      for (int r = 0; r < this.dim; r++) {
        result = 31 * result + this.getTileAt(q, r).hashCode();
      }
    }
    return result;
  }

  @Override
  public void setTileAt(int q, int r) {
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      throw new IllegalArgumentException("Invalid coordinates");
    }

    board[q][r] = new Cell(q, r);
  }
}
