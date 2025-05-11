package cs3500.pawnsboard.model;

import java.util.List;

/**
 * Read-only interface for observing the state of the Pawns Board game.
 * Contains only the observer methods and excludes mutator methods.
 */
public interface ReadonlyPawnsBoardModel {
  /**
   * Retrieves the rows of the board.
   * @return the rows of the board
   */
  int getRow();

  /**
   * Retrieves the columns of the board.
   * @return the columns of the board
   */
  int getColumn();

  /**
   * Retrieves the corresponding deck of Red player or Blue player.
   * @return the deck in this turn
   * @throws IllegalStateException the game has not been started
   */
  List<PawnsBoardCard> getDeck();

  /**
   * Retrieves corresponding cards on the hand of Red player or Blue player.
   * @return the cards on the hand in this turn
   * @throws IllegalStateException the game has not been started
   */
  List<PawnsBoardCard> getHand();

  /**
   * get the hand card of red player,
   * and this method just used in Test and View class for convenience.
   * @return the List of cards on red player's hand
   */
  List<PawnsBoardCard> getRedHand();

  /**
   * get the hand card of blue player,
   * and this method just used in Test and View class for convenience.
   * @return the List of cards on red player's hand
   */
  List<PawnsBoardCard> getBlueHand();

  /**
   * Retrieves whose turn.
   * @return the player who acts in the turn
   */
  Player getTurn();

  /**
   * Retrieves the board full of Pawns.
   * @return the pawns board
   * @throws IllegalStateException the game has not been started
   */
  PawnsBoardPawns[][] getBoard();

  /**
   * Retrieves the board full of Card.
   * @return the bard board
   * @throws IllegalStateException the game has not been started
   */
  PawnsBoardCard[][] getPlacedCards();

  /**
   * get the pass count in this turn.
   * @return the rounds two players continuously pass
   */
  int getPassCount();

  /**
   * calculates the score of each row of two players, and put them in a 2-D array.
   * @return a 2-D array of score of each row of two players.
   * @throws IllegalStateException the game has not been started
   */
  int[][] getScore();

  /**
   * Identify who is the winner by comparing each row's score, and who wins the most rows is the
   * winner. It will appear the tie case.
   * @return which player is the winner or the game is drawn
   * @throws IllegalStateException the game has not been started
   */
  Player getWinner();

  /**
   * check if the game is over by checking if two players continuously pass
   * or the board is full of cards.
   * @return if the game is over
   * @throws IllegalStateException the game has not been started
   */
  boolean isGameOver();

  /**
   * Creates a deep copy of this board state.
   * @return a new ReadonlyPawnsBoardInterface with the same data
   */
  ReadonlyPawnsBoardModel copy();

  int[][] getFutureValueBoard();

  PawnsBoardCard getPawnsBoardCard(int cardIdx,int r, int c);
}
