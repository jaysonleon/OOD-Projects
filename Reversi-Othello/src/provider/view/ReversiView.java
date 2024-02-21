package provider.view;

import provider.model.GameOutcome;

/**
 * An interface that represents the behavior a graphical view of Reversi should have.
 */
public interface ReversiView {
  /**
   * Toggles whether this view should be displayed or not.
   *
   * @param activate if the view should be displayed
   */
  void display(boolean activate);

  /**
   * Adds a feature listener to this view in order to be notified when certain features are
   * triggered in the view.
   *
   * @param features the listener to be attached to the view
   */
  void addFeatureListener(ReversiFeatures features);

  /**
   * Displays an error dialog window with the given message.
   *
   * @param msg the message to display in the error window
   */
  void displayError(String msg);

  /**
   * Refreshes this view with a new title and graphics of the board.
   */
  void refresh();

  /**
   * Sets the game over state on this view with the given outcome.
   *
   * @param outcome turns background green and title to "You win!" if won, red and "You lose!" if
   *               lost, and yellow and "Tie!" if tied
   */
  void setGameOver(GameOutcome outcome);
}
