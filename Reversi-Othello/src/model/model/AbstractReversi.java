package model.model;

import java.util.ArrayList;
import java.util.List;

import model.player.Player;

/**
 * Represents an abstract model of the game of Reversi. It represents a game of Reversi,
 * played both on a hex or a square board.
 */
public class AbstractReversi implements ReversiModel {

  // represents the board used in the game of Reversi
  protected Board board;
  protected int dim;
  protected int sideLength;
  // the current player's turn
  protected Player turn;
  // the status of the game
  protected Status status;
  // INVARIANT 1: numConsecPass < 2
  // number of times in a row a player has passed their turn
  protected int numConsecPass;
  // is this game using a hex board or a square board?
  protected boolean isHex;
  // the listeners of this model
  protected List<ModelFeatures> listeners;

  /**
   * Constructs a Reversi game with the specified side length and board type.
   * @param sideLength the side length of the board
   * @param isHex true if the board is hexagonal, false if the board is square
   */
  public AbstractReversi(int sideLength, boolean isHex) {
    if (isHex) {
      if (sideLength < 3) {
        throw new IllegalArgumentException("Invalid side length. "
                + "Must be greater than or equal to 3.");
      }
      this.sideLength = sideLength;
      this.dim = sideLength * 2 - 1;
      this.board = new HexBoard(this.dim);
    } else {
      if (sideLength <= 2 || sideLength % 2 != 0) {
        throw new IllegalArgumentException("Invalid side length. "
                + "Must be positive and even. ");
      }
      this.sideLength = sideLength;
      this.dim = sideLength;
      this.board = new SquareBoard(sideLength);
    }
    this.isHex = isHex;
    this.turn = null;
    this.status = null;
    this.numConsecPass = 0;
    this.listeners = new ArrayList<>();
  }

  @Override
  public void startGame() {
    if (this.status != null) {
      throw new IllegalStateException("Game has already started");
    }
    // add tiles to board
    this.board.addStartingCells(dim, sideLength);
    this.status = Status.Playing;
    this.turn = Player.BLACK;
    notifyPlayerTurn(this.turn);
  }

  // enforces INVARIANT 1 since this method only assigns numConsecPass to 0
  @Override
  public void playMove(int q, int r) {
    if (this.status != Status.Playing) {
      throw new IllegalStateException("Game is not in progress.");
    }
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    if (getTileAt(q, r) == null) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    if (getTileAt(q, r).hasPlayer()) {
      throw new IllegalArgumentException("Hex is already occupied");
    }
    // validate moves
    if (!isValidMove(q, r, this.turn)) {
      throw new IllegalStateException("Invalid move");
    }

    makeMove(q, r);
    this.numConsecPass = 0;
    notifyPlayerTurn(this.turn);
  }

  // enforces INVARIANT 1 since if numConsecPass == 0, the game is ended.
  // numConsecPass cannot be incremented any further after the game ends.
  @Override
  public void pass() {
    if (this.status != Status.Playing) {
      throw new IllegalStateException("Game is over");
    }

    this.numConsecPass++;

    // if the number of consecutive passes is 2, the game is over
    if (this.numConsecPass == 2) {
      this.status = Status.Over;
      return;
    }

    turn = this.nextTurn();
    notifyPlayerTurn(this.turn);
  }

  @Override
  public boolean isGameOver() {
    if ((!hasValidMoves(Player.BLACK) && !hasValidMoves(Player.WHITE))
            || this.numConsecPass == 2
            || this.getScore(Player.BLACK) == 0
            || this.getScore(Player.WHITE) == 0) {
      this.status = Status.Over;

      return true;
    }

    return false;
  }

  @Override
  public void addFeatures(ModelFeatures features) {
    listeners.add(features);
  }

  @Override
  public void notifyPlayerTurn(Player p) {
    for (ModelFeatures f : listeners) {
      f.updateView();
    }
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
  public int getSideLen() {
    return this.sideLength;
  }

  @Override
  public Status getStatus() {
    return this.status;
  }

  @Override
  public Board getBoard() {
    return this.board;
  }

  @Override
  public int getScore(Player player) {
    int count = 0;
    for (int r = 0; r < this.dim; r++) {
      for (int q = 0; q < this.dim; q++) {

        if (this.getTileAt(q, r) == null) {
          continue;
        }
        if (this.board.getTileAt(q, r).getPlayerAt() == player) {
          count++;
        }
      }
    }
    return count;
  }

  @Override
  public Player getTurn() {
    return this.turn;
  }

  @Override
  public Tile getTileAt(int q, int r) {
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    return this.board.getTileAt(q, r);
  }

  @Override
  public Player nextTurn() {
    Player ans = this.turn;
    return ans == Player.BLACK ? Player.WHITE : Player.BLACK;
  }

  @Override
  public Player determineWinner() {
    if (this.status == null) {
      throw new IllegalStateException("Game has not started");
    }
    if (this.status == Status.Playing) {
      throw new IllegalStateException("Game is not over");
    }
    if (this.getScore(Player.BLACK) > this.getScore(Player.WHITE)) {
      return Player.BLACK;
    } else if (this.getScore(Player.BLACK) < this.getScore(Player.WHITE)) {
      return Player.WHITE;
    } else {
      return Player.EMPTY;
    }
  }

  @Override
  public Board copyBoard() {
    Board newBoard;
    if (this.isHex) {
      newBoard = new HexBoard(this.dim);
    } else {
      newBoard = new SquareBoard(this.dim);
    }

    for (int r = 0; r < this.dim; r++) {
      for (int q = 0; q < this.dim; q++) {
        Tile hex = this.getTileAt(q, r);
        if (hex != null) {
          newBoard.setTileAt(q, r);
          newBoard.getTileAt(q, r).changePlayer(hex.getPlayerAt());
        }
      }
    }
    return newBoard;
  }

  @Override
  public boolean isValidMove(int q, int r, Player player) {
    // if the tile is not empty, the move is not valid
    if (getTileAt(q, r).getPlayerAt() != Player.EMPTY) {
      return false;
    }
    boolean result = false;

    // use the appropriate directions depending on the game type
    int[][] directions = determineDirection();
    // check all directions
    for (int[] direction : directions) {
      // get the tiles in the direction
      List<Tile> tiles = getTilesInDirection(q, r, direction);
      // if the direction is valid, the move is valid
      if (isValidDirection(tiles, player)) {
        result = true;
      }
    }

    return result;
  }

  @Override
  public int moveScore(int q, int r) {
    List<Tile> flippedTiles = new ArrayList<>();
    for (int[] direction : this.getValidDirections(q, r, this.turn)) {
      List<Tile> tiles = getTilesInDirection(q, r, direction);
      if (isValidDirection(tiles, this.turn)) {
        for (Tile t : tiles) {
          if (t.getPlayerAt() == Player.EMPTY) {
            continue; // skip empty tiles
          } else if (t.getPlayerAt() == this.turn) {
            break; // stop when we reach a tile of the current player
          } else {
            flippedTiles.add(t);
          }
        }
      }
    }

    if (flippedTiles.isEmpty()) {
      return 0;
    } else {
      return flippedTiles.size() + 1;
    }
  }

  @Override
  public boolean hasValidMoves(Player player) {
    // check all Tiles
    for (int r = 0; r < this.dim; r++) {
      for (int q = 0; q < this.dim; q++) {
        Tile tile = getTileAt(q, r);
        // if the tile is empty, check if it is a valid move
        if (tile != null && tile.getPlayerAt() == Player.EMPTY) {
          List<int[]> directions = getValidDirections(q, r, player);
          if (!directions.isEmpty()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Makes a move at the specified coordinates for the current player.
   *
   * @param q the x coordinate
   * @param r the y coordinate
   */
  protected void makeMove(int q, int r) {
    Tile tile = getTileAt(q, r);
    tile.changePlayer(this.turn);

    List<Tile> flippedTiles = new ArrayList<>();

    int[][] directions = determineDirection();

    for (int[] direction : directions) {

      List<Tile> tiles = getTilesInDirection(q, r, direction);
      // after tiles are collected, trim the list such that it starts and ends
      // with the current player's tiles
      if (isValidDirection(tiles, this.turn)) {
        for (Tile t : tiles) {
          if (t.getPlayerAt() == Player.EMPTY) {
            break; // skip empty tiles
          } else if (t.getPlayerAt() == this.turn) {
            break; // stop when we reach a tile of the current player
          } else {
            flippedTiles.add(t);
          }
        }
      }
    }

    // flip the tiles
    for (Tile t : flippedTiles) {
      t.changePlayer(this.turn);
    }
    // change the turn
    turn = this.nextTurn();
  }

  /**
   * Returns a list of tiles in the specified direction from the given tile.
   * The list includes the tile at the given position, at index[0].
   *
   * @param q         the x coordinate of the starting hex
   * @param r         the y coordinate of the starting hex
   * @param direction the direction vector
   * @return list of tiles in the specified direction
   */
  protected List<Tile> getTilesInDirection(int q, int r, int[] direction) {
    List<Tile> tiles = new ArrayList<>();
    // validate coordinates
    if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
      return tiles;
    }
    // add the starting tile to the list
    tiles.add(getTileAt(q, r));

    // get the appropriate direction vector
    int dq = direction[0];
    int dr = direction[1];
    while (true) {
      // move in the direction vector
      q += dq;
      r += dr;
      // ensure the coordinates are valid
      if (q < 0 || q >= this.dim || r < 0 || r >= this.dim) {
        break;
      }
      // add the tile to the list
      Tile tile = getTileAt(q, r);
      if (tile == null) {
        break;
      }
      tiles.add(tile);
    }

    return tiles;
  }

  /**
   * Returns true if the specified direction contains a valid move for the current player.
   * A valid move is a move that is adjacent (in at least one direction)
   * to a straight line of the opponent player's disks,
   * at the far end of which is another player's disk.
   *
   * @param tiles  the list of tiles in the direction
   * @param player the player making the move
   * @return true if the direction is valid, false otherwise
   */
  protected boolean isValidDirection(List<Tile> tiles, Player player) {
    if (tiles.size() < 3) {
      return false;
    }

    Player otherPlayer = this.nextTurn();
    boolean foundOtherPlayer = false;
    boolean foundCurrPlayer = false;
    // ignore the first tile, since it is the starting tile.
    // we only care about the tiles after it.
    tiles.remove(0);
    for (Tile tile : tiles) {
      // if the tile is empty, the tiles are not consecutive, the move is not valid
      if (tile.getPlayerAt() == Player.EMPTY) {
        return false;
      }
      // if the tile is the other player's, we have found the other player
      else if (tile.getPlayerAt() == otherPlayer) {
        foundOtherPlayer = true;
      }
      // if the tile is the current player's, we have found the current player
      else if (tile.getPlayerAt() == player) {
        foundCurrPlayer = true;
        break;
      } else {
        foundOtherPlayer = false;
      }
    }
    // only return true if we have found both the other player and the current player
    return (foundOtherPlayer && foundCurrPlayer);
  }

  /**
   * Returns a list of valid directions for the current player at the specified coordinates.
   *
   * @param q      the x coordinate
   * @param r      the y coordinate
   * @param player the current player
   * @return a list of valid directions for the current player at the specified coordinates
   */
  protected List<int[]> getValidDirections(int q, int r, Player player) {
    List<int[]> validDirections = new ArrayList<>();
    // use the appropriate directions depending on the game type
    int[][] directions = determineDirection();
    // check all directions
    for (int[] direction : directions) {
      // get the tiles in the direction
      List<Tile> tiles = getTilesInDirection(q, r, direction);
      // if the direction is valid, add it to the list
      if (isValidDirection(tiles, player)) {
        validDirections.add(direction);
      }
    }

    return validDirections;
  }

  /**
   * Returns the appropriate direction vectors depending on the game type.
   * @return the appropriate direction vectors depending on the game type
   */
  private int[][] determineDirection() {
    return this.isHex ? HEX_DIRECTIONS : SQUARE_DIRECTIONS;
  }

  @Override
  public boolean isHex() {
    return this.isHex;
  }
}
