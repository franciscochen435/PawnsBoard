package cs3500.pawnsboard.strategy;

import java.util.List;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * A simple strategy for the Pawns Board game that selects the first valid move available,
 * scanning the board from top (row 0) to bottom (row rowCount-1).
 *
 * <p>For Red, columns are scanned left to right (0 → colCount-1).
 * For Blue, columns are scanned right to left (colCount-1 → 0).
 * A move is valid if:
 * - The cell is owned by the current player
 * - The cell has enough pawns to pay the card's cost
 * - The cell has no card placed on it
 */
public class FillFirst implements Strategy {
  private ReadonlyPawnsBoardModel model;

  @Override
  public int[] chooseMove(ReadonlyPawnsBoardModel model) {
    List<PawnsBoardCard> hand = model.getHand();
    PawnsBoardPawns[][] board = model.getBoard();
    Player turn = model.getTurn();

    for (int cardIdx = 0; cardIdx < hand.size(); cardIdx++) {
      PawnsBoardCard card = hand.get(cardIdx);

      for (int r = 0; r < model.getRow(); r++) {

        if (turn == Player.Red) {
          // Left to right
          for (int c = 0; c < model.getColumn(); c++) {
            if (canPlace(board, card, r, c, model)) {
              return new int[] { cardIdx, r, c };
            }
          }
        }
        else if (turn == Player.Blue) {
          // Right to left
          for (int c = model.getColumn() - 1; c >= 0; c--) {
            if (canPlace(board, card, r, c, model)) {
              return new int[] { cardIdx, r, c };
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * Helper method to check if the given card can be placed at board[r][c].
   * @param board the board of pawns.
   * @param card the placed or valid card
   * @param r the row of this card be placed
   * @param c the col of this card be placed
   * @param m the read-only pawns model
   * @return if the given card can be placed at the specific position.
   */
  private boolean canPlace(PawnsBoardPawns[][] board, PawnsBoardCard card,
                           int r, int c, ReadonlyPawnsBoardModel m) {
    return board[r][c] != null
            && board[r][c].getOwner() == m.getTurn()
            && board[r][c].canPlaceCard(card)
            && m.getPlacedCards()[r][c] == null;
  }
}
