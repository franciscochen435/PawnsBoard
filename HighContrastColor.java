package cs3500.pawnsboard.view;

import java.awt.Color;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.Player;

/**
 * Represent a model with high contrast color, the board color is black, the text is always black,
 * and red card or pawns are pure red, blue cards or pawns are highlighted color.
 * Highlight color is yellow.
 */
public class HighContrastColor implements ModelColor {

  @Override
  public Color getCellColor(PawnsBoardPawns pawns, PawnsBoardCard card, boolean selected) {
    if (selected) {
      return Color.YELLOW;
    }
    if (card != null) {
      return card.getOwner() == Player.Red ? Color.RED : Color.CYAN;
    }
    return Color.BLACK;
  }

  @Override
  public Color getTextColor(PawnsBoardPawns pawns, PawnsBoardCard card, boolean selected) {
    if (selected) {
      return Color.BLACK;
    }
    if (card != null) {
      return Color.BLACK;
    }
    return Color.WHITE;
  }

  @Override
  public Color getPawnColor(PawnsBoardPawns pawns) {
    if (pawns == null) {
      return Color.GRAY;
    }
    return pawns.getOwner() == Player.Red ? Color.RED : Color.CYAN;
  }
}
