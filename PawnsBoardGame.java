package cs3500.pawnsboard;

import cs3500.pawnsboard.controller.DeckReader;

import cs3500.pawnsboard.controller.PawnsBoardVisualController;
import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.NewBoard;
import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.player.HumanPlayer;
import cs3500.pawnsboard.player.MachinePlayer;
import cs3500.pawnsboard.player.PawnsBoardPlayer;
import cs3500.pawnsboard.strategy.FillFirst;
import cs3500.pawnsboard.strategy.MaxRowScore;
import cs3500.pawnsboard.strategy.Strategy;
import cs3500.pawnsboard.view.BoardPanel;
import cs3500.pawnsboard.view.HandPanel;
import cs3500.pawnsboard.view.HighContrastColor;
import cs3500.pawnsboard.view.ModelColor;
import cs3500.pawnsboard.view.NewBoardPanel;
import cs3500.pawnsboard.view.OriginalColor;
import cs3500.pawnsboard.view.PawnsFrame;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

//java -jar pawnsboard.jar docs/redDeck.config docs/blueDeck.config human human og hc

/**
 * The main entry point for the PawnsBoard game.
 * This class initializes the game, reads the deck configurations,
 * manages user input for playing the game, and output GUI view of this game.
 */
public class PawnsBoardGame {
  /**
   * The main method to start and run the PawnsBoard game.
   * It reads the deck configurations, initializes the board,
   * and simulate user input and return the visual view.
   * The output of this class is GUI view.
   * @param args command-line arguments
   * @throws IOException if an error occurs while reading the deck configuration file
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 4) {
      throw new IllegalArgumentException("The Correct Input Format is:" +
              " red deck - blue deck - red player - blue player + " +
              " red high contrast - blue high contrast");
    }

    String[] redPathParts = args[0].split("/");
    String[] bluePathParts = args[1].split("/");

    String redPath = String.join(File.separator, redPathParts);
    String bluePath = String.join(File.separator, bluePathParts);

    List<PawnsBoardCard> redDeck = DeckReader.readDeck(redPath, Player.Red);
    List<PawnsBoardCard> blueDeck = DeckReader.readDeck(bluePath, Player.Blue);

    PawnsBoardInterface model = new NewBoard(new Board(5, 7, new Random()));
    model.startGame(redDeck, blueDeck, true, 5);


    PawnsBoardPlayer redPlayer = null;
    PawnsBoardPlayer bluePlayer = null;
    Strategy fillStrategy = new FillFirst();
    Strategy maxRowStrategy = new MaxRowScore();

    switch (args[2]) {
      case "human":
        redPlayer = new HumanPlayer(model);
        break;
      case "strategy1":
        redPlayer = new MachinePlayer(model);
        redPlayer.addStrategy(fillStrategy);
        break;
      case "strategy2":
        redPlayer = new MachinePlayer(model);
        redPlayer.addStrategy(maxRowStrategy);
        break;
      default:
        break;
    }

    switch (args[3]) {
      case "human":
        bluePlayer = new HumanPlayer(model);
        break;
      case "strategy1":
        bluePlayer = new MachinePlayer(model);
        bluePlayer.addStrategy(fillStrategy);
        break;
      case "strategy2":
        bluePlayer = new MachinePlayer(model);
        bluePlayer.addStrategy(maxRowStrategy);
        break;
      default:
        break;
    }

    ModelColor redModelColor;
    ModelColor blueModelColor;
    if (args[4].equals("hc")) {
      redModelColor = new HighContrastColor();
    } else {
      redModelColor = new OriginalColor();
    }
    if (args[5].equals("hc")) {
      blueModelColor = new HighContrastColor();
    } else {
      blueModelColor = new OriginalColor();
    }

    BoardPanel redBoard = new NewBoardPanel(model, redModelColor);
    HandPanel redHand = new HandPanel(model.getRedHand(), Player.Red);
    PawnsFrame redFrame = new PawnsFrame(redBoard, redHand, model);
    redPlayer.addView(redFrame);

    BoardPanel blueBoard = new NewBoardPanel(model, blueModelColor);
    HandPanel blueHand = new HandPanel(model.getBlueHand(), Player.Blue);
    PawnsFrame blueFrame = new PawnsFrame(blueBoard, blueHand, model);
    bluePlayer.addView(blueFrame);

    PawnsBoardVisualController redController =
            new PawnsBoardVisualController(model, redFrame, redPlayer, redModelColor);
    PawnsBoardVisualController blueController =
            new PawnsBoardVisualController(model, blueFrame, bluePlayer, blueModelColor);

    model.addModelListener(redController);
    model.addModelListener(blueController);


    redController.setOpponent(blueController);
    blueController.setOpponent(redController);

    redController.playGame();
  }
}

