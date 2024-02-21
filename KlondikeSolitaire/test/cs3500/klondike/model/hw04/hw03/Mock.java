package cs3500.klondike.model.hw04.hw03;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Mock game model for testing.
 */
public class Mock implements KlondikeModel {
  public StringBuilder log;

  public Mock(StringBuilder log) {
    this.log = log;
  }

  @Override
  public List<Card> getDeck() {
    return null;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    log.append("startGame (").append(deck).append(", ").append(shuffle).append(", ").append(
            numPiles).append(", ").append(numDraw).append(")\n");
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append("movePile (").append(srcPile).append(", ").append(numCards).append(", ").append(
            destPile).append(")\n");
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    log.append("moveDraw (").append(destPile).append(")\n");
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append("moveToFound (").append(srcPile).append(", ").append(foundationPile).append(")\n");
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    log.append("moveDrawToFound (").append(foundationPile).append(")\n");
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    log.append("DD");
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public Card getCardAt(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    return new ArrayList<>();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    return 0;
  }
}
