package provider.view;

import java.awt.Dimension;

import javax.swing.JFrame;

import provider.model.GameOutcome;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;

/**
 * A simple graphical view of Reversi that shows the board and the current discs on the board.
 */
public class SimpleReversiView extends JFrame implements ReversiView {
  private final JReversiPanel panel;
  private final ReadOnlyReversiModel model;

  /**
   * Instantiates a new Simple Reversi View based on the given model.
   *
   * @param model the model to graphically display
   */
  public SimpleReversiView(ReadOnlyReversiModel model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new JReversiPanel(model);
    this.model = model;
    this.panel.setFocusable(true);
    this.setVisible(true);
    this.add(panel);
    this.pack();
    this.setTitle(String.format("Reversi | B: %d | W: %d",
                                model.getScore(PlayerSymbol.BLACK),
                                model.getScore(PlayerSymbol.WHITE)));
  }

  @Override
  public Dimension getPreferredSize() {
    // TODO get this to work lol or just switch back to constants for now?
    return new Dimension((int) (Math.sqrt(3) * panel.getHexSize() * panel.getHexWidth())
            + 2 * panel.getHexSize(),
            (int) (Math.sqrt(3) * panel.getHexSize() * panel.getHexWidth())
                                 + 2 * panel.getHexSize());
  }

  @Override
  public void display(boolean activate) {
    this.setVisible(activate);
  }

  @Override
  public void addFeatureListener(ReversiFeatures features) {
    this.panel.addFeatureListener(features);
  }

  @Override
  public void displayError(String msg) {
    this.panel.displayError(msg);
  }

  @Override
  public void refresh() {
    this.panel.repaint();

    StringBuilder title = new StringBuilder();
    title.append(String.format("Reversi | B: %d | W: %d",
                               model.getScore(PlayerSymbol.BLACK),
                               model.getScore(PlayerSymbol.WHITE)));

    this.setTitle(title.toString());
  }

  @Override
  public void setGameOver(GameOutcome outcome) {
    StringBuilder winnerStr = new StringBuilder();

    if (outcome == GameOutcome.TIE) {
      winnerStr.append("Tie!");
    } else if (outcome == GameOutcome.LOSS) {
      winnerStr.append("You lose!");
    } else if (outcome == GameOutcome.WIN) {
      winnerStr.append("You win!");
    }

    this.setTitle(winnerStr.toString());
    this.panel.setGameOver(outcome);
  }
}
