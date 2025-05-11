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
public class PawnsBoardTextualView implements PawnsBoardView {
  private final ReadonlyPawnsBoardModel model;

  /**
   * Constructs a textual view of the given PawnsBoard model.
   *
   * @param model the game model to be represented textually
   * @throws IllegalArgumentException if the provided model is null
   */
  public PawnsBoardTextualView(PawnsBoardInterface model) {
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
    StringBuilder out = new StringBuilder();

    for (int i = 0; i < row; i++) {
      out.append(score[i][0]);
      out.append(" ");
      for (int j = 0; j < col; j++) {
        if (placeCard[i][j] != null) {
          String c = placeCard[i][j].getOwner().equals(Player.Red) ? "R" : "B";
          out.append(c);
        } else if (pawn[i][j] != null) {
          out.append(pawn[i][j].getCount());
        } else {
          out.append("_");
        }
      }
      out.append(" ");
      out.append(score[i][1]);
      out.append("\n");
    }
    out.append("Red: ").append(model.getRedHand().toString()).append("\n");
    out.append("Blue: ").append(model.getBlueHand().toString());
    return out.toString();
  }
}