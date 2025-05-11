package cs3500.pawnsboard.model;

/**
 * Represent two players, including Red and Blue.
 */
public enum Player {
  Red("Red"), Blue("Blue");

  private final String disp;

  Player(String disp) {
    this.disp = disp;
  }

  @Override
  public String toString() {
    return disp;
  }
}