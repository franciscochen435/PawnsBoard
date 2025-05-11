package cs3500.pawnsboard.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * A board panel that uses a ModelColor strategy to render cells
 * in a customizable color scheme (e.g., high contrast, original).
 */
public class ColorBoardPanel extends BoardPanel {
  private final ModelColor modelColor;

  /**
   * Constructs a ColorBoardPanel with the given model and color strategy.
   * @param model the read-only game model
   * @param modelColor the color strategy for rendering the board
   */
  public ColorBoardPanel(ReadonlyPawnsBoardModel model, ModelColor modelColor) {
    super(model);
    this.modelColor = modelColor;
  }

  @Override
  protected JPanel createCellPanel(int row, int col, PawnsBoardPawns pawn, PawnsBoardCard card) {
    int[] selectedCell = getSelectedCell();
    // considering that the ModelColor needs a boolean selected to judge
    // if the cell need be highlighted
    boolean selected = selectedCell[0] >= 0 && selectedCell[1] >= 0
            && row == selectedCell[0] && col == selectedCell[1];

    JPanel cell = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (card == null && pawn != null) {
          g.setColor(modelColor.getPawnColor(pawn));
          int count = pawn.getCount();
          int circleSize = Math.min(getWidth(), getHeight()) / 4;
          int spacing = circleSize + 2;
          int startX = (getWidth() - spacing * count + circleSize) / 2;
          int y = (getHeight() - circleSize) / 2;
          for (int i = 0; i < count; i++) {
            g.fillOval(startX + i * spacing, y, circleSize, circleSize);
          }
        }
      }
    };

    cell.setLayout(new BorderLayout());
    cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    cell.setBackground(modelColor.getCellColor(pawn, card, selected));

    if (card != null) {
      JLabel valueLabel = new JLabel(String.valueOf(card.getValue()), SwingConstants.CENTER);
      try {
        valueLabel.setForeground(modelColor.getTextColor(pawn, card, selected));
      } catch (NullPointerException e) {
        valueLabel.setForeground(Color.WHITE);
      }
      cell.add(valueLabel, BorderLayout.CENTER);
    }

    cell.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setSelectedCell(row, col);
        if (getListener() != null) {
          getListener().onCellSelected(row, col);
        }
      }
    });

    return cell;
  }

}
