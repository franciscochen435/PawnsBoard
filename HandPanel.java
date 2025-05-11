package cs3500.pawnsboard.view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.Player;

/**
 * A JPanel that visualizes the state of the Pawns Board game.
 * This panel displays cards on the players' hands.
 * It also highlights the selected card when clicked by the user.
 */
public class HandPanel extends JPanel implements PawnsPanel {
  private final List<PawnsBoardCard> hand;
  private int selectedCardIndex = -1;
  private final Color red = new Color(255, 175, 163);
  private final Color blue = new Color(61, 125, 209);
  private final Player player;
  private PlayerActionListener listener;
  private final Font monoBold = new Font(Font.MONOSPACED, Font.BOLD, 15);

  /**
   * create the panel with given list of cards on the hand, and the corresponding player.
   * @param hand the list of cards on the player's hand
   * @param player the corresponding player, Red or Blue
   */
  public HandPanel(List<PawnsBoardCard> hand, Player player) {
    this.hand = hand;
    this.player = player;
    this.setLayout(new GridLayout(1, hand.size(), 5, 5));
    update();
  }

  /**
   * print the index when the mouse clicked on one card.
   * @param index the index of the clicked card on the hand
   */
  public void setSelectedCardIndex(int index) {
    this.selectedCardIndex = index;
    System.out.println("Card clicked at index: " + index + ", Player: " + player);
    update();
  }

  public Player getPlayer() {
    return player;
  }

  public int getSelectedCard() {
    return selectedCardIndex;
  }

  @Override
  public void update() {
    this.removeAll();
    for (int i = 0; i < hand.size(); i++) {
      PawnsBoardCard card = hand.get(i);
      JPanel cardPanel = new JPanel();
      cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
      cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      cardPanel.setOpaque(true);

      cardPanel.setBackground(card.getOwner() == Player.Red ? red : blue);
      if (i == selectedCardIndex) {
        cardPanel.setBackground(Color.CYAN);
      }

      JLabel name = new JLabel("<html><u>" + card.getName() + "</u></html>");
      cardPanel.add(name);
      cardPanel.add(new JLabel("Cost: " + card.getCost()));
      cardPanel.add(new JLabel("Value: " + card.getValue()));

      for (String line : card.getInfluence()) {
        JLabel influence = new JLabel(line);
        influence.setFont(monoBold);
        cardPanel.add(influence);
      }

      final int index = i;
      cardPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          setSelectedCardIndex(index);
          if (listener != null) {
            listener.onCardSelected(index);
          }
        }
      });

      this.add(cardPanel);
    }
    this.revalidate();
    this.repaint();
  }

  @Override
  public void setActionListener(PlayerActionListener listener) {
    this.listener = listener;
  }
}


