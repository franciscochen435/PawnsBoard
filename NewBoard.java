package cs3500.pawnsboard.model;

import java.util.List;

/**
 * A decorator for the Board class that implements new
 * influences types: upgrade & devalue.
 */
public class NewBoard implements PawnsBoardInterface {
  private PawnsBoardInterface model;
  private final int[][] futureValueBoard;

  /**
   * Creates a new NewBoard given a pawns board model.
   * @param model The model to be decorated
   */
  public NewBoard(PawnsBoardInterface model) {
    this.model = model;
    this.futureValueBoard = new int[model.getRow()][model.getColumn()];
  }

  @Override
  public void startGame(List<PawnsBoardCard> redDeck, List<PawnsBoardCard> blueDeck,
                        boolean shuffle, int handSize) {
    this.model.startGame(redDeck, blueDeck, shuffle, handSize);
  }

  @Override
  public void placeCard(int cardIdx, int r, int c) {
    PawnsBoardCard card = getPawnsBoardCard(cardIdx, r, c);
    card = applyFutureValueBoost(card, r, c);

    applyInfluence(card.getInfluence(), r, c);

    if (card.getValue() <= 0) {
      getBoard()[r][c] = new Pawns(getTurn(), card.getCost());
    } else {
      getPlacedCards()[r][c] = card;
      getPlacedCards()[r][c].setOwner(getTurn());
    }

    if (getTurn() == Player.Red) {
      getRedHand().remove(cardIdx);
    } else {
      getBlueHand().remove(cardIdx);
    }
    drawCard();
    resetPassCount();
    setTurn(getTurn() == Player.Red ? Player.Blue : Player.Red);
  }

  /**
   * Updates the value of the card with the future value of the same cell.
   * @param card The card being placed
   * @param r row for the future value board
   * @param c col for the future value board
   * @return a new card that has the future value boost applied to it
   */
  private PawnsBoardCard applyFutureValueBoost(PawnsBoardCard card, int r, int c) {
    int boost = futureValueBoard[r][c];
    if (boost != 0) {
      card = new Card(
              card.getName(),
              card.getCost(),
              Math.max(0, card.getValue() + boost),
              card.getInfluence(),
              card.getOwner()
      );
      futureValueBoard[r][c] = 0;
    }
    return card;
  }

  /**
   * Applies the influence of the card being placed.
   * @param influence the influence of the card
   * @param r row of the placed card
   * @param c column of the placed card
   */
  private void applyInfluence(String[] influence, int r, int c) {
    for (int i = -2; i <= 2; i++) {
      for (int j = -2; j <= 2; j++) {
        int newRow = r + i;
        int newCol = c + j;

        if (newRow >= 0 && newCol >= 0) {
          if (newRow < getRow() && newCol < getColumn()) {
            char effect = influence[i + 2].charAt(j + 2);
            switch (effect) {
              case 'I':
                applyInfluenceI(newRow, newCol);
                break;
              case 'U':
                applyInfluenceU(newRow, newCol);
                break;
              case 'D':
                applyInfluenceD(newRow, newCol);
                break;
              default:
                break;
            }
          }
        }
      }
    }
  }

  /**
   * Applying influence I.
   * @param r row to apply influence
   * @param c col to apply influence
   */
  private void applyInfluenceI(int r, int c) {
    if (getBoard()[r][c] == null) {
      getBoard()[r][c] = new Pawns(getTurn(), 1);
    } else {
      getBoard()[r][c].addPawns(getTurn());
    }
  }

  /**
   * Applying influence U.
   * @param r row to apply influence
   * @param c col to apply influence
   */
  private void applyInfluenceU(int r, int c) {
    if (getPlacedCards()[r][c] == null) {
      futureValueBoard[r][c]++;
    } else {
      PawnsBoardCard card = getPlacedCards()[r][c];
      int newValue = card.getValue() + futureValueBoard[r][c] + 1;
      getPlacedCards()[r][c] = new Card(
              card.getName(),
              card.getCost(),
              newValue,
              card.getInfluence(),
              card.getOwner()
      );
    }
  }

  /**
   * Applying influence D.
   * @param r row to apply influence
   * @param c col to apply influence
   */
  private void applyInfluenceD(int r, int c) {
    if (getPlacedCards()[r][c] == null) {
      futureValueBoard[r][c]--;
    } else {
      PawnsBoardCard card = getPlacedCards()[r][c];
      int newValue = card.getValue() + futureValueBoard[r][c] - 1;
      if (newValue <= 0) {
        getBoard()[r][c] = new Pawns(getPlacedCards()[r][c].getOwner(),
                getPlacedCards()[r][c].getCost());
        getPlacedCards()[r][c] = null;
        futureValueBoard[r][c] = 0;
      } else {
        getPlacedCards()[r][c] = new Card(
                card.getName(),
                card.getCost(),
                newValue,
                card.getInfluence(),
                card.getOwner()
        );
      }
    }
  }


  @Override
  public void drawCard() {
    model.drawCard();
  }

  @Override
  public void passTurn() {
    model.passTurn();
  }

  @Override
  public void addModelListener(ModelListener listener) {
    model.addModelListener(listener);
  }

  @Override
  public void setTurn(Player player) {
    model.setTurn(player);
  }

  @Override
  public void resetPassCount() {
    model.resetPassCount();
  }

  @Override
  public int getRow() {
    return model.getRow();
  }

  @Override
  public int getColumn() {
    return model.getColumn();
  }

  @Override
  public List<PawnsBoardCard> getDeck() {
    return model.getDeck();
  }

  @Override
  public List<PawnsBoardCard> getHand() {
    return model.getHand();
  }

  @Override
  public List<PawnsBoardCard> getRedHand() {
    return model.getRedHand();
  }

  @Override
  public List<PawnsBoardCard> getBlueHand() {
    return model.getBlueHand();
  }

  @Override
  public Player getTurn() {
    return model.getTurn();
  }

  @Override
  public PawnsBoardPawns[][] getBoard() {
    return model.getBoard();
  }

  @Override
  public PawnsBoardCard[][] getPlacedCards() {
    return model.getPlacedCards();
  }

  @Override
  public int getPassCount() {
    return model.getPassCount();
  }

  @Override
  public int[][] getScore() {
    return model.getScore();
  }

  @Override
  public Player getWinner() {
    return model.getWinner();
  }

  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  @Override
  public ReadonlyPawnsBoardModel copy() {
    Board baseCopy = (Board) model.copy();
    NewBoard copy;
    copy = new NewBoard(baseCopy);

    copy.model = baseCopy;
    for (int i = 0; i < getRow(); i++) {
      System.arraycopy(this.futureValueBoard[i], 0, copy.futureValueBoard[i], 0, getColumn());
    }

    return copy;
  }

  @Override
  public int[][] getFutureValueBoard() {
    return futureValueBoard;
  }

  @Override
  public PawnsBoardCard getPawnsBoardCard(int cardIdx, int r, int c) {
    return model.getPawnsBoardCard(cardIdx, r, c);
  }
}