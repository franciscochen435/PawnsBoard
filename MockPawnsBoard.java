package cs3500.pawnsboard.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock implementation of the PawnsBoard interface for testing the controller.
 * With an StringBuilder input log.
 */
public class MockPawnsBoard implements PawnsBoardInterface {
  private StringBuilder log = new StringBuilder();
  private Player currentTurn = Player.Red;

  /**
   * Create the model mock with the given stringbuilder.
   * @param log the stringbuilder.
   */
  public MockPawnsBoard(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void startGame(List<Card> redDeck, List<Card> blueDeck, boolean shuffle, int handSize) {
    log.append("startGame\n");
  }

  @Override
  public void placeCard(int cardIdx, int row, int col) {
    log.append(String.format("placeCard(%d, %d, %d)\n", cardIdx, row, col));
    currentTurn = (currentTurn == Player.Red) ? Player.Blue : Player.Red;
  }

  @Override
  public void drawCard() {
    log.append("drawCard\n");
  }

  @Override
  public void passTurn() {
    log.append("passTurn\n");
    currentTurn = (currentTurn == Player.Red) ? Player.Blue : Player.Red;
  }

  @Override
  public void addModelListener(ModelListener listener) {

  }

  @Override
  public int getRow() {
    return 3;
  }

  @Override
  public int getColumn() {
    return 3;
  }

  @Override
  public Player getTurn() {
    return currentTurn;
  }

  @Override
  public List<Card> getHand() {
    return new ArrayList<>();
  }

  @Override
  public List<Card> getRedHand() {
    return new ArrayList<>();
  }

  @Override
  public List<Card> getBlueHand() {
    return new ArrayList<>();
  }

  @Override
  public List<Card> getDeck() {
    return new ArrayList<>();
  }

  @Override
  public Pawns[][] getBoard() {
    return new Pawns[3][3];
  }

  @Override
  public Card[][] getPlacedCards() {
    return new Card[3][3];
  }

  @Override
  public int getPassCount() {
    return 0;
  }

  @Override
  public int[][] getScore() {
    return new int[3][2];
  }

  @Override
  public Player getWinner() {
    return Player.Red;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public ReadonlyPawnsBoardModel copy() {
    return this;
  }
}
