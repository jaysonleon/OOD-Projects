package model.player;

/**
 * Represents a player in the Reversi game.
 * Also used to represent if a disc is in a cell in the board.
 * If there is no player disc at the hex, the hex is empty.
 */
public enum Player {
  BLACK("X"), WHITE("O"), EMPTY("_");

  private final String strRep; // symbol of player

  Player(String strRep) {
    this.strRep = strRep;
  }

  @Override
  public String toString() {
    return strRep;
  }
}
