package cs3500.pawnsboard.view;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * Represents a textual view of the PawnsBoard game.
 * This class converts the current board state into a string format for display.
 */
public class NewTextualView implements PawnsBoardView {
  private final ReadonlyPawnsBoardModel model;

  /**
   * Constructs a textual view of the given PawnsBoard model.
   *
   * @param model the game model to be represented textually
   * @throws IllegalArgumentException if the provided model is null
   */
  public NewTextualView(PawnsBoardInterface model) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;
  }

  @Override
  public String toString() {
    int row = model.getRow();
    int col = model.getColumn();
    int[][] score = model.getScore();
    PawnsBoardPawns[][] pawn = model.getBoard();
    PawnsBoardCard[][] placeCard = model.getPlacedCards();
    int[][] futureValues = model.getFutureValueBoard();
    StringBuilder out = new StringBuilder();

    for (int i = 0; i < row; i++) {
      out.append(score[i][0]);
      out.append(" ");
      for (int j = 0; j < col; j++) {
        out.append("[");
        if (placeCard[i][j] != null) {
          String c = placeCard[i][j].getOwner().equals(Player.Red) ? "Red" : "Blu";
          out.append(c);
          out.append(placeCard[i][j].getValue());
        } else if (pawn[i][j] != null) {
          String c = pawn[i][j].getOwner().equals(Player.Red) ? "R" : "B";
          out.append(pawn[i][j].getCount()).append(c);
          if (futureValues[i][j] > 0) {
            out.append("+").append(futureValues[i][j]);
          } else if (futureValues[i][j] < 0) {
            out.append(futureValues[i][j]);
          } else {
            out.append("--");
          }

        } else if (futureValues[i][j] != 0) {
          if (futureValues[i][j] > 0) {
            out.append("_+").append(futureValues[i][j]).append("_");
          } else {
            out.append("_").append(futureValues[i][j]).append("_");
          }
        } else {
          out.append("____");
        }
        out.append("]");
        if (j + 1 < col) {
          out.append(" ");
        }
      }
      out.append(" ").append(score[i][1]).append("\n");
    }
    out.append("Red: ").append(model.getRedHand().toString()).append("\n");
    out.append("Blue: ").append(model.getBlueHand().toString());
    return out.toString();
  }
}