package cs3500.klondike.controller;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a textual controller for the game of Klondike Solitaire.
 */
public class KlondikeTextualController implements cs3500.klondike.controller.KlondikeController {

  private final Appendable out;
  private final Scanner scan;


  /**
   * Constructor for the KlondikeTextualController.
   * @param in the readable object, used to get input from the user
   * @param out the appendable object, used to write output to the user
   * @throws IllegalArgumentException if the readable or appendable object is null
   */
  public KlondikeTextualController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable or Appendable is null");
    }
    this.scan = new Scanner(in);
    this.out = out;
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                       int numDraw) {
    // check if model is null
    if (model == null) {
      throw new IllegalArgumentException("Model is null");
    }
    // check if deck is null or empty
    if (deck == null || deck.isEmpty()) {
      throw new IllegalStateException("Deck is empty");
    }
    boolean gameStarted = true;
    model.startGame(deck, shuffle, numPiles, numDraw);


    while (gameStarted) {
      try {
        // render the board
        this.renderBoard(model);
        // render the score
        this.renderScore(model);

        if (!scan.hasNext()) {
          throw new IllegalStateException("No input");
        }
        // get the next move
        String move = scan.next();
        if (move.toLowerCase().contains("q")) {
          this.quit(model);
          return;
        }

        // try to make the move requested by the user and catch any exceptions
        try {
          int input1;
          int input2;
          int input3;
          List<Integer> inputs;
          switch (move) {
            case "mpp":
              inputs = collectInputs(3);
              if (inputs.isEmpty()) {
                // quit
                this.quit(model);
                return;
              }
              input1 = inputs.get(0) - 1;
              input2 = inputs.get(1);
              input3 = inputs.get(2) - 1;
              model.movePile(input1, input2, input3);
              break;
            case "mpf":
              inputs = collectInputs(2);
              if (inputs.isEmpty()) {
                // quit
                this.quit(model);
                return;
              }
              input1 = inputs.get(0) - 1;
              input2 = inputs.get(1) - 1;
              model.moveToFoundation(input1, input2);
              break;
            case "mdf":
              inputs = collectInputs(1);
              if (inputs.isEmpty()) {
                // quit
                this.quit(model);
                return;
              }
              input1 = inputs.get(0) - 1;
              model.moveDrawToFoundation(input1);
              break;
            case "md":
              inputs = collectInputs(1);
              if (inputs.isEmpty()) {
                // quit
                this.quit(model);
                return;
              }
              input1 = inputs.get(0) - 1;
              model.moveDraw(input1);
              break;
            case "dd":
              model.discardDraw();
              break;
            default:
              writeMessage("Invalid move. Play again. Invalid moveType entered.");
              break;
          }

        } catch (IllegalStateException e) {
          writeMessage("Invalid move. Play again. Move is not possible.");
        }
        // check if game is over and print the appropriate message, then quit game
        if (model.isGameOver()) {
          int maxScore = deck.size();
          renderFinalResult(model, maxScore);
          gameStarted = false;
        }
      } catch (IllegalArgumentException e) {
        writeMessage("Invalid move. Play again.");
      }
    }
  }

  /**
   * Collect the given number of integer inputs from the user, filter out any non-integer inputs,
   *  excluding "q" or "Q".
   * @param num the number of inputs to collect
   * @return a list of integers representing the inputs
   */
  private List<Integer> collectInputs(int num) {
    List<Integer> ans = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      String next = scan.next();
      if (next.equalsIgnoreCase("q")) {
        return new ArrayList<>();
      }
      try {
        ans.add(Integer.parseInt(next));
      } catch (NumberFormatException ignored) {
        i--;
      }
    }
    return ans;
  }

  /**
   * Write a message to the appendable object.
   * @param message the message to write to the appendable object
   * @throws IllegalStateException if the append fails
   */
  private void writeMessage(String message) throws IllegalStateException {
    try {
      out.append(message).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException("Append failed. ");
    }
  }

  /**
   * Write the appropriate message for when a game is quit.
   * @param m the model of the current game
   */
  private void quit(KlondikeModel m) {
    writeMessage("Game quit!");
    writeMessage("State of game when quit:");
    renderBoard(m);
    renderScore(m);
  }

  /**
   * Render the given model's current board state to the appendable object.
   * @param m the model to render the board of
   */
  private void renderBoard(KlondikeModel m) {
    KlondikeTextualView view = new KlondikeTextualView(m);
    writeMessage(view.toString());
  }

  /**
   * Render the score of the given model to the appendable object.
   * @param m the model to render the score of
   */
  private void renderScore(KlondikeModel m) {
    writeMessage("Score: " + m.getScore());
  }

  /**
   * Render the final result of the game to the appendable object, if the user has won the game,
   *  and the appropriate corresponding message. Using the given model,
   *  if the score is equal to the given maxScore, the game is won. Otherwise, the game is lost.
   * @param m the model to render the final result of
   * @param maxScore the maximum score possible for the game
   */
  private void renderFinalResult(KlondikeModel m, int maxScore) {
    if (m.getScore() == maxScore) {
      renderBoard(m);
      writeMessage("You win!");
    } else {
      renderBoard(m);
      writeMessage("Game over. Score: " + m.getScore());
    }
  }
}
