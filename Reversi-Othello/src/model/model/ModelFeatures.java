package model.model;

/**
 * Represents the features that the model can call on the player.
 */
public interface ModelFeatures {
  /**
   * Get the current turn, if appropriate.
   */
  void updateView();

  /**
   * Get the current turn, if appropriate.
   */
  void showWinner();
}
