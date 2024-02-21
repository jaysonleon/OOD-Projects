package controller;

import model.player.Player;
import model.model.ReversiModel;
import view.gui.ReversiView;
import model.player.PlayerFeatures;
import model.player.ReversiPlayer;
import model.model.ModelFeatures;

/**
 * Represents the new controller for the completed Reversi game.
 */
public class NewController implements ViewFeatures, ModelFeatures, PlayerFeatures {
  private final ReversiModel model;
  private final ReversiPlayer player;
  private final ReversiView view;

  /**
   * Constructs a new controller for the Reversi game.
   *
   * @param m the model for the game
   * @param p the player for the game
   * @param v the view for the game
   */
  public NewController(ReversiModel m, ReversiPlayer p, ReversiView v) {
    this.model = m;
    this.player = p;
    this.view = v;
    view.addFeatures(this);
    model.addFeatures(this);
    player.addFeatures(this);
  }

  @Override
  public void passTurn() {
    model.pass();
    if (model.isGameOver()) {
      this.showWinner();
      System.exit(0);
    }
  }

  @Override
  public void placePiece() {
    try {
      if (model.getTurn() == player.getPiece()) {
        int q = (int) view.convertCoords().getX();
        int r = (int) view.convertCoords().getY();
        player.makeMove(q, r);
      } else {
        view.showMessage("It is not your turn");
      }
    } catch (IllegalStateException ex) {
      view.showMessage("Illegal move");
    } catch (IllegalArgumentException | NullPointerException ex) {
      //System.out.println("Hex is already occupied");
    }
  }

  @Override
  public void placePieceHelper(int q, int r) {
    try {
      if (model.getTurn() == player.getPiece()) {
        player.makeMove(q, r);
      } else {
        view.showMessage("It is not your turn");
      }
    } catch (IllegalStateException ex) {
      view.showMessage("Illegal move");
    } catch (IllegalArgumentException | NullPointerException ex) {
      //System.out.println("Hex is already occupied");
    }
  }

  @Override
  public void selectHex() {
    if (model.getTurn() == player.getPiece()) {
      view.selectHex();
    } else {
      view.showMessage("It is not your turn");
    }
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }

  @Override
  public void updateView() {
    view.updateView();
    if (model.isGameOver()) {
      this.showWinner();
      //    System.exit(0);
      return;
    }
    if (model.getTurn() == player.getPiece()) {

      if (player.isHuman()) {
        view.showMessage("It's your turn");
        this.placePiece();
      } else {
        player.makeMove();
      }
    }
  }

  @Override
  public void makeMove(int row, int col) {
    model.playMove(row, col);
    view.updateView();
  }

  @Override
  public void pass() {
    model.pass();
    view.updateView();
  }

  @Override
  public void showWinner() {
    view.updateView();
    Player winner = model.determineWinner();
    if (winner == Player.EMPTY) {
      view.showMessage("It's a tie!");
    } else {
      String color = winner == Player.BLACK ? "p1(black)" : "p2(white)";
      view.showMessage("The winner is " + color + "\n" + "Final score:\n"
              + "P1: " + model.getScore(Player.BLACK) + "\n"
              + "P2: " + model.getScore(Player.WHITE)
              + "\n" + "Thanks for playing!");
    }
  }
}
