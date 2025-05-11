package cs3500.pawnsboard.strategy;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This strategy tries to maximize row score. The strategy goes
 * row by row and tries to win that specific row. Rows are visited
 * from top-down.
 */
public class MaxRowScore implements Strategy {
  private int[] ans = null;

  @Override
  public int[] chooseMove(ReadonlyPawnsBoardModel model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }

    int row = model.getColumn();
    PawnsBoardPawns[][] pawn = model.getBoard();
    PawnsBoardCard[][] placeCard = model.getPlacedCards();
    List<PawnsBoardCard> hand = model.getHand();
    int[][] score = model.getScore();
    Player controller = model.getTurn();
    List<Integer> controllerScore = new ArrayList<>();
    List<Integer> opponentScore = new ArrayList<>();
    switch (controller) {
      case Red:
        for (int[] r : score) {
          controllerScore.add(r[0]);
          opponentScore.add(r[1]);
        }
        break;
      case Blue:
        for (int[] r : score) {
          controllerScore.add(r[1]);
          opponentScore.add(r[0]);
        }
        break;
      default:
        throw new IllegalStateException("No Third Player");
    }

    for (int i = 0; i < placeCard.length; i++) {
      if (controllerScore.get(i) <= opponentScore.get(i)) {
        switch (controller) {
          case Red:
            for (int j = 0; j < row; j++) {
              if (placeCard[i][j] == null) {
                if (check(hand, controllerScore, opponentScore, pawn, i, j)) {
                  return ans;
                }
              }
            }
            break;
          case Blue:
            for (int j = row - 1; j > 0; j--) {
              if (placeCard[i][j] == null) {
                if (check(hand, controllerScore, opponentScore, pawn, i, j)) {
                  return ans;
                }
              }
            }
            break;
          default:
            throw new IllegalStateException("Unexpected");
        }
      }
    }
    return null;
  }

  /**
   * This helper function checks if a card in the player's
   * hand being placed at coordinate (i, j) will result in
   * winning row i. If it finds a suitable card, it will
   * update the ans array which keeps track of the solution.
   * @param hand the hand that is being checked.
   * @param controller a list of all scores for the controller.
   * @param opponent a list of all scores for the opponent.
   * @param pawn the pawn board that has all pawns in play.
   * @param i the row coordinate
   * @param j the col coordinate
   * @return true if an answer is found, false if not.
   */
  private boolean check(List<PawnsBoardCard> hand, List<Integer> controller,
                        List<Integer> opponent, PawnsBoardPawns[][] pawn, int i, int j) {
    try {
      for (int l = 0; l < hand.size(); l++) {
        if (hand.get(l).getCost() <= pawn[i][j].getCount()) {
          if (hand.get(l).getValue() + controller.get(i) > opponent.get(i)) {
            ans = new int[]{l, i, j};
            return true;
          }
        }
      }
    } catch (NullPointerException ignored) { }

    return false;
  }
}