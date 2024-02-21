package view.gui;

import java.awt.geom.Point2D;

import controller.ViewFeatures;

/**
 * Represents the view for the Reversi game.
 */
public interface ReversiView {

  /**
   * Adds the given features to the view.
   *
   * @param viewFeatures the features to add
   */
  void addFeatures(ViewFeatures viewFeatures);

  /**
   * Selects the hexagon at the given coordinates.
   */
  void selectHex();

  /**
   * Converts the mouse coordinates from the given mouseEvent to hex coordinates.
   *
   * @return the hex coordinates
   */
  Point2D convertCoords();

  void updateView();

  void showMessage(String message);
}
