package adapter;

import java.awt.Point;

import controller.ViewFeatures;
import provider.model.HexCoordinate;
import provider.model.ReadOnlyReversiModel;
import provider.view.ReversiFeatures;

/**
 * Represents our view features adapted to our provider's view features interface.
 */
public class ViewFeaturesAdapter implements ReversiFeatures {

  private final ReadOnlyReversiModel model;
  private final ViewFeatures viewFeatures;

  /**
   * Constructs a new ViewFeaturesAdapter.
   * @param model the model to display
   * @param viewFeatures the view features to adapt (our interface)
   */
  public ViewFeaturesAdapter(ReadOnlyReversiModel model, ViewFeatures viewFeatures) {
    this.model = model;
    this.viewFeatures = viewFeatures;
  }

  /**
   * Converts a HexCoordinate to a Point.
   * @param location the HexCoordinate to convert
   * @return the Point of the given HexCoordinate
   */
  private Point convertHexCoordinateToPoint(HexCoordinate location) {
    return new Point(location.q + model.getBoardSize(), location.r + model.getBoardSize());
  }

  @Override
  public void placeDisc(HexCoordinate location) {
    // convert coords
    Point p = convertHexCoordinateToPoint(location);
    viewFeatures.placePieceHelper(p.x, p.y);
  }



  @Override
  public void passTurn() {
    viewFeatures.passTurn();
  }
}
