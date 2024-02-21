package cs3500.klondike.model.hw04;

import java.io.InputStreamReader;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeCard;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Rank;
import cs3500.klondike.model.hw02.Suit;
import cs3500.klondike.model.hw04.hw03.Mock;

/**
 * Constants for testing.
 */
public abstract class Constants {
  Card aS = new KlondikeCard(Suit.SPADE, Rank.ACE);
  Card twoS = new KlondikeCard(Suit.SPADE, Rank.TWO);
  Card threeS = new KlondikeCard(Suit.SPADE, Rank.THREE);
  Card fourS = new KlondikeCard(Suit.SPADE, Rank.FOUR);
  Card aH = new KlondikeCard(Suit.HEART, Rank.ACE);
  Card twoH = new KlondikeCard(Suit.HEART, Rank.TWO);
  Card threeH = new KlondikeCard(Suit.HEART, Rank.THREE);
  Card fourH = new KlondikeCard(Suit.HEART, Rank.FOUR);
  Card aC = new KlondikeCard(Suit.CLUB, Rank.ACE);
  Card twoC = new KlondikeCard(Suit.CLUB, Rank.TWO);
  Card threeC = new KlondikeCard(Suit.CLUB, Rank.THREE);
  Card fourC = new KlondikeCard(Suit.CLUB, Rank.FOUR);
  Card aD = new KlondikeCard(Suit.DIAMOND, Rank.ACE);
  Card twoD = new KlondikeCard(Suit.DIAMOND, Rank.TWO);
  Card threeD = new KlondikeCard(Suit.DIAMOND, Rank.THREE);
  Card fourD = new KlondikeCard(Suit.DIAMOND, Rank.FOUR);
  Card fiveS = new KlondikeCard(Suit.SPADE, Rank.FIVE);
  Card fiveH = new KlondikeCard(Suit.HEART, Rank.FIVE);
  KlondikeModel game;
  KlondikeModel game2;
  List<Card> cards = List.of(aS, twoS, threeS, fourS, aH, twoH, threeH, fourH);
  List<Card> cards2 = List.of(aS, twoS, threeH, fourS, aH, twoH, threeS, fourH);
  List<Card> deck;
  KlondikeController c = new KlondikeTextualController(
          new InputStreamReader(System.in), System.out);
  KlondikeModel m;
  StringBuilder log = new StringBuilder();
  Mock mock = new Mock(log);
  StringBuilder gameLog = new StringBuilder();
}
