package model.player;

/**
 * Represents a human player in a game of Reversi.
 */
public class HumanReversiPlayer implements ReversiPlayer {


  private PlayerFeatures features;
  private final Player piece;

  /**
   * Constructs a HumanReversiPlayer with the given piece that represents which player the Human is
   *  (black or white).
   * @param piece the piece to represent the player
   */
  public HumanReversiPlayer(Player piece) {
    this.piece = piece;
  }

  @Override
  public void makeMove(int row, int col) {
    features.makeMove(row, col);
  }

  @Override
  public void makeMove() {
    throw new IllegalArgumentException("Human players cannot use AI strategies");
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
    return true;
  }
}
