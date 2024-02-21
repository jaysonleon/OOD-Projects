package view.gui;

import model.player.Player;
import model.model.ReadonlyReversiModel;
import model.model.Tile;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents the drawing panel for the Reversi game, played with a hexagonal board.
 */
public class HexReversiPanel extends JPanel {

  private final ReadonlyReversiModel model;
  private Tile selectedHex;
  private static final double HEX_SIZE = 30;

  public HexReversiPanel(ReadonlyReversiModel model) {
    this.model = model;
    this.selectedHex = null;
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension d = new Dimension();
    d.setSize(HEX_SIZE * (Math.sqrt(3) * model.getWidth()),
            HEX_SIZE * (model.getWidth() * 1.5 + 0.5));
    return d;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    this.setBackground(Color.BLACK);

    // Set antialiasing for smoother edges
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int rows = model.getWidth();
    int cols = model.getHeight();
    int side = model.getSideLen();


    for (int r = 0; r < rows; r++) {
      for (int q = 0; q < cols; q++) {

        Tile hex = model.getTileAt(q, r);
        Point2D p = this.toPixel(hex);
        if (p == null) {
          continue;
        }
        double centerX = p.getX() - ((side - 2) * Math.sqrt(3) / 2 * HEX_SIZE);
        double centerY = p.getY() + (HEX_SIZE);

        this.drawHexagon(g2d, centerX, centerY, Color.GRAY);
        this.drawDisc(g2d, centerX, centerY, hex.getPlayerAt(), hex);
      }
    }
  }

  /**
   * Draws the given hex and disc with the given color.
   *
   * @param hex   the hex to draw
   * @param color the color to draw the hex
   */
  private void drawHexAndDisc(Tile hex, Color color) {
    int side = model.getSideLen();
    double centerX = this.toPixel(hex).getX() - ((side - 2) * Math.sqrt(3) / 2 * HEX_SIZE);
    double centerY = this.toPixel(hex).getY() + HEX_SIZE;
    this.drawHexagon((Graphics2D) this.getGraphics(), centerX, centerY, color);

    this.drawDisc((Graphics2D) this.getGraphics(), centerX, centerY, hex.getPlayerAt(), hex);
  }

  /**
   * Draws a disc with the given center and radius.
   *
   * @param g       the graphics object
   * @param centerX the x coordinate of the center of the disc
   * @param centerY the y coordinate of the center of the disc
   * @param color   the color of the disc
   * @param hex     the hex that the disc is on
   */
  private void drawDisc(Graphics2D g, double centerX, double centerY, Player color, Tile hex) {
    double radius = HEX_SIZE / 2.0;
    if (hex.hasPlayer()) {
      switch (color) {
        case BLACK:
          g.setColor(Color.BLACK);
          break;
        case WHITE:
          g.setColor(Color.WHITE);
          break;
        default:
          return;
      }

      Ellipse2D.Double disc = new Ellipse2D.Double(centerX - radius,
              centerY - radius, 2 * radius, 2 * radius);
      g.fill(disc);
    }
  }

  /**
   * Draws a hexagon with the given center and radius.
   *
   * @param g       the graphics object
   * @param centerX the x coordinate of the center of the hexagon
   * @param centerY the y coordinate of the center of the hexagon
   * @param c       the color of the hexagon
   */
  private void drawHexagon(Graphics2D g, double centerX, double centerY, Color c) {
    Path2D.Double hexagon = new Path2D.Double();

    List<Point2D> points = getCornerPoints(centerX, centerY);
    hexagon.moveTo(points.get(0).getX(), points.get(0).getY());
    for (int i = 1; i < points.size(); i++) {
      hexagon.lineTo(points.get(i).getX(), points.get(i).getY());
    }
    hexagon.closePath();

    g.setColor(Color.BLACK);
    g.draw(hexagon);
    g.setColor(c);
    g.fill(hexagon);
  }

  /**
   * Returns the corner points of a hexagon with the given center and radius.
   *
   * @param centerX the x coordinate of the center of the hexagon
   * @param centerY the y coordinate of the center of the hexagon
   * @return the corner points of a hexagon with the given center and radius
   */
  private List<Point2D> getCornerPoints(double centerX, double centerY) {
    List<Point2D> points = new ArrayList<>();

    for (int i = 0; i <= 5; i++) {
      double angleDeg = 60 * i - 30;
      double angleRad = Math.PI / 180 * angleDeg;

      points.add(new Point2D.Double(
              centerX + HEX_SIZE * Math.cos(angleRad),
              centerY - HEX_SIZE * Math.sin(angleRad)));
    }

    return points;
  }

  /**
   * Selects the hex, and highlight it, at the coordinates of the given mouseEvent.
   *
   * @param e the mouse event
   */
  public void selectHex(MouseEvent e) {
    try {
      Tile hex = pixelToHex(e.getX(), e.getY());

      if (selectedHex != null) {
        drawHexAndDisc(selectedHex, Color.GRAY);
        if (hex == null || hex.equals(selectedHex)) {
          selectedHex = null;
        } else {
          drawHexAndDisc(hex, Color.CYAN);
          selectedHex = hex;
        }
      } else {
        if (hex != null) {
          drawHexAndDisc(hex, Color.CYAN);
          selectedHex = hex;
        }
      }

      this.printMove(hex);
    } catch (IllegalArgumentException ex) {
      if (selectedHex != null) {
        drawHexAndDisc(selectedHex, Color.GRAY);
      }
    }
  }

  /**
   * Returns the pixel coordinates of the given hex.
   *
   * @param hex the hex
   * @return the pixel coordinates of the given hex
   */
  private Point2D toPixel(Tile hex) {
    if (hex == null) {
      return null;
    }
    double x = HEX_SIZE * (Math.sqrt(3) * hex.getQ() + Math.sqrt(3) / 2 * hex.getR());
    double y = HEX_SIZE * (3.0 / 2 * hex.getR());
    return new Point2D.Double(x, y);
  }

  /**
   * Returns the hex at the given pixel coordinates.
   *
   * @param x the x pixel coordinate
   * @param y the y pixel coordinate
   * @return the hex coordinates of the hex at the given pixel coordinates
   */
  private Tile pixelToHex(int x, int y) {
    int side = model.getSideLen();
    double q = x + ((side - 2) * Math.sqrt(3) / 2 * HEX_SIZE);
    double r = y - (2 * HEX_SIZE);
    q = (Math.sqrt(3) / 3 * q - 1.0 / 3 * r) / HEX_SIZE;
    r = 2.0 / 3 * r / HEX_SIZE;
    return hexRound(q, r);
  }

  /**
   * Returns the hex at the given coordinates, rounded to the nearest hex.
   *
   * @param q the q coordinate
   * @param r the r coordinate
   * @return the hex at the given coordinates, rounded to the nearest hex
   */
  private Tile hexRound(double q, double r) {
    int qR = (int) Math.round(q);
    int rR = (int) Math.round(r);
    int sR = (int) Math.round(-q - r);
    double qDiff = Math.abs(qR - q);
    double rDiff = Math.abs(rR - r);
    double sDiff = Math.abs(sR - (-q - r));
    if (qDiff > rDiff && qDiff > sDiff) {
      qR = -rR - sR;
    } else if (rDiff > sDiff) {
      rR = -qR - sR;
    }
    return model.getTileAt(qR, rR);
  }

  /**
   * Converts the given mouse event to hex coordinates.
   *
   * @param e the mouse event
   * @return the hex coordinates of the given mouse event
   */
  public Point2D convertCoords(MouseEvent e) {
    // check for turn
    Tile hex = pixelToHex(e.getX(), e.getY());
    return new Point(hex.getQ(), hex.getR());
  }

  /**
   * Prints the move at the given Tile.
   *
   * @param h the hex
   */
  private void printMove(Tile h) {
    try {
      System.out.println("Highlighted hex at: " + h.getQ() + ", " + h.getR());
    } catch (NullPointerException ignored) {
    } catch (IllegalArgumentException ex) {
      System.out.println("Invalid move");
    }
  }

  /**
   * Shows the current turn.
   * Updates the view to the current model
   **/
  public void updateView() {
    this.repaint();
  }

  /**
   * Shows the given message in a pop-up window.
   *
   * @param message the message to show
   */
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

}
