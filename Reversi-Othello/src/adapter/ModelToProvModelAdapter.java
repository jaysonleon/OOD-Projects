package adapter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;
import model.model.Tile;
import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * Represents our model adapted to our provider's model interface.
 */
public class ModelToProvModelAdapter implements ReadOnlyReversiModel {

  static int[][] DIRECTIONS = {
          {1, 0}, {1, -1}, {0, -1}, {-1, 0}, {-1, 1}, {0, 1}
  };

  private final ReadonlyReversiModel adaptee;

  /**
   * Constructs a new ModelToProvModelAdapter.
   * @param model the model to adapt (our interface)
   */
  public ModelToProvModelAdapter(ReadonlyReversiModel model) {
    this.adaptee = model;
  }

  /**
   * Converts a hexCoordinate to a Point.
   * @param location the HexCoordinate to convert
   * @return the Point of the given HexCoordinate
   */
  private Point convertHexCoordinateToPoint(HexCoordinate location) {
    return new Point(location.q + this.getBoardSize(), location.r + this.getBoardSize());
  }

  @Override
  public Optional<PlayerSymbol> getPlayerOnTile(HexCoordinate location)
          throws IllegalArgumentException {
    Point point = convertHexCoordinateToPoint(location);
    Tile hex = adaptee.getTileAt(point.x, point.y);
    if (hex == null) {
      return Optional.empty();
    }
    else if (hex.getPlayerAt() == Player.BLACK) {
      return Optional.of(PlayerSymbol.BLACK);
    }
    else if (hex.getPlayerAt() == Player.WHITE) {
      return Optional.of(PlayerSymbol.WHITE);
    }
    else {
      return Optional.empty();
    }
  }

  @Override
  public PlayerSymbol getTurn() throws IllegalStateException {
    if (this.isGameOver()) {
      throw new IllegalStateException("Game is over");
    }
    Player player = adaptee.getTurn();
    if (player == Player.BLACK) {
      return PlayerSymbol.BLACK;
    } else {
      return PlayerSymbol.WHITE;
    }
  }

  @Override
  public boolean isGameOver() {
    return !(adaptee.hasValidMoves(Player.BLACK) || adaptee.hasValidMoves(Player.WHITE));
  }

  @Override
  public int getBoardSize() {
    return (adaptee.getWidth() - 1) / 2;
  }

  @Override
  public boolean moveValid(HexCoordinate location, PlayerSymbol player)
          throws IllegalArgumentException {
    Point point = convertHexCoordinateToPoint(location);
    return adaptee.isValidMove(point.x, point.y,
            player == PlayerSymbol.BLACK ? Player.BLACK : Player.WHITE);
  }

  @Override
  public int potentialDiscsToGain(HexCoordinate location, PlayerSymbol player) {
    Point point = convertHexCoordinateToPoint(location);
    return adaptee.moveScore(point.x, point.y);
  }

  @Override
  public int getScore(PlayerSymbol player) {
    if (player == PlayerSymbol.BLACK) {
      return adaptee.getScore(Player.BLACK);
    } else {
      return adaptee.getScore(Player.WHITE);
    }
  }

  @Override
  public boolean doesPlayerHaveMoves(PlayerSymbol player) {
    if (player == PlayerSymbol.BLACK) {
      return adaptee.hasValidMoves(Player.BLACK);
    } else {
      return adaptee.hasValidMoves(Player.WHITE);
    }
  }

  @Override
  public List<HexCoordinate> getNeighbors(HexCoordinate location) {
    List<HexCoordinate> neighbors = new ArrayList<>();
    Point point = convertHexCoordinateToPoint(location);
    for (int[] direction : DIRECTIONS) {
      int q = point.x;
      int r = point.y;
      int dq = direction[0];
      int dr = direction[1];
      q += dq;
      r += dr;
      int qcor = q - this.getBoardSize();
      int rcor = r - this.getBoardSize();
      HexCoordinate neighbor = new HexCoordinate(qcor, rcor, -qcor - rcor);
      try {
        if (adaptee.getTileAt(q, r) != null) {
          neighbors.add(neighbor);
        }
      } catch (IllegalArgumentException ignored) {
      }
    }
    return neighbors;
  }
}
