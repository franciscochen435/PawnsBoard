package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * Represents a strategy for choosing a move in the Pawns Board game.
 */
public interface Strategy {
  /**
   * Decide the next move for the current player given the board state.
   * @return an array [cardIndex, row, col] or null if no valid move is found
   */
  int[] chooseMove(ReadonlyPawnsBoardModel model);
}