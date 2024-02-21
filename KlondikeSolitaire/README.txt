Changes made from Klondike part 1:
- startGame changed to add checking for valid deck, throws IllegalArgumentException
- method added to BasicKlondike: isValidDeck() -> validates the deck in this KlondikeModel
- isGameOver implementation changed to fit constraints in javaDocs
- helper methods for isGameOver:
	- isCascadeEmpty() -> returns true iff the cascade has no cards in any pile
	- movesLeftInCasToCas() -> returns true iff there is at least one valid move left in the cascade to another cascade pile
	- movesLeftInCasToFound() -> returns true iff there is at least one valid move left in the cascade to a foundation pile
	- movesLeftInDrawToCas() -> returns true iff there is at least one valid move left in the draw pile to a cascade pile
	- movesLeftInDrawToFound() -> returns true iff there is at least one valid move left in the draw pile to a foundation pile
	- movesLeftInCas() -> returns true iff there is at least one valid move left in the cascade (either to foundation, or to another cascade pile)
	- movesLeftInDraw() -> returns true iff there is at least one valid move left in the draw (either to a foundation, or to a cascade pile)
	- areFoundsFull() -> returns true if the foundations are full, the foundations are full when the sum of the highest ranks == the # of cards in the deck
Changes made from Klondike part 2:
- renderBoard, renderScore, renderFinalResult added to KlondikeTextualController to improve readability of playGame
- added abstract class AbstractModel to reduce code duplication between BasicKlondike and new Model implementations
- added abstract class Constants to reduce code duplication in tests