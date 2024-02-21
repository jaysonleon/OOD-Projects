package view.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.player.Player;
import model.model.ReadonlyReversiModel;
import model.model.Tile;

/**
 * Represents the drawing panel for the Reversi game, played with a square board.
 */
public class SquareReversiPanel extends JPanel {

  private final ReadonlyReversiModel model;

  private Tile selectedCell;
  private static final int CELL_SIZE = 30;

  /**
   * Constructs a SquareReversiPanel object with the given model.
   * @param model the model
   */
  public SquareReversiPanel(ReadonlyReversiModel model) {
    this.model = model;
    this.selectedCell = null;
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension d = new Dimension();
    d.setSize(CELL_SIZE * model.getWidth(), CELL_SIZE * model.getHeight());
    return d;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    this.setBackground(Color.BLACK);

    // set antialiasing for smoother edges
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int rows = model.getWidth();
    int cols = model.getHeight();

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {

        Tile cell = model.getTileAt(r, c);
        Point2D p = this.toPixel(cell);

        if (p == null) {
          continue;
        }

        int x = c * CELL_SIZE;
        int y = r * CELL_SIZE;

        this.drawSquare(g2d, x, y, Color.GRAY);
        this.drawDisc(g2d, x, y, cell.getPlayerAt(), cell);
      }
    }
  }

  /**
   * Draws the given cell and disc with the given color.
   * @param cell the cell to draw
   * @param color the color to draw the cell
   */
  private void drawCellAndDisc(Tile cell, Color color) {
    int x = cell.getR() * CELL_SIZE;
    int y = cell.getQ() * CELL_SIZE;
    this.drawSquare((Graphics2D) this.getGraphics(), x, y, color);
    this.drawDisc((Graphics2D) this.getGraphics(), x, y, cell.getPlayerAt(), cell);
  }

  /**
   * Draws a disc at the given tile, if there is one.
   * @param g the graphics object
   * @param x the x coordinate of the disc
   * @param y the y coordinate of the disc
   * @param color the color of the disc
   * @param cell the tile that the disc is on
   */
  private void drawDisc(Graphics2D g, int x, int y, Player color, Tile cell) {
    double radius = CELL_SIZE / 3.0;
    if (cell.hasPlayer()) {
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

      Ellipse2D.Double disc = new Ellipse2D.Double(x + CELL_SIZE / 2.0 - radius,
              y + CELL_SIZE / 2.0 - radius, radius * 2, radius * 2);
      g.fill(disc);
    }
  }

  /**
   * Draws a square with the given color at the given coordinates.
   * @param g2d the graphics object
   * @param x the x coordinate of the square
   * @param y the y coordinate of the square
   * @param c the color of the square
   */
  private void drawSquare(Graphics2D g2d, int x, int y, Color c) {
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
    g2d.setColor(c);
    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
  }

  /**
   * Selects the hex, and highlight it, at the coordinates of the given mouseEvent.
   *
   * @param e the mouse event
   */
  public void selectHex(MouseEvent e) {
    try {
      Point2D coords = convertCoords(e);
      Tile cell = model.getTileAt((int) coords.getX(), (int) coords.getY());

      if (selectedCell != null) {
        drawCellAndDisc(selectedCell, Color.GRAY);
        if (cell == null || cell.equals(selectedCell)) {
          selectedCell = null;
        } else {
          drawCellAndDisc(cell, Color.CYAN);
          selectedCell = cell;
        }
      } else {
        if (cell != null) {
          drawCellAndDisc(cell, Color.CYAN);
          selectedCell = cell;
        }
      }
        this.printMove(cell);
    } catch (IllegalArgumentException ex) {
      if (selectedCell != null) {
        drawCellAndDisc(selectedCell, Color.GRAY);
      }
    }
  }

  /**
   * Returns the pixel coordinates of the given tile.
   *
   * @param cell the tile to convert
   * @return the pixel coordinates of the given tile
   */
  private Point2D toPixel(Tile cell) {
    if (cell == null) {
      return null;
    }

    int x = CELL_SIZE * cell.getQ();
    int y = CELL_SIZE * cell.getR();
    return new Point2D.Double(x, y);
  }

  /**
   * Converts from the given pixel coordinates to a cell in the board
   * @param x the x coordinate
   * @param y the y coordinate
   * @return the cell at the given coordinates
   */
  private Tile pixelToCell(int x, int y) {
    int q = x / CELL_SIZE;
    int r = y / CELL_SIZE - 1;
    return model.getTileAt(q, r);
  }

  /**
   * Converts the given mouse event to cell coordinates.
   *
   * @param e the mouse event
   * @return the cell coordinates of the given mouse event
   */
  public Point2D convertCoords(MouseEvent e) {
    Tile hex = pixelToCell(e.getX(), e.getY());
    return new Point(hex.getR(), hex.getQ());
  }

  /**
   * Prints the move at the given Tile.
   *
   * @param cell the cell to print
   */
  private void printMove(Tile cell) {
    try {
      System.out.println("Highlighted hex at: " + cell.getR() + ", " + cell.getQ());
    } catch (NullPointerException ignored) {
    } catch (IllegalArgumentException ex) {
      System.out.println("Invalid move");
    }
  }

  /**
   * Shows the current turn.
   * Updates the view to the current model
   */
  public void updateView() {
    this.repaint();
  }

  /**
   * Shows the given message in a pop-up window.
   *
   * @param s the message to show
   */
  public void showMessage(String s) { JOptionPane.showMessageDialog(this, s); }
}
