package cs3500.pawnsboard.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represent A Card which has name, cost, value, and its influence. The Cost is the value of pawns
 * needed for placement. Value of the number on the Card, which is used to calculate the score.
 * Influence pattern shapes the area tha Card can impact at. Card also has owner.
 */
public class Card implements PawnsBoardCard {
  private final String name;
  private final int cost;
  private final int value;
  private final String[] influence;
  private Player owner;

  /**
   * Create a card with given name, cost, value, and influence grid.
   * @param name the name string of the card
   * @param cost the cost of this value
   * @param value the value of this card
   * @param influence the influence grid of this card
   * @throws IllegalArgumentException if the name is null or empty
   * @throws IllegalArgumentException if the cost is smaller than1 or larger than 3
   * @throws IllegalArgumentException if the value is not positive
   * @throws IllegalArgumentException if the influence grid is empty or null
   */
  public Card(String name, int cost, int value, String[] influence, Player owner) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Card name cannot be null or empty.");
    }
    // class invariant:
    // the cost of card must be between 1 and 3
    if (cost < 1 || cost > 3) {
      throw new IllegalArgumentException("Card cost must be between 1 and 3.");
    }
    // class invariant:
    // the value of card must be positive
    if (value < 0) {
      throw new IllegalArgumentException("Card value must be positive.");
    }
    if (influence == null || influence.length == 0) {
      throw new IllegalArgumentException("Card influence cannot be null or empty.");
    }

    this.name = name;
    this.cost = cost;
    this.value = value;
    this.influence = influence;
    this.owner = owner;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String[] getInfluence() {
    return influence;
  }

  @Override
  public int getCost() {
    return cost;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public void setOwner(Player owner) {
    this.owner = owner;
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return name + " " + cost + " " + value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Card)) {
      return false;
    }
    Card other = (Card) o;
    return other.name.equals(this.name) && other.cost == this.cost
            && other.value == this.value && Arrays.equals(other.influence, this.influence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, cost, value);
  }
}
