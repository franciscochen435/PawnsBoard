package cs3500.pawnsboard.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

/**
 * The main window of the Pawns Board game UI.
 * This frame embeds the board panel in the center and the hand panel at the bottom.
 * It is responsible for layout, sizing, and visibility of the overall game interface.
 */
public class PawnsFrame extends JFrame implements PawnsFrameInterface {
  private final BoardPanel boardPanel;
  private HandPanel handPanel;
  private boolean confirm;
  private boolean pass;
  private PlayerActionListener listener;

  /**
   * create a frame for pawns board, including setup the size of board, and input the hand and
   * board panels. Additionally, add the key behavior, printing corresponding argument when user
   * presses specific key.
   */
  public PawnsFrame(BoardPanel boardPanel, HandPanel handPanel, ReadonlyPawnsBoardModel model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.boardPanel = boardPanel;
    this.add(boardPanel, BorderLayout.CENTER);
    Player p = null;
    if (handPanel != null) {
      this.handPanel = handPanel;
      this.add(handPanel, BorderLayout.SOUTH);
      p = handPanel.getPlayer();
    }
    this.setSize(700, 550);
    this.setLocationRelativeTo(null);
    Point loc = this.getLocation();

    this.setTitle(p + " Player");
    if (p == Player.Red) {
      this.setLocation(loc.x - 350, loc.y);
    } else {
      this.setLocation(loc.x + 350, loc.y);
    }

    Player finalP = p;
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          if (model.getTurn() != finalP) {
            JOptionPane.showMessageDialog(null, "Not Your Turn!");
          } else {
            System.out.println(finalP + " Confirm");
            confirm = true;
            if (listener != null) {
              listener.onConfirm();
            }
          }
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
          if (model.getTurn() != finalP) {
            JOptionPane.showMessageDialog(null, "Not Your Turn!");
          } else {
            System.out.println(finalP + " Pass");
            pass = true;
            if (listener != null) {
              listener.onPass();
            }
          }
        }
      }
    });
  }

  @Override
  public HandPanel getHandPanel() {
    return handPanel;
  }

  @Override
  public void refresh() {
    this.revalidate();
    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
    this.requestFocusInWindow();
  }

  @Override
  public int[] getSelectedCell() {
    return boardPanel.getSelectedCell();
  }

  @Override
  public int getSelectedCard() {
    return handPanel.getSelectedCard();
  }

  @Override
  public void setActionListener(PlayerActionListener listener) {
    this.listener = listener;
    boardPanel.setActionListener(listener);
    handPanel.setActionListener(listener);
  }
}



