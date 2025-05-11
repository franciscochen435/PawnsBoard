package cs3500.pawnsboard.player;

import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;
import cs3500.pawnsboard.strategy.Strategy;
import cs3500.pawnsboard.view.PawnsFrameInterface;

/**
 * Represents a machine player during a game of pawns board.
 * It picks moves based on a strategy that is given to it.
 */
public class MachinePlayer implements PawnsBoardPlayer {
  PawnsBoardInterface model;
  Strategy strategy;
  int row = -1;
  int col = -1;
  int cardIdx = -1;

  /**
   * Setup for a new MachinePlayer.
   * @param model the model that the current game of pawns board
   *              is being played on.
   */
  public MachinePlayer(PawnsBoardInterface model) {
    if (model == null) {
      throw new IllegalArgumentException("No Null Inputs");
    }
    this.model = model;

  }

  @Override
  public void addStrategy(Strategy strategy) {
    if (strategy == null) {
      throw new IllegalArgumentException("No Null Strategy");
    }
    this.strategy = strategy;
  }

  @Override
  public PawnsBoardInterface placeCard() {
    ReadonlyPawnsBoardModel copy = model.copy();
    int[] move  = strategy.chooseMove(copy);
    if (move.length == 3) {
      cardIdx = move[0];
      row = move[1];
      col = move[2];
      model.placeCard(cardIdx, row, col);
    } else {
      this.passTurn();
      throw new IllegalArgumentException("Error");
    }
    return model;
  }

  @Override
  public PawnsBoardInterface passTurn() {
    model.passTurn();
    return model;
  }

  @Override
  public void addView(PawnsFrameInterface view) {
    return;
  }

  @Override
  public boolean isAuto() {
    return true;
  }
}
