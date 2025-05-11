package cs3500.pawnsboard.model;

/**
 * An observer interface for components that need to be notified
 * when the game model updates the current turn.
 */
public interface ModelListener {
  /**
   * Called by the model when it's the given player's turn.
   *
   * @param currentTurn the player whose turn it is
   */
  void onTurn(Player currentTurn);
}