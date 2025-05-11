package cs3500.pawnsboard.view;

import java.awt.Color;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.Player;

/**
 * Represent a model with original color (low contract).
 * the board color is white, the text is always white,
 * and red card or pawns are mix of pink and red, blue cards or pawns are blue.
 * Highlight color is CYAN.
 */
public class OriginalColor implements ModelColor {
  private final Color red = new Color(255, 175, 163);
  private final Color blue = new Color(61, 125, 209);

  @Override
  public Color getCellColor(PawnsBoardPawns pawns, PawnsBoardCard card, boolean selected) {
    if (selected) {
      return Color.CYAN;
    }

    if (card != null) {
      return card.getOwner() == Player.Red ? red : blue;
    }

    return Color.WHITE;
  }

  @Override
  public Color getTextColor(PawnsBoardPawns pawns, PawnsBoardCard card, boolean selected) {
    return Color.white;
  }

  @Override
  public Color getPawnColor(PawnsBoardPawns pawns) {
    if (pawns == null) {
      return Color.LIGHT_GRAY;
    }
    return pawns.getOwner() == Player.Red ? red : blue;
  }
}
