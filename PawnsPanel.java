package cs3500.pawnsboard.view;

/**
 * A JPanel that visualizes the state of the Pawns Board game,including hand and board panel.
 * For each panel, it can display something in GUI view
 * and allow user to click object to highlight.
 */
public interface PawnsPanel {

  /**
   * Refreshes the board view by removing all components and reconstructing the board
   * based on the current state of pawns, cards, and scores from the game model.
   */
  void update();

  void setActionListener(PlayerActionListener listener);
}
