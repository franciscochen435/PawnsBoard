package cs3500.pawnsboard.view;

/**
 * A Listener of view to receive the message from the model, and then update to view.
 */
public interface PlayerActionListener {
  /**
   * Called when a player clicks on a cell on the board.
   * @param row the row index of the clicked cell
   * @param col the column index of the clicked cell
   */
  void onCellSelected(int row, int col);

  /**
   * Called when a player clicks on a card in their hand.
   * @param cardIndex the index of the clicked card
   */
  void onCardSelected(int cardIndex);

  /**
   * Called when user presses ENTER.
   * If no selection was made, do nothing.
   * If placement succeeds, also draws a card, then pass turn to opponent.
   */
  void onConfirm();

  /**
   * Called when user presses 'P'.
   * This means pass. We draw a card, then pass turn to opponent.p
   */
  void onPass();
}
