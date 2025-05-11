package cs3500.pawnsboard.view;

/**
 * Interface for a view for the PawnsBoard game. The interface assume the shape of the board
 * is the rectangle, and there are two columns beside the rectangle to represent the score in
 * each row.
 */
public interface PawnsBoardView {
  /**
   * Returns a string representation of the board state.
   * @return a textual view of this pawns board game.
   */
  public String toString();
}