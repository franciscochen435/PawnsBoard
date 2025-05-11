package cs3500.pawnsboard.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * A variant board panel that displays future value influence (+/-)
 * from the NewBoard model in addition to normal pawn and card display.
 */
public class NewBoardPanel extends ColorBoardPanel {
  private int[][] futureValueBoard;

  /**
   * create a new board panel with displaying future value influence,
   * with given model and model color pattern.
   * @param model the model implemented in such view.
   * @param modelColor the model color be selected to implement this view.
   */
  public NewBoardPanel(ReadonlyPawnsBoardModel model, ModelColor modelColor) {
    super(model, modelColor);
    this.futureValueBoard = new int[0][0];
  }

  @Override
  public void update() {
    this.futureValueBoard = getModel().getFutureValueBoard();
    super.update();
  }

  @Override
  public JPanel createCellPanel(int row, int col, PawnsBoardPawns pawn, PawnsBoardCard card) {
    JPanel cell = super.createCellPanel(row, col, pawn, card);

    if (futureValueBoard != null
            && row >= 0 && row < futureValueBoard.length
            && col >= 0 && col < futureValueBoard[row].length
            && futureValueBoard[row][col] != 0) {

      JLabel futureValueLabel = new JLabel(
              (futureValueBoard[row][col] > 0 ? "+" : "") + futureValueBoard[row][col],
              SwingConstants.CENTER);
      futureValueLabel.setFont(new Font("Arial", Font.BOLD, 14));
      futureValueLabel.setForeground(futureValueBoard[row][col] > 0
              ? new Color(0, 100, 0)
              : new Color(128, 0, 128));
      JPanel overlay = new JPanel();
      overlay.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
      overlay.add(futureValueLabel);
      cell.add(overlay, BorderLayout.SOUTH);
    }
    return cell;
  }
}
