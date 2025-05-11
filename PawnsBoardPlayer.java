package cs3500.pawnsboard.player;

import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.strategy.Strategy;
import cs3500.pawnsboard.view.PawnsFrameInterface;

/**
 * Interface for a player of the Pawns Board game.
 * Players should be able to place a card or pass
 * the turn.
 */
public interface PawnsBoardPlayer {
  /**
   * Represents a player placing a card on a model.
   * @return an updated model with a card placed.
   */
  PawnsBoardInterface placeCard();

  /**
   * Represents a player passing the turn on a model.
   * @return an updated model with a card placed.
   */
  PawnsBoardInterface passTurn();

  /**
   * Setup used by human player so there is a view to use.
   * @param view the view used by the human player.
   */
  void addView(PawnsFrameInterface view);

  /**
   * Setup used by machine player so it has a strategy to play
   * the game with.
   * @param strategy the strategy used by the machine player
   */
  void addStrategy(Strategy strategy);

  /**
   * Checks if the player is machine or not.
   * @return true if it's a machine player, false otherwise.
   */
  boolean isAuto();
}
