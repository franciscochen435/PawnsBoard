package cs3500.pawnsboard.model;

import java.util.List;

/**
 * Behavior for a game of pawns board, assuming the board is a rectangle which can be shaped by
 * the user.
 *
 * <p>The game start with a rectangle board, which first column full of red pawns, and last column
 * full of blue pawns. The game ends when two players continuously pass or the board is full.
 *
 * <p>Player can choose to pass or place card each turn. Before the player's motion, they should
 * draw the card from deck.
 *
 * <p>Updated: Now this interface just include some mutable methods.
 */
public interface PawnsBoardInterface extends ReadonlyPawnsBoardModel {

  /**
   * Starts the game with the given decks and hand size. If shuffle is set to true,
   * then the deck is shuffled prior to dealing the hand.
   * @param redDeck the deck the Red player draws from
   * @param blueDeck the deck the Blue player draws from
   * @param shuffle if the deck is shuffled
   * @param handSize the number of cards in the hand in the initial
   * @throws IllegalStateException if the game has started
   * @throws IllegalArgumentException if hand size is larger than one third of deck's size
   * @throws IllegalArgumentException if the deck size is smaller than the number of cells on the
   *                                  board
   */
  void startGame(List<PawnsBoardCard> redDeck, List<PawnsBoardCard> blueDeck,
                 boolean shuffle, int handSize);

  /**
   * Place Card of hands on the specific with the given row and column.
   * @param cardIdx the index of card on the hand
   * @param row the row the card will be placed at
   * @param col the column the card will be placed at
   * @throws IllegalStateException the game has not been started
   * @throws IllegalStateException the cell has been placed a card
   * @throws IllegalArgumentException if the place position is out of index range
   * @throws IllegalArgumentException if the card can not be placed on the cell
   * @throws IllegalArgumentException if the index of hand is out of index range
   *
   */
  void placeCard(int cardIdx, int row, int col);

  /**
   * Allows the current player to draw a card from their deck.
   * If the deck is empty, no card is drawn.
   * @throws IllegalStateException the game has not been started
   * @throws IllegalStateException if the deck is empty
   */
  void drawCard();

  /**
   * pass the turn if the player choose to pass the turn, and then increase the pass count.
   */
  void passTurn();

  void addModelListener(ModelListener listener);

  void setTurn(Player player);

  void resetPassCount();
}