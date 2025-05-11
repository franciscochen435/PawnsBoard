package cs3500.pawnsboard.view;

import java.awt.Color;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;

/**
 * Strategy interface for determining visual appearance of the board.
 */
public interface ModelColor {

  /**
   * Gets the background color of a cell.
   * @param pawns the pawn in the cell
   * @param card the card in the cell
   * @param selected whether the cell is currently selected
   * @return the background color
   */
  Color getCellColor(PawnsBoardPawns pawns, PawnsBoardCard card, boolean selected);

  /**
   * Gets the text color for labels (card value, score, etc).
   * @param pawns the pawn in the cell
   * @param card the card in the cell
   * @param selected whether the cell is currently selected
   * @return the text color
   */
  Color getTextColor(PawnsBoardPawns pawns, PawnsBoardCard card, boolean selected);

  /**
   * Gets the color for rendering pawn circles.
   * @param pawns the pawn in the cell
   * @return the color of the pawn circle
   */
  Color getPawnColor(PawnsBoardPawns pawns);
}
