package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.GameType;
import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * Run a game of Klondike Solitaire interactively on the console.
 */
public final class Klondike {

  /**
   * Main entry point for the program. The first command-line argument must be a string
   * (basic, limited, whitehead), which will decide the gameType to be played.
   * If the gameType is limited, the second command-line argument must be an integer R,
   * which represent the number of times the draw pile can be used.
   * The second and third command-line arguments, P and D, are optional, and represent the number
   * of cascade piles and number of draw cards to be used in the game, respectively.
   * If there is no argument for P and D, use 7 and 3.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("Invalid input");
    }

    String gameType = args[0];
    validateArgs(args);

    GameType type;
    KlondikeModel model = null;
    int r;
    int p = 7;
    int d = 3;
    KlondikeController c = new KlondikeTextualController(new InputStreamReader(System.in),
            System.out);
    System.out.println("Please enter a game type (basic, limited, whitehead).");

    try {
      if (args.length == 1) {
        if (gameType.contains("limited")) {
          throw new IllegalArgumentException("invalid limited input, needs R");
        } else {
          type = GameType.toGameType(gameType);
          model = KlondikeCreator.create(type);
        }
      } else {
        if (args.length == 2) {
          if (gameType.contains("limited")) {
            r = Integer.parseInt(args[1]);
            model = new LimitedDrawKlondike(r);
          } else {
            type = GameType.toGameType(gameType);
            model = KlondikeCreator.create(type);
            p = Integer.parseInt(args[1]);
          }
        }
        if (args.length == 3) {
          if (gameType.contains("limited")) {
            r = Integer.parseInt(args[1]);
            p = Integer.parseInt(args[2]);
            model = new LimitedDrawKlondike(r);
          } else {
            type = GameType.toGameType(gameType);
            model = KlondikeCreator.create(type);
            p = Integer.parseInt(args[1]);
            d = Integer.parseInt(args[2]);
          }
        }
        if (args.length == 4) {
          if (gameType.contains("limited")) {
            r = Integer.parseInt(args[1]);
            p = Integer.parseInt(args[2]);
            d = Integer.parseInt(args[3]);
            model = new LimitedDrawKlondike(r);
          } else {
            type = GameType.toGameType(gameType);
            model = KlondikeCreator.create(type);
            p = Integer.parseInt(args[1]);
            d = Integer.parseInt(args[2]);
          }
        }
      }
      if (model == null) {
        System.out.println("Invalid gameType");
      } else {
        c.playGame(model, model.getDeck(), true, p, d);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid input");
    } catch (IllegalStateException e) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  private static void validateLimitedArgs(String[] args) {
    if (args.length == 1) {
      throw new IllegalArgumentException("invalid limited input, needs R");
    } else {
      try {
        Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("invalid limited input, needs R");
      }
    }
  }

  private static void validateArgs(String[] args) {
    if (args.length == 1) {
      if (args[0].contains("basic") || args[0].contains("whitehead")) {
        return;
      } else {
        throw new IllegalArgumentException("Invalid gameType");
      }
    } else {
      if (args.length > 4) {
        if (args[0].contains("limited")) {
          validateLimitedArgs(args);
        } else {
          throw new IllegalArgumentException("Invalid arguments");
        }
      } else {
        try {
          Integer.parseInt(args[1]);
          if (args.length > 2) {
            Integer.parseInt(args[2]);
          }
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Invalid arguments");
        }
      }
    }
  }
}