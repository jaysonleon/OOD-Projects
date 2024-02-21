package cs3500.klondike.view;

import java.io.IOException;

/**
 * A marker interface for all text-based views, to be used in the Klondike Solitaire game.
 */
public interface TextView {
  /**
   * Return the string version of the current gameState.
   *
   * @return the current game board with the draw pile first, then the foundations,
   *     then the cascades.
   */
  String toString();

  /**
   * Render the current gameState to the Appendable.
   * @throws IOException if the Appendable fails to append
   */
  void render() throws IOException;
}
