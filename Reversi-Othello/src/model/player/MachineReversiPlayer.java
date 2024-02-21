package model.player;

import java.awt.Point;

import model.model.ReadonlyReversiModel;
import strategy.InfallibleReversiStrategy;

/**
 * Represents a machine player in a game of Reversi.
 */
public class MachineReversiPlayer implements ReversiPlayer {

  private final InfallibleReversiStrategy strategy;
  private final ReadonlyReversiModel model;
  private PlayerFeatures features;
  private final Player piece;

  /**
   * Constructs a MachineReversiPlayer.
   * @param model the model for the game
   * @param strategy the strategy to use
   * @param piece the piece to represent the player
   */
  public MachineReversiPlayer(ReadonlyReversiModel model, InfallibleReversiStrategy strategy,
                              Player piece) {
    this.model = model;
    this.strategy = strategy;
    this.piece = piece;
  }

  @Override
  public void makeMove(int row, int col) {
    throw new IllegalArgumentException("Machine players cannot make moves");
  }

  @Override
  public void makeMove() {
    try {
      Point coords = strategy.chooseMove(model, piece);
      features.makeMove(coords.x, coords.y);
    } catch (IllegalStateException e) {
      features.pass();
    }
  }

  @Override
  public void pass() {
    features.pass();
  }

  @Override
  public void addFeatures(PlayerFeatures f) {
    this.features = f;
  }

  @Override
  public Player getPiece() {
    return this.piece;
  }

  @Override
  public boolean isHuman() {
    return false;
  }
}
