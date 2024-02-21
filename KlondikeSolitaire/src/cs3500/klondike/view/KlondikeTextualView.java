package cs3500.klondike.view;

import java.io.IOException;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike Solitaire game.
 */
public class KlondikeTextualView implements TextView {
  private final KlondikeModel model;
  private Appendable out;

  /**
   * Constructor for the KlondikeTextualView.
   * @param model the model of the game
   */
  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
  }

  /**
   * Constructor for the KlondikeTextualView, with an appendable.
   * @param model the model of the game
   * @param out the appendable
   */
  public KlondikeTextualView(KlondikeModel model, Appendable out) {
    if (model == null | out == null) {
      throw new IllegalArgumentException("Model or Appendable is null");
    }
    this.model = model;
    this.out = out;
  }

  /**
   * Represent the current game model as a String. Append the current draw pile,
   *  then the foundation, then the cascade. Each pile should be separated by a newline.
   *  If the cascade is empty, display an "X".
   * @return the current game board with the draw pile first, then the foundations, then the cascade
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    // draw pile
    sb.append("Draw:");
    if (!this.model.getDrawCards().isEmpty()) {
      for (Card c : this.model.getDrawCards()) {
        sb.append(" ").append(c.toString()).append(",");
      }

      sb.delete(sb.length() - 1, sb.length());
    } else {
      sb.append(" ");
    }
    sb.append("\n");

    // foundation piles
    sb.append("Foundation:");
    for (int i = 0; i < this.model.getNumFoundations(); i++) {
      if (this.model.getCardAt(i) == null) {
        sb.append(" <none>,");
      } else {
        sb.append(" ").append(this.model.getCardAt(i).toString()).append(",");
      }
    }
    sb.delete(sb.length() - 1, sb.length());
    sb.append("\n");

    // cascade piles
    // if a cascade pile is empty, represent by a "X" in row 0, if a card is face up,
    //  show its string representation, if a card is face down, show a "?"
    for (int row = 0; row < this.model.getNumRows(); row++) {
      for (int col = 0; col < this.model.getNumPiles(); col++) {
        if (this.model.getPileHeight(col) == 0 && row == 0) {
          sb.append("  ").append("X");
        } else if (this.model.getPileHeight(col) == 0) {
          sb.append("   ");
        } else if (this.model.getPileHeight(col) <= row) {
          sb.append("   ");
        } else if (this.model.isCardVisible(col, row)) {
          if (this.model.getCardAt(col, row).toString().length() == 3) {
            sb.append(this.model.getCardAt(col, row).toString());
          }
          else {
            sb.append(" ").append(this.model.getCardAt(col, row).toString());
          }
        } else {
          sb.append("  ").append("?");
        }
      }
      sb.append("\n");
    }

    sb.delete(sb.length() - 1, sb.length());
    return sb.toString();
  }

  @Override
  public void render() throws IOException {
    out.append(this.toString());
  }


}
