package cs3500.pawnsboard.model;

import java.util.Objects;

/**
 * Represent pawns in the cell of the PawnsBoard. One pawn should have count and its owner at this
 * turn, including Red and Blue. For count of one pawn, the maximum is 3. Pawns' counts also the
 * conditions which justify if the cell can be placed by one specific card.
 */
public class Pawns implements PawnsBoardPawns {
  private Player owner;
  private int count;
  private static final int MAX_PAWNS = 3;

  /**
   * Create a pawn with given player and count.
   * @param owner the owner of this pawn, Red or Blue player
   * @param count the number of pawns in this cell
   * @throws IllegalStateException if the count of pawn is larger than 3
   */
  public Pawns(Player owner, int count) {
    this.owner = owner;
    // class invariant:
    // the count of pawns in one cell cannot larger than 3
    if (count > 3) {
      throw new IllegalArgumentException("The count of pawn cannot be greater than 3");
    }
    this.count = count;
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public int getCount() {
    return Math.max(0, Math.min(count, MAX_PAWNS));
  }

  @Override
  public boolean canPlaceCard(PawnsBoardCard card) {
    return count >= card.getCost();
  }

  @Override
  public void addPawns(Player o) {
    if (count == 0) {
      owner = o;
    }
    if (owner == o) {
      count = Math.min(count + 1, MAX_PAWNS);
    } else {
      owner = o;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Pawns)) {
      return false;
    }
    Pawns other = (Pawns) o;
    return owner.equals(other.owner) && count == other.count;
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, count);
  }
}

