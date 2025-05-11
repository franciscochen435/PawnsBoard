package cs3500.pawnsboard.view;

/**
 * Interface for the main game frame that displays the Pawns Board UI.
 * Provides methods to refresh and make the window visible.
 */
public interface PawnsFrameInterface {
  /**
   * Refresh the window when users have some interactions.
   */
  void refresh();

  /**
   * Make the frame visible.
   */
  void makeVisible();

  /**
   * Return the selected cell.
   * @return the array of position of selected cell.
   */
  int[] getSelectedCell();

  /**
   * returns the selected card.
   * @return the index of selected card on the hand
   */
  int getSelectedCard();

  /**
   * Return the hand panel.
   * @return the hand panel
   */
  HandPanel getHandPanel();

  /**
   * set listener to receive the update message from model, and update the view.
   * @param listener the listener listening to the interaction of player.
   */
  void setActionListener(PlayerActionListener listener);

  /**
   * make the view visible depending on if the view need.
   * @param visible if the view should be visible
   */
  void setVisible(boolean visible);

}