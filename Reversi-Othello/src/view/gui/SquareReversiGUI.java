package view.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.*;

import controller.ViewFeatures;
import model.model.ReadonlyReversiModel;

/**
 * Represents the GUI view for the Reversi game played with a square board.
 */
public class SquareReversiGUI extends JFrame implements ReversiView {
  private final SquareReversiPanel panel;
  private final JButton quitButton;
  private MouseEvent lastMouseEvent;

  /**
   * Constructs a SquareReversiGUI object with the given model.
   * @param model the model
   */
  public SquareReversiGUI(ReadonlyReversiModel model) {
    super();
    this.setFocusable(true);
    this.setTitle("Reversi");
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // main panel uses border layout,drawing panel in center and button panel in south
    this.setLayout(new BorderLayout());

    panel = new SquareReversiPanel(model);
    this.add(panel, BorderLayout.CENTER);

    // button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    this.add(buttonPanel, BorderLayout.SOUTH);

    // quit button
    quitButton = new JButton("Quit");
    quitButton.addActionListener((e) -> System.exit(0));
    buttonPanel.add(quitButton);

    setResizable(false);

    this.pack();
    setVisible(true);
  }

  @Override
  public void addFeatures(ViewFeatures viewFeatures) {
    quitButton.addActionListener(evt -> viewFeatures.exitProgram());
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'q') {
          viewFeatures.exitProgram();
        }
        if (e.getKeyChar() == 'p') {
          viewFeatures.passTurn();
        }
        if (e.getKeyChar() == KeyEvent.VK_ENTER && lastMouseEvent != null) {
          try {
            viewFeatures.placePiece();
          } catch (NullPointerException | IllegalArgumentException ex) {
            System.out.println("Hex out of play");
          }
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        /// This method has to be kept in order to implement the KeyListener interface. Since we
        //  are responding to key events using keyTyped, this method is left blank.
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // This method has to be kept in order to implement the KeyListener interface. Since we
        //  are responding to key events using keyTyped, this method is left blank.
      }
    });

    this.addMouseListener(new MouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        lastMouseEvent = e;
        viewFeatures.selectHex();
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        // This method has to be kept in order to implement the MouseListener interface. Since we
        //  are responding to mouse events using mousePressed, this method is left blank.
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // This method has to be kept in order to implement the MouseListener interface. Since we
        //  are responding to mouse events using mousePressed, this method is left blank.
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // This method has to be kept in order to implement the MouseListener interface. Since we
        //  are responding to mouse events using mousePressed, this method is left blank.
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // This method has to be kept in order to implement the MouseListener interface. Since we
        //  are responding to mouse events using mousePressed, this method is left blank.
      }
    });
  }

  @Override
  public void selectHex() {
    panel.selectHex(lastMouseEvent);
  }

  @Override
  public Point2D convertCoords() {
    return panel.convertCoords(lastMouseEvent);
  }

  @Override
  public void updateView() {
    panel.updateView();
  }

  @Override
  public void showMessage(String s) {
    panel.showMessage(s);
  }
}
