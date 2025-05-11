package cs3500.pawnsboard.controller;

import java.util.List;

import cs3500.pawnsboard.model.ModelListener;
import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.player.PawnsBoardPlayer;
import cs3500.pawnsboard.view.BoardPanel;
import cs3500.pawnsboard.view.HandPanel;
import cs3500.pawnsboard.view.ModelColor;
import cs3500.pawnsboard.view.NewBoardPanel;
import cs3500.pawnsboard.view.PawnsFrame;
import cs3500.pawnsboard.view.PawnsFrameInterface;
import cs3500.pawnsboard.view.PlayerActionListener;
import javax.swing.JOptionPane;

/**
 * A visual controller that handles user input (via PlayerActionListener),
 * orchestrates a turn-based flow between two players (human or AI),
 * and updates each player's view accordingly.
 */
public class PawnsBoardVisualController implements PawnsBoardController, PlayerActionListener,
        ModelListener {
  private final PawnsBoardPlayer player;
  private final PawnsBoardInterface model;
  private final Player color;
  private PawnsFrameInterface boardView;
  private PawnsBoardVisualController opponent;
  private final ModelColor modelColor;


  private int selectedRow = -1;
  private int selectedCol = -1;
  private int selectedCard = -1;

  /**
   * Constructs the visual controller for a single player.
   * with given model, frame, and player.
   * @param model the game model
   * @param boardView this player's frame
   * @param player the PawnsBoardPlayer (human or machine)
   */
  public PawnsBoardVisualController(PawnsBoardInterface model,
                                    PawnsFrame boardView,
                                    PawnsBoardPlayer player,
                                    ModelColor modelColor) {
    if (model == null || boardView == null || player == null) {
      throw new IllegalArgumentException("No null inputs allowed.");
    }
    this.model = model;
    this.player = player;
    this.modelColor = modelColor;
    this.boardView = boardView;
    this.color = boardView.getHandPanel().getPlayer();
    boardView.setActionListener(this);
  }

  /**
   * Sets the opponent's controller for turn transitions.
   *
   * @param opponent the other player's visual controller
   */
  public void setOpponent(PawnsBoardVisualController opponent) {
    this.opponent = opponent;
  }

  /**
   * Starts this player's turn:
   * 1) Reset selections;
   * 2) Refresh both frame;
   * 3) If AI, auto-play; otherwise show the board.
   */
  @Override
  public void playGame() {
    if (model.isGameOver()) {
      endGame();
      return;
    }

    // Clear out any leftover selections from last turn
    selectedRow = -1;
    selectedCol = -1;
    selectedCard = -1;

    updateView();

    if (opponent != null) {
      opponent.updateView();
    }
    if (player.isAuto()) {
      performAIMove();
      if (opponent != null) {
        opponent.playGame();
      }
    } else {
      boardView.makeVisible();
    }
  }

  /**
   * The AI attempts to place a card. If that fails, it passes
   * (which increments passCount). Then we check gameOver.
   */
  private void performAIMove() {
    try {
      player.placeCard();
    } catch (Exception e) {
      player.passTurn();
    }
  }

  @Override
  public void onCellSelected(int row, int col) {
    selectedRow = row;
    selectedCol = col;
  }

  @Override
  public void onCardSelected(int cardIndex) {
    selectedCard = cardIndex;
  }

  @Override
  public void onConfirm() {
    if (player.isAuto() || model.getTurn() != color) {
      return;
    }

    if (selectedCard == -1 || selectedRow == -1 || selectedCol == -1) {
      return;
    }

    try {
      model.placeCard(selectedCard, selectedRow, selectedCol);
      updateView();
      if (opponent != null) {
        opponent.playGame();
      }

    } catch (IllegalStateException e) {
      showError(e.getMessage());
      updateView();
    }
  }

  @Override
  public void onPass() {
    if (player.isAuto() || model.getTurn() != color) {
      return;
    }

    try {
      showError("Pass turn!");
      model.passTurn();
      if (opponent != null) {
        opponent.playGame();
      }
    } catch (IllegalStateException e) {
      showError(e.getMessage());
      updateView();
    }
  }

  /**
   * Refreshes this player's board/frame with the latest model state.
   */
  private void updateView() {
    boardView.setVisible(false);
    BoardPanel boardPanel = new NewBoardPanel(model.copy(), modelColor);
    HandPanel handPanel = new HandPanel(getCurrentHand(), color);
    this.boardView = new PawnsFrame(boardPanel, handPanel, model);
    this.boardView.setActionListener(this);
    boardPanel.update();
    this.boardView.makeVisible();
    player.addView(this.boardView);
  }

  /**
   * Display a message with the winner or tie, and end the game (closing frames).
   */
  private void endGame() {
    Player winner = model.getWinner();
    if (winner == null) {
      JOptionPane.showMessageDialog(null, "Tie!");
    } else {
      JOptionPane.showMessageDialog(null, winner + " Won!");
    }

    boardView.setVisible(false);
    if (opponent != null) {
      opponent.boardView.setVisible(false);
    }
  }

  /**
   * Display the message in some specific cases.
   * @param message the string displayed to player
   */
  private void showError(String message) {
    JOptionPane.showMessageDialog(null, message);
  }

  /**
   * Gets the current player's hand from the model.
   */
  private List<PawnsBoardCard> getCurrentHand() {
    if (color == Player.Red) {
      return model.getRedHand();
    } else {
      return model.getBlueHand();
    }
  }

  @Override
  public void onTurn(Player currentTurn) {
    if (currentTurn == this.color) {
      boardView.getHandPanel().update();
      boardView.refresh();
      if (!player.isAuto()) {
        playGame();
      }

    }
  }

}