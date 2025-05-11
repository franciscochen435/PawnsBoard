package cs3500.pawnsboard.player;

import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.strategy.Strategy;
import cs3500.pawnsboard.view.PawnsFrameInterface;

/**
 * Represents a human player during a game of Pawns Board.
 * It communicates with the view to place cards on the model.
 */
public class HumanPlayer implements PawnsBoardPlayer {
  PawnsBoardInterface model;
  PawnsFrameInterface boardView;

  /**
   * Constructor for the HumanPlayer class.
   * @param model the model of the game that is being played
   */
  public HumanPlayer(PawnsBoardInterface model) {
    if (model == null) {
      throw new IllegalArgumentException("Cannot have null input");
    }
    this.model = model;

  }

  @Override
  public void addView(PawnsFrameInterface boardView) {
    if (boardView == null) {
      throw new IllegalStateException("No Null View");
    }
    this.boardView = boardView;
  }

  @Override
  public void addStrategy(Strategy strategy) {
    return;
  }

  @Override
  public PawnsBoardInterface placeCard() {
    if (boardView.getSelectedCell()[0] == -1 || boardView.getSelectedCell()[1] == -1) {
      throw new IllegalStateException("No Location Selected");
    }
    int row = boardView.getSelectedCell()[0];
    int col = boardView.getSelectedCell()[1];
    if (boardView.getSelectedCard() == -1) {
      throw new IllegalStateException("No Card Selected");
    }
    int handIdx = boardView.getSelectedCard();
    model.drawCard();
    model.placeCard(handIdx, row, col);
    return model;
  }

  @Override
  public PawnsBoardInterface passTurn() {
    model.passTurn();
    return model;
  }

  @Override
  public boolean isAuto() {
    return false;
  }
}
