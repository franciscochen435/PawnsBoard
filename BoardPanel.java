package cs3500.pawnsboard.view;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardPawns;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * A JPanel that visualizes the state of the Pawns Board game.
 * This panel displays pawns, placed cards, and scores in a grid layout.
 * It also highlights the selected cell when clicked by the user.
 */
public class BoardPanel extends JPanel implements PawnsPanel {
  private final ReadonlyPawnsBoardModel model;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private final Color red = new Color(230, 73, 62);
  private final Color blue = new Color(61, 125, 209);
  private PlayerActionListener listener;

  /**
   * Create a board panel with given read-only pawns board model.
   * @param model the read-only pawns board model
   */
  public BoardPanel(ReadonlyPawnsBoardModel model) {
    this.model = model;
    this.setLayout(new GridLayout(model.getRow(), model.getColumn() + 2));
  }

  /**
   * print the position when the mouse clicked on one cell.
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  public void setSelectedCell(int row, int col) {
    this.selectedRow = row;
    this.selectedCol = col;
    System.out.println("Cell clicked at: " + row + ", " + col);
    update();
  }

  public int[] getSelectedCell() {
    return new int[]{selectedRow, selectedCol};
  }

  @Override
  public void update() {
    this.removeAll();
    PawnsBoardPawns[][] pawns = model.getBoard();
    PawnsBoardCard[][] cards = model.getPlacedCards();
    int[][] score = model.getScore();

    for (int row = 0; row < model.getRow(); row++) {
      renderRow(row, pawns, cards, score);
    }

    this.revalidate();
    this.repaint();
  }

  /**
   * Renders a single row of the board, including:
   * - Left score label (Red player);
   * - Each cell on the board (either with pawns, cards, or empty);
   * - Right score label (Blue player).
   *
   * @param row the index of the row
   * @param pawns the 2D array of pawns on the board
   * @param cards the 2D array of cards placed on the board
   * @param score the score array of the row
   */
  protected void renderRow(int row, PawnsBoardPawns[][] pawns, PawnsBoardCard[][] cards,
                           int[][] score) {
    this.add(new JLabel(String.valueOf(score[row][0]), SwingConstants.CENTER));

    for (int col = 0; col < model.getColumn(); col++) {
      this.add(createCellPanel(row, col, pawns[row][col], cards[row][col]));
    }

    this.add(new JLabel(String.valueOf(score[row][1]), SwingConstants.CENTER));
  }

  /**
   * Creates a single cell JPanel for the board grid.
   * The cell displays:
   * - Pawn circles (based on owner and count)
   * - Card value (if a card is placed)
   * - Highlighting for selection
   * Also sets up a mouse listener to notify the controller when clicked.
   *
   * @param row the row of this cell
   * @param col the column of this cell
   * @param pawn the pawn data (can be null)
   * @param card the card data (can be null)
   * @return a JPanel representing the board cell at (row, col)
   */
  protected JPanel createCellPanel(int row, int col, PawnsBoardPawns pawn, PawnsBoardCard card) {
    JPanel cell = new JPanel() {
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (card == null && pawn != null) {
          g.setColor(pawn.getOwner() == Player.Red ? red : blue);
          int count = pawn.getCount();
          int circleSize = Math.min(getWidth(), getHeight()) / 4;
          int spacing = circleSize + 2;
          int totalWidth = spacing * count;
          int startX = (getWidth() - totalWidth + circleSize) / 2;
          int y = (getHeight() - circleSize) / 2;
          for (int k = 0; k < count; k++) {
            int x = startX + k * spacing;
            g.fillOval(x, y, circleSize, circleSize);
          }
        }
      }
    };

    cell.setLayout(new BorderLayout());
    cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));

    if (card != null) {
      cell.setBackground(card.getOwner() == Player.Red ? red : blue);
      JLabel valueLabel = new JLabel(String.valueOf(card.getValue()), SwingConstants.CENTER);
      valueLabel.setForeground(Color.WHITE);
      cell.add(valueLabel, BorderLayout.CENTER);
    }

    if (row == selectedRow && col == selectedCol) {
      cell.setBackground(Color.CYAN);
    }

    cell.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        setSelectedCell(row, col);
        if (listener != null) {
          listener.onCellSelected(row, col);
        }
      }
    });
    return cell;
  }

  @Override
  public void setActionListener(PlayerActionListener listener) {
    this.listener = listener;
  }

  protected PlayerActionListener getListener() {
    return this.listener;
  }

  protected ReadonlyPawnsBoardModel getModel() {
    return this.model;
  }
}