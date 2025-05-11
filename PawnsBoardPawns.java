package cs3500.pawnsboard.model;

/**
 * Represent pawns in the cell of the PawnsBoard. One pawn should have count and its owner at this
 * turn, including Red and Blue. For count of one pawn, the maximum is 3. Pawns' counts also the
 * conditions which justify if the cell can be placed by one specific card.
 */
public interface PawnsBoardPawns {

  /**
   * Get the owner of the pawns in this cell at this time.
   * @return the owner of pawns in this cell
   */
  Player getOwner();

  /**
   * Get the value of number of pawns in this cell.
   * @return the number of pawns.
   */
  int getCount();

  /**
   * Determine if the card can be placed in this cell, depending on the number of pawns and
   * cost of the card.
   * @param card the card will be placed in this cell
   * @return if the card can be placed in the cell
   */
  boolean canPlaceCard(PawnsBoardCard card);

  /**
   * Add pawns when the cell is influenced by placed card, but it will not increase when the number
   * of pawns has get maximum(3). When the pawns convert into other color,
   * the number will not change.
   * @param o the player in this turn
   */
  void addPawns(Player o);
}
