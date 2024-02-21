# Reversi

## Overview
This is a simple implementation of the game Reversi, played on a tile board. It is a two player game
where the players take turns placing pieces on a board. The goal of the game is to have the most
pieces on the board when the game ends. The game ends when the board is full, when both players pass
their turns consecutively, or when neither player can make a move. A player can make a move by
placing a piece on the board such that it surrounds at least one of the opponent's pieces.
When a player surrounds an opponent's piece, the opponent's piece is flipped to the player's color.

## Quick Start
To play a game of reversi, first, the user must run the jar in the terminal. The user must indicate
the size of the board, the types of players, and if the player is an AI, the strategy/difficulty of
the AI via command line arguments. Our game supports our original implementation for the model,
controller, view and strategies. It also supports the view and strategies provided by our providers 
as well as backwards compatibility between the providers and our strategies.

To play a game of reversi, run the .jar with the following command line arguments: 

The structure of the command line arguments is as follows:

`[board type] [board size] [player 1 type] (optional)[player 1 strategy type]
(optional)[player 1 strategy number] (optional)[player 1 strategies] [player 2 type]
(optional)[player 2 strategy type] (optional)[player 2 strategy number]
(optional)[player 2 strategies]`

- to play using a Square board, type `false` or `f` for board type, any other input will default to a Hex board
- board size can be any integer greater than 2, any other input will default to a 11x11 board
- player 1 and player 2 type can be one of the following:
    - human: a human player
    - h: same as human
    - machine: an AI player
    - m: same as machine
    - any other input will default to a human player
- player 1 and player 2 strategy type represent whether the AI will use one or multiple strategies:
    - to use 1 strategy, type either 'single' or 's'
    - any other input will default to multiple strategies
- enter an integer less than 3 for player 1 and player 2 strategy number if player 1 or player 2 strategy type is multiple, then the user must enter the number of
  strategies they wish for the AI to use
- player 1 and player 2 strategies can be any of the following:
    - capturemax: the AI will try to capture as many pieces as possible
    - avoidnexttocorner: the AI will try to avoid placing pieces next to corners
    - movetocorner: the AI will try to place pieces next to corners
    - strategy1: the adapted GreedyStrategy from our provider
    - strategy2: the adapted AvoidHexNearCornersStrategy from our provider
    - strategy3: the adapted TakeCornerStrategy from our provider

An example of how to manually set up a game with both of our views & strategies:

```java
import model.player.HumanReversiPlayer;
import model.player.Player;
import model.model.ReadonlyReversiModel;
import strategy.CompleteStrategy;
import view.gui.ReversiView;
import controller.NewController;
import model.player.MachineReversiPlayer;
import model.player.ReversiPlayer;

class Main {
  void main(String args[]) {
    ReversiModel model = new BasicReversi();
    ReadonlyReversiModel n = model;
    ReversiPlayer p1 = new HumanReversiPlayer(Player.BLACK); // p1 is a human 
    ReversiPlayer p2 = new MachineReversiPlayer(n, Player.WHITE, new CompleteStrategy(new CaptureMax())); // p2 is an AI, using 1 strategy, capturemax
    ReversiView v1 = new ReversiView(n); // view for player1
    ReversiView v2 = new ReversiView(n); // view for player2 
    NewController c = new ReversiController(model, p1, v1); // controller for player1
    NewController c2 = new ReversiController(model, p2, v2); // controller for player2
    model.startGame(); // starts the game
  }
}
```
To run the game above, the user would type the following in the terminal:
`java -jar Reversi.jar anyInput h machine single capturemax`

An example of how to manually set up a game with player 1 using our implementation and player 2 using our provider's implementation:

```java
import model.player.HumanReversiPlayer;
import strategy.CompleteStrategy;
import adapter.ProvStratToOurStratAdapter;
import model.player.ReversiPlayer;
import provider.strategy.GreedyStrategy;

class Main {
  void main(String args[]) {
    ReversiModel model = new BasicReversi();
    ReadonlyReversiModel n = model;
    ReadOnlyReversiModel adapter = new ModelToProvModelAdapter(model); // adapter for our model to our provider's model interface 
    SimpleReversiView view = new SimpleReversiView(adapter); // view for player 2
    ReversiView v1 = new ReversiView(n); // view for player 1
    ReversiView v2 = new ProvViewToOurViewAdapter(adapter, view); // adapter for our view to our provider's view interface
    ReversiPlayer p1 = new HumanReversiPlayer(Player.BLACK); // p1 is a human
    model.player.ReversiPlayer p2 = new ProvPlayerToOurPlayerAdapter(adapter, new hw06.model.player.MachineReversiPlayer(adapter, Player.WHITE, new CompleteStrategy(new ProvStratToOurStratAdapter(new GreedyStrategy())))); // p2 is an AI, using 1 strategy, greedyStrategy
    NewController c1 = new NewController(model, player1, v1); // controller for player 1
    NewController c2 = new NewController(model, player2, v2); // controller for player 2
  }
}
```
To run the game above, the user would type the following in the terminal:
`java -jar Reversi.jar anyInput h machine single strategy1`

To run a game with the AI using multiple strategies, the user would type the following in the terminal:
`java -jar Reversi.jar anyInput h machine multiple 3 strategy2 strategy3 capturemax`

Note - if player 2 is a human, there are no command-line arguments required after. An example of this is: 
`java -jar Reversi.jar anyInput h h`

Note - when the game is initially launched, the 2 views may be overlapping one another entirely. After 
accepting the first turn notification, try moving the 2 windows around before playing. 


## How To Play
The game starts with 6 pieces placed, 3 for each player, around the center of the board.
To select a tile, simply click on it, if it is your turn. The selected tile will be highlighted in cyan.
To deselect a tile, either click on another tile, click on the selected tile again, or click anywhere off the board (but still inside the window).

To place a piece, first a tile must be selected. If the move is valid, the move will be made, if not.
the user will be alerted and will still be their turn. To make the move, for our view's implementation, press `enter`. For our provider's view, press `spacebar` to make your move.

If no moves are available, `p` allows the user to pass their turn. If both players pass their turn consecutively, the game will end.

To quit the game at any time, press `q`, the `quit` button, or close the window.
## Key Components
### Hexagonal board & Coordinate System
Each tile in the board is represented by a [Hex object](src/hw05/model/Hex.java), it is stored by the model in a [Board object](src/hw05/model/Board.java).
The board is represented as a hexagonal grid. The board is represented as a 2D array of hexagons. Our board uses the 
axial coordinate system, where each hexagon is represented by a pair of coordinates (q, r). The q coordinate represents
the column of the hexagon, and the r coordinate represents the row of the hexagon. The q coordinate increases from left to right,
and the r coordinate increases from top to bottom. 

This is an example of the board with 3 locations: 
```
      1 _ _ _
     _ _ _ _ _          
    _ _ _ _ _ _
   _ _ _ 2 _ _ _ 
    _ _ _ _ _ _
     _ _ _ _ _
      _ _ _ 3
  
```

```1: (3,0)```
```2: (3,3)``` 
```3: (3,6)```

### View
The view is responsible for displaying the board and the pieces on the board. It is also responsible for
handling inputs for moves by a human user. The view is also responsible for displaying the current player's
turn, and the winner of the game. The view is also responsible for displaying the score of each player. 
We currently support our view for player 1 and our provider's view for player 2.
### Controller
The controller is the main driver of the program. It is responsible for communicating with the view, model, 
players (if the player is a human), and strategies (if the player is an AI). The controller is responsible for starting the game,
and ending the game. The controller is also responsible for handling inputs from the user, and passing them to the model.
### Strategies
We have created 3 strategies for AI players to play Reversi with.
1. [**CaptureMax**](src/hw06/strategy/CaptureMax.java) - Always selects the tile with the most possible
   score increase for the current player. If there are ties between multiple locations, the topmost and leftmost tile is selected.
2. [**AvoidNextToCorners**](src/hw06/strategy/AvoidNextToCorner.java) - Avoids placing pieces
   next to corners. If there are no valid moves that do not place a piece next to a corner, the AI will
   place a piece next to a corner. Breaks ties by selecting the top-left most tile. 
3. [**MovetoCorner**](src/hw06/strategy/MovetoCorner.java) - Only can select valid corner tiles for
   the current player. Breaks ties by choosing the top-left corner. 

Our program supports backwards compatibility with our providers strategies, for both single and composite strategies. 
For more information, see quick start. 
### Source Organization
- [hw05/model](src/hw05/model) - Contains the model for the game, including the board, tile, and player classes.
- [hw05/view](src/hw05/view) - Contains the textual representation of the game, including the textual view interface, and the textual view implementation. Used for testing/debugging. 
- [hw06/controller](src/hw06/controller) - contains the ViewFeatures interface, which allows us to decouple the view from the controller and defines the methods that the controller can call on the view.
- [hw06/model](src/hw06/model) - Contains the Readonly Interface, which allows us to decouple the model from the view.
- [hw06/strategy](src/hw06/strategy) - Contains the strategy interface, and the 3 strategies we have implemented. It also contains the implementation for composite strategies. 
- [hw06/view](src/hw06/view) - Contains the graphical representation of the game, including the graphical view interface, and the graphical view implementation.
- [hw07/controller](src/hw07/controller) - Contains the controller for the game, including the controller interface, and the controller implementation.
- [hw07/model](src/hw07/model) - Contains the player implementations and the player interface.
- [hw08/model](src/hw08/model) - Contains the adapter for our model to our provider's model interface.
- [hw08/strategy](src/hw08/strategy) - Contains the adapter for our provider's strategy to our strategy interface.
- [hw08/view](src/hw08/view) - Contains the adapter for our provider's view to our view interface. Also contains the adapter for our ViewFeatures interface to our provider's ReversiFeatures interface.
- [provider](src/provider) - Contains the provider's model, view, and strategy interfaces. Also contains the provider's model, view, and strategy implementations.
- [Reversi](src/Reversi.java) - Contains the main method for the program. Launches a reversi game via command line arguments.
## Changes for part 4:
- added the method hasValidMoves(Player player) to the ReadonlyReversiModel interface, we had the public method from an earlier assignment, but forgot to add it to the interface. 
- added the method placePieceHelper(int q, int r) to the ViewFeatures interface - this method was added to help adapt our provider's view to our view interface. it contains basically the same logic as the previous placePiece() method we had, but
uses the given q and r coordinates rather than delegating to the view to get the coordinates. 
- made fields private in CombinedStrategy, CompleteStrategy, and ReversiGUI to promote encapsulation of data
- added command-line support for provider's view and strategies in the main method. 
## Changes for part 3:
- added getTurn() method to the ReversiModel interface to allow the view to display the current player's turn
- renamed ViewFeatures interface from 'Features' for clarity
- renamed TextualView interface from 'ReversiView' for clarity
- renamed MockModel from 'ModelMock' and moved to test package for clarity
- added notifyPlayerTurn(Player p) method to notify controller of which player's turn it currently is
- added addFeatures(ModelFeatures features) method to add the given listener to the model, allows for communication between the model and controller.
## Changes for part 2: 
- added ReadonlyReversiModel interface to decouple the model from the view
- copyBoard() method added to return a copy of the current board
- moveScore(int q, int r) method added to determine 'value' of a move at a given position, used by Strategies
- getBoard() method added to return the current board, used for testing copyBoard()
- equals() method added to compare two boards, used for testing copyBoard()
- hashCode() method added to compare two boards, used for testing copyBoard()