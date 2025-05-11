package cs3500.pawnsboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import cs3500.pawnsboard.controller.DeckReader;
import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.NewBoard;
import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.view.NewTextualView;
import cs3500.pawnsboard.view.PawnsBoardView;

/**
 * The main entry point for the PawnsBoard game.
 * This class initializes the game, reads the deck configurations,
 * and manages user input for playing the game.
 */
public class PawnsBoard {
  /**
   * The main method to start and run the PawnsBoard game.
   * It reads the deck configurations, initializes the board,
   * and continuously takes user input until the game is over.
   * The output of this class is textual view.
   * @param args command-line arguments
   * @throws IOException if an error occurs while reading the deck configuration file
   */
  public static void main(String[] args) throws IOException {
    Readable rd = new InputStreamReader(System.in);
    Scanner sc = new Scanner(rd);
    String pathRed = "docs" + File.separator + "newDeck.config";
    String pathBlue = "docs" + File.separator + "newDeck.config";
    List<PawnsBoardCard> redDeck = DeckReader.readDeck(pathRed, Player.Red);
    List<PawnsBoardCard> blueDeck = DeckReader.readDeck(pathBlue, Player.Blue);

    PawnsBoardInterface board = new NewBoard(new Board(3, 5, new Random()));
    board.startGame(redDeck, blueDeck, false,5);
    board.drawCard();
    PawnsBoardView view = new NewTextualView(board);

    while (!board.isGameOver()) {
      System.out.println("Turn: " + board.getTurn());
      System.out.println(view);
      board.getHand();
      System.out.println("Would you like to pass or place: ");
      String input = sc.next();

      switch (input.toLowerCase()) {
        case "pass" :
          board.passTurn();
          board.drawCard();
          break;
        case "place":
          System.out.print("Hand Index: ");
          int handIndex = Integer.parseInt(sc.next());
          System.out.print("Row: ");
          int row = Integer.parseInt(sc.next());
          System.out.print("Column: ");
          int col = Integer.parseInt(sc.next());
          try {
            board.placeCard(handIndex, row, col);
          } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
          }
          board.drawCard();
          break;
        default:
          System.out.println("Invalid Input");
          break;
      }
      System.out.println("----------------------");
    }
    System.out.println("Game Over");
    System.out.println(view);
    if (board.getWinner() == Player.Red) {
      System.out.println("Red won!");
    } else if (board.getWinner() == Player.Blue) {
      System.out.println("Blue won!");
    } else {
      System.out.println("Draw");
    }
  }
}
