package provider.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

import provider.model.GameOutcome;
import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.model.ReadOnlyReversiModel;
import provider.view.ReversiFeatures;

/**
 * A JPanel that displays the currently playing Reversi board.
 */
public class JReversiPanel extends JPanel {
  private final ReadOnlyReversiModel model;

  private final List<ReversiFeatures> featuresListeners;
  private GameOutcome outcome;

  /**
   * The size in pixels of the side of each hexagon (with some alterations on the diagonals).
   */
  private final int HEX_SIZE = 25;
  private HexCoordinate clickedHex;

  /**
   * Instantiates a new panel for the Reversi board.
   *
   * @param model the model to base the drawing of the board off of
   */
  public JReversiPanel(ReadOnlyReversiModel model) {
    this.model = model;
    // empty list for now until controller is implemented and concrete features are implemented
    this.featuresListeners = new ArrayList<>();

    this.addMouseListener(new MouseEventsListener());
    this.addKeyListener(new KeyEventsListener());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    if (this.outcome != null) {
      switch (outcome) {
        case TIE:
          this.setBackground(Color.YELLOW);
          break;
        case WIN:
          this.setBackground(Color.GREEN);
          break;
        case LOSS:
          this.setBackground(Color.RED);
          break;
        default:
          throw new RuntimeException("This is an impossible branch");
      }
    } else {
      this.setBackground(Color.DARK_GRAY);
    }

    // moves the origin of the drawing to the middle of the window
    g2d.translate(this.getWidth() / 2, this.getHeight() / 2);

    int boardSize = this.model.getBoardSize();

    for (int q = -boardSize; q <= boardSize; q++) {
      int startingR = Math.max(-boardSize, -q - boardSize);
      int endingR = Math.min(boardSize, -q + boardSize);
      for (int r = startingR; r <= endingR; r++) {
        // gets where the center of the next hexagon to be drawn is
        Point2D center = axialToPixelCoords(q, r);
        this.paintHexagon(g2d, center.getX(), center.getY(),
                          this.clickedHex != null
                                  && this.clickedHex.equals(new HexCoordinate(q, r, -q - r)));
      }
    }
  }

  /**
   * Adds another Reversi feature listener to this panel.
   *
   * @param features the features listener to add
   */
  public void addFeatureListener(ReversiFeatures features) {
    this.featuresListeners.add(features);
  }

  /**
   * Draws a new hexagon with the center at the given x and y coordinates.
   *
   * @param g2d     the graphics object to draw with
   * @param x       the x coordinate of the center of the hexagon
   * @param y       the y coordinate of the center of the hexagon
   * @param clicked whether to render this hexagon in a clicked states (highlighted) or not
   */
  private void paintHexagon(Graphics2D g2d, double x, double y, boolean clicked) {
    AffineTransform oldTransform = g2d.getTransform();
    g2d.translate(x, y);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(2));
    HexPath2D hex = new HexPath2D(HEX_SIZE);
    g2d.draw(hex);
    if (clicked) {
      g2d.setColor(Color.CYAN);
    } else {
      g2d.setColor(Color.LIGHT_GRAY);
    }

    g2d.fill(hex);

    // draws any potential disc on this hexagon if it exists
    Optional<PlayerSymbol> symbol = model.getPlayerOnTile(this.pixelToCubeCoords(x, y));
    if (symbol.isPresent() && symbol.get() == PlayerSymbol.WHITE) {
      g2d.setColor(Color.WHITE);
    } else if (symbol.isPresent() && symbol.get() == PlayerSymbol.BLACK) {
      g2d.setColor(Color.BLACK);
    }
    g2d.fillOval(-HEX_SIZE / 2, -HEX_SIZE / 2, HEX_SIZE, HEX_SIZE);
    g2d.setTransform(oldTransform);
  }

  /**
   * Transforms the given axial coordinates to their equivalent pixel coordinates on the screen. The
   * pixel equivalent of the axial coordinates is the center of the hexagon identified with the
   * given axial coordinates.
   *
   * @param q the q axis of the axial coordinates to transform
   * @param r the r axis of the axial coordinates to transform
   * @return the physical point representing the center of the hexagon at the  given axial
   *         coordinates
   */
  private Point2D axialToPixelCoords(int q, int r) {
    // refer here to more info: https://www.redblobgames.com/grids/hexagons/#hex-to-pixel

    // a change in q coordinate between two adjacent pointy-top hexagons means a change in x of
    // sqrt(3), for a change in r coordinate is a change in x of sqrt(3)/2
    double x = HEX_SIZE * (Math.sqrt(3) * q + Math.sqrt(3) / 2 * r);

    // a change in r coordinate between two adjacent pointy-top hexagons means a change in y of 3/2
    double y = HEX_SIZE * (3. / 2 * r);
    return new Point2D.Double(x, y);
  }

  /**
   * Transforms the given physical coordinates into a HexCoordinate. X and y coordinates map to
   * hexagons when the physical coordinates fall within the bounds of the hexagon on the screen.
   *
   * @param x the x component of the physical coordinate
   * @param y the y component of the physical coordinate
   * @return a HexCoordinate that
   */
  private HexCoordinate pixelToCubeCoords(double x, double y) {
    // refer here for more info: https://www.redblobgames.com/grids/hexagons/#pixel-to-hex

    // this gives fractional coordinates for each component, which need to be further refined
    // into rounded numbers below
    double q = (Math.sqrt(3) / 3 * x - 1. / 3 * y) / HEX_SIZE;
    double r = (2. / 3 * y) / HEX_SIZE;
    double s = -q - r;

    double roundedQ = Math.round(q);
    double roundedR = Math.round(r);
    double roundedS = Math.round(s);

    double qDiff = Math.abs(roundedQ - q);
    double rDiff = Math.abs(roundedR - r);
    double sDiff = Math.abs(roundedS - s);

    // if the rounding error for the q component is the greatest, that means the final q needs to
    // be derived from the others components
    if (qDiff > rDiff && qDiff > sDiff) {
      roundedQ = -roundedR - roundedS;
    }
    // similar logic here, just that the r needs to be fixed
    else if (rDiff > sDiff) {
      roundedR = -roundedQ - roundedS;
    }
    // otherwise the s needs to be fixed
    else {
      roundedS = -roundedQ - roundedR;
    }

    return new HexCoordinate((int) roundedQ, (int) roundedR, (int) roundedS);
  }

  /**
   * Exposes the hex size constant set in this class to size other windows and parts of the board
   * properly.
   *
   * @return the hex size constant representing the size of a side of a single hexagon on the
   *         board in pixels
   */
  public int getHexSize() {
    return this.HEX_SIZE;
  }

  /**
   * Exposes the "diameter" of this board, or how many hexagons the board is across the middle row.
   *
   * @return the number of hexagons in the middle line which determines the width of the board
   */
  public int getHexWidth() {
    return this.model.getBoardSize() * 2 + 1;
  }

  /**
   * Displays an error message with the given text.
   *
   * @param msg the message to play in the error window
   */
  public void displayError(String msg) {
    JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Sets the game over state for the game in this panel.
   *
   * @param outcome the outcome of the game (win, loss, tie)
   * @see ReversiView#setGameOver(GameOutcome)
   */
  public void setGameOver(GameOutcome outcome) {
    this.outcome = outcome;
  }

  /**
   * Listens for mouse clicks on the board.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      System.out.println("Clicked: " + e.getX() + ", " + e.getY());
      // converts the physical coordinates of the click to a HexCoordinate. Shifts the x and y
      // coordinates to offset properly to be in the center of the screen
      HexCoordinate logical = JReversiPanel.this.pixelToCubeCoords(
              e.getX() - ((double) JReversiPanel.this.getWidth() / 2),
              e.getY() - ((double) JReversiPanel.this.getHeight() / 2));

      if (logical.equals(JReversiPanel.this.clickedHex)) {
        JReversiPanel.this.clickedHex = null;
      } else {
        JReversiPanel.this.clickedHex = logical;
      }

      JReversiPanel.this.repaint();
    }
  }

  /**
   * Listens for key presses from the user while playing the game.
   */
  private class KeyEventsListener implements KeyListener {
    Map<Character, Runnable> keyTypes = new HashMap<>();

    KeyEventsListener() {
      // maps the p key to initiate passing a turn
      keyTypes.put('p', () -> {
        for (ReversiFeatures features : JReversiPanel.this.featuresListeners) {
          System.out.println("Passed");
          features.passTurn();
        }
      });

      // maps the space bar to place a disc when a hexagon is selected
      keyTypes.put(' ', () -> {
        for (ReversiFeatures features : JReversiPanel.this.featuresListeners) {
          if (JReversiPanel.this.clickedHex != null) {
            System.out.println("Filled: " + clickedHex);
            features.placeDisc(clickedHex);
          }
        }
      });
    }

    @Override
    public void keyTyped(KeyEvent e) {
      if (this.keyTypes.containsKey(e.getKeyChar())) {
        this.keyTypes.get(e.getKeyChar()).run();
      }
      JReversiPanel.this.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
      // Empty because we do not care about keyPressed events
    }

    @Override
    public void keyReleased(KeyEvent e) {
      // Empty because we do not care about keyReleased events
    }
  }
}
