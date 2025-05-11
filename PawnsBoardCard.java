package cs3500.pawnsboard.model;

/**
 * Represent A Card which has name, cost, value, and its influence. The Cost is the value of pawns
 * needed for placement. Value of the number on the Card, which is used to calculate the score.
 * Influence pattern shapes the area tha Card can impact at. Card also has owner.
 */
public interface PawnsBoardCard {
  /**
   * Get the name of this card.
   * @return the name of this card
   */
  String getName();

  /**
   * Get the influence grid of this card.
   * @return influence grid of this card
   */
  String[] getInfluence();

  /**
   * Get the cost of this Card.
   * @return the cost of this card
   */
  int getCost();

  /**
   * Get the value of this card.
   * @return the value of this card
   */
  int getValue();

  /**
   * Set the card's owner.
   * @param owner the player who owns this card
   */
  void setOwner(Player owner);

  /**
   * identity who owns this card.
   * @return the player who owns this card
   */
  Player getOwner();
}
