package adapter;

import java.awt.geom.Point2D;

import controller.ViewFeatures;
import view.gui.ReversiView;
import provider.model.ReadOnlyReversiModel;
import provider.view.SimpleReversiView;

/**
 * Represents the provided view interface adapted to our view interface.
 */
public class ProvViewToOurViewAdapter implements ReversiView {
  private final ReadOnlyReversiModel model;
  private final SimpleReversiView view;

  /**
   * Constructs a new ProvViewToOurViewAdapter.
   * @param model the model to display
   * @param view the view to adapt (provided interface)
   */
  public ProvViewToOurViewAdapter(ReadOnlyReversiModel model, SimpleReversiView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void addFeatures(ViewFeatures viewFeatures) {
    view.addFeatureListener(new ViewFeaturesAdapter(this.model, viewFeatures));
  }

  @Override
  public void selectHex() {
    view.refresh();
  }

  @Override
  public Point2D convertCoords() {

    return null;
  }

  @Override
  public void updateView() {
    view.refresh();
  }

  @Override
  public void showMessage(String message) {
    view.displayError(message);
  }
}
