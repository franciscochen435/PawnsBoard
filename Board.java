package cs3500.pawnsboard.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a board for the Pawns game. The board consists of a grid where pawns
 * and cards are placed. The game involves two players, Red and Blue, who take turns
 * placing cards and influencing the board or just draw a card from the deck.
 * The board maintains the state of the game, including the decks, hands, and turn management.
 * When the game is over, the mechanism will calculate the score and get a winner in this game.
 *
 * <p>About the coordinate system used: The origin is at the left-top corner of the board,
 * in other words, the row starts from 0, and increases when falling down; the column starts from
 * 0 and increases when going right.
 * The hand is started from left to right, and the first index is known as 0.
 */
public class Board implements PawnsBoardInterface {
  private final Random rand;
  private final int row;
  private final int col;
  private PawnsBoardPawns[][] board;
  private PawnsBoardCard[][] placedCards;
  private List<PawnsBoardCard> redDeck;
  private List<PawnsBoardCard> blueDeck;
  private List<PawnsBoardCard> redHand;
  private List<PawnsBoardCard> blueHand;
  private Player turn;
  private int passCount = 0;
  private boolean isGameStart;
  private final List<ModelListener> listeners = new ArrayList<>();

  /**
   * Constructs a new Board with the given number of rows and columns and random.
   */
  public Board(int row, int col) throws IOException {
    this(row, col, new Random());
  }

  /**
   * Constructs a new Board with the given number of rows and columns and random.
   * Initializes the game board, sets up the initial placement of pawns, and
   * sets the starting turn to the Red player.
   *
   * @param row the number of rows on the board (must be at least 1)
   * @param col the number of columns on the board (must be at least 1 and an odd number)
   * @throws IllegalArgumentException if the row or column is less than 1 or if the column is even
   * @throws IOException if an I/O operation fails during initialization
   */
  public Board(int row, int col, Random rand) throws IOException {
    this.board = new Pawns[row][col];
    this.placedCards = new Card[row][col];

    // class invariant:
    // Considering that the row of the board must be positive and col must be positive and odd.
    if (row < 1  || col < 1 || col % 2 == 0) {
      throw new IllegalArgumentException("Invalid row or column");
    }
    this.row = row;
    this.col = col;
    this.turn = Player.Red;
    this.isGameStart = false;
    this.rand = rand;

    for (int i = 0; i < row; i++) {
      board[i][0] = new Pawns(Player.Red, 1);  // Red pawns in first column
      board[i][col - 1] = new Pawns(Player.Blue, 1);  // Blue pawns in last column
    }
  }

  @Override
  public void startGame(List<PawnsBoardCard> rDeck, List<PawnsBoardCard> bDeck, boolean shuffle,
                        int handSize) {
    if (isGameStart) {
      throw new IllegalStateException("Game already started");
    }

    if (3 * handSize > rDeck.size() || 3 * handSize > bDeck.size()) {
      throw new IllegalArgumentException("Hand size is too large.");
    }

    if (rDeck.size() + bDeck.size() < this.col * this.row) {
      throw new IllegalArgumentException("Not enough cards in the deck to start the game.");
    }

    this.redDeck = new ArrayList<>(rDeck);
    this.blueDeck = new ArrayList<>(bDeck);
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();

    if (shuffle) {
      Collections.shuffle(this.redDeck, this.rand);
      Collections.shuffle(this.blueDeck, this.rand);
    }

    for (int i = 0; i < handSize; i++) {
      this.redHand.add(this.redDeck.remove(0));
      this.blueHand.add(this.blueDeck.remove(0));
    }

    // Red starts the game
    this.turn = Player.Red;
    this.isGameStart = true;
  }


  @Override
  public int getRow() {
    return row;
  }

  @Override
  public int getColumn() {
    return col;
  }

  @Override
  public Player getTurn() {
    return turn;
  }

  @Override
  public List<PawnsBoardCard> getHand() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }
    return new ArrayList<>(turn == Player.Red ? redHand : blueHand);
  }

  @Override
  public List<PawnsBoardCard> getRedHand() {
    return redHand;
  }

  @Override
  public List<PawnsBoardCard> getBlueHand() {
    return blueHand;
  }

  @Override
  public List<PawnsBoardCard> getDeck() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }
    return turn == Player.Red ? redDeck : blueDeck;
  }

  @Override
  public PawnsBoardPawns[][] getBoard() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }
    return this.board;
  }

  @Override
  public PawnsBoardCard[][] getPlacedCards() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }
    return this.placedCards;
  }

  @Override
  public void placeCard(int cardIdx, int r, int c) {
    PawnsBoardCard card = getPawnsBoardCard(cardIdx, r, c);

    // Apply influence grid
    String[] influence = card.getInfluence();
    for (int i = -2; i <= 2; i++) {
      for (int j = -2; j <= 2; j++) {
        int newRow = r + i;
        int newCol = c + j;
        if (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col) {
          if (influence[i + 2].charAt(j + 2) == 'I') {
            if (board[newRow][newCol] == null) {
              board[newRow][newCol] = new Pawns(turn, 1);
            } else {
              board[newRow][newCol].addPawns(turn);
            }
          }
        }
      }
    }

    board[r][c] = null;
    placedCards[r][c] = card;
    placedCards[r][c].setOwner(turn);
    if (turn == Player.Red) {
      redHand.remove(cardIdx);
    } else {
      blueHand.remove(cardIdx);
    }
    passCount = 0;

    drawCard();
    turn = (turn == Player.Red) ? Player.Blue : Player.Red;
    notifyListeners();
  }

  @Override
  public PawnsBoardCard getPawnsBoardCard(int cardIdx, int r, int c) {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }

    if (r < 0 || r >= row || c < 0 || c >= col) {
      throw new IllegalArgumentException("Invalid position.");
    }

    if (placedCards[r][c] != null) {
      throw new IllegalStateException("Card already placed at " + r + c);
    }

    List<PawnsBoardCard> currentHand = this.getHand();
    if (cardIdx < 0 || cardIdx >= currentHand.size()) {
      throw new IllegalArgumentException("Invalid card index.");
    }

    PawnsBoardCard card = currentHand.get(cardIdx);
    PawnsBoardPawns cell = board[r][c];

    if (cell == null || cell.getOwner() != turn || !cell.canPlaceCard(card)) {
      throw new IllegalStateException("Not enough pawns to place this card.");
    }
    return card;
  }

  @Override
  public void drawCard() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }

    List<PawnsBoardCard> currentDeck = (turn == Player.Red) ? redDeck : blueDeck;
    List<PawnsBoardCard> currentHand = (turn == Player.Red) ? redHand : blueHand;

    if (!currentDeck.isEmpty()) {
      currentHand.add(currentDeck.remove(0));
    } else {
      throw new IllegalStateException("No cards left in the deck to draw.");
    }
    notifyListeners();

  }

  @Override
  public void passTurn() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }
    drawCard();
    turn = (turn == Player.Red) ? Player.Blue : Player.Red;
    passCount++;
    notifyListeners();
  }

  @Override
  public int getPassCount() {
    return passCount;
  }

  @Override
  public int[][] getScore() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }

    int[][] score = new int[row][2];
    for (int row = 0; row < placedCards.length; row++) {
      int red = 0;
      int blue = 0;
      for (int col = 0; col < placedCards[row].length; col++) {
        try {
          if (placedCards[row][col].getOwner().equals(Player.Red)) {
            red += placedCards[row][col].getValue();
          } else if (placedCards[row][col].getOwner().equals(Player.Blue)) {
            blue += placedCards[row][col].getValue();
          }
        } catch (NullPointerException ignored) {
        }
      }
      score[row][0] = red;
      score[row][1] = blue;
    }
    return score;
  }

  @Override
  public Player getWinner() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }
    int[][] score = getScore();
    int blue = 0;
    int red = 0;
    for (int i = 0; i < row; i++) {
      if (score[i][0] > score[i][1]) {
        red += score[i][0];
      } else if (score[i][0] < score[i][1]) {
        blue += score[i][1];
      }
    }
    if (blue > red) {
      return Player.Blue;
    } else if (red > blue) {
      return Player.Red;
    } else {
      return null;
    }
  }

  @Override
  public boolean isGameOver() {
    if (!this.isGameStart) {
      throw new IllegalStateException("Game has not been started.");
    }

    if (passCount == 2) {
      return true;
    }

    for (int r = 0; r < row; r++) {
      for (int c = 0; c < col; c++) {
        if (placedCards[r][c] == null) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Board)) {
      return false;
    }
    Board other = (Board) o;
    return row == other.row && col == other.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  @Override
  public ReadonlyPawnsBoardModel copy() {
    try {
      Board copy = new Board(this.row, this.col, this.rand);
      copy.isGameStart = this.isGameStart;
      copy.turn = this.turn;
      copy.passCount = this.passCount;

      // Deep copy the board
      copy.board = new Pawns[this.row][this.col];
      for (int i = 0; i < this.row; i++) {
        for (int j = 0; j < this.col; j++) {
          if (this.board[i][j] != null) {
            copy.board[i][j] = new Pawns(this.board[i][j].getOwner(), this.board[i][j].getCount());
          }
        }
      }

      // Deep copy placedCards
      copy.placedCards = new Card[this.row][this.col];
      for (int i = 0; i < this.row; i++) {
        for (int j = 0; j < this.col; j++) {
          if (this.placedCards[i][j] != null) {
            PawnsBoardCard c = this.placedCards[i][j];
            copy.placedCards[i][j] = new Card(
                    c.getName(),
                    c.getCost(),
                    c.getValue(),
                    c.getInfluence().clone(),
                    c.getOwner()
            );
          }
        }
      }

      // Deep copy of the hand
      copy.redDeck = new ArrayList<>();
      for (PawnsBoardCard c : this.redDeck) {
        copy.redDeck.add(new Card(c.getName(), c.getCost(), c.getValue(), c.getInfluence().clone(),
                c.getOwner()));
      }

      copy.blueDeck = new ArrayList<>();
      for (PawnsBoardCard c : this.blueDeck) {
        copy.blueDeck.add(new Card(c.getName(), c.getCost(), c.getValue(), c.getInfluence().clone(),
                c.getOwner()));
      }

      copy.redHand = new ArrayList<>();
      for (PawnsBoardCard c : this.redHand) {
        copy.redHand.add(new Card(c.getName(), c.getCost(), c.getValue(), c.getInfluence().clone(),
                c.getOwner()));
      }

      copy.blueHand = new ArrayList<>();
      for (PawnsBoardCard c : this.blueHand) {
        copy.blueHand.add(new Card(c.getName(), c.getCost(), c.getValue(), c.getInfluence().clone(),
                c.getOwner()));
      }

      return copy;

    } catch (IOException e) {
      throw new IllegalStateException("Failed to create a copy of the board", e);
    }
  }

  @Override
  public int[][] getFutureValueBoard() {
    return new int[0][];
  }

  @Override
  public void addModelListener(ModelListener listener) {
    System.out.println("Added listeners to model");
    this.listeners.add(listener);
  }

  @Override
  public void setTurn(Player player) {
    turn = player;
  }

  @Override
  public void resetPassCount() {
    passCount = 0;
  }

  private void notifyListeners() {
    System.out.println("Board: Notifying listeners that turn = " + turn);
    for (ModelListener listener : listeners) {
      listener.onTurn(turn);
    }
  }
}
