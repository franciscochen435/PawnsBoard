package cs3500.pawnsboard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cs3500.pawnsboard.controller.DeckReader;
import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.NewBoard;
import cs3500.pawnsboard.model.PawnsBoardCard;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;
import cs3500.pawnsboard.strategy.FillFirst;
import cs3500.pawnsboard.strategy.MaxRowScore;
import cs3500.pawnsboard.view.NewTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test if strategies work correctly with new board model that implements
 * new influence types: upgrade & devalue.
 */
public class Level1Test {
  String path1;
  String path2;
  List<PawnsBoardCard> redDeck = new ArrayList<>();
  List<PawnsBoardCard> blueDeck = new ArrayList<>();
  PawnsBoardInterface board;
  ReadonlyPawnsBoardModel readonlyBoard;
  FillFirst strategy;
  MaxRowScore strategy2;
  NewTextualView view;

  @Before
  public void startGame() throws IOException {
    this.path1 = "docs" + File.separator + "newDeck.config";
    this.path2 = "docs" + File.separator + "newDeck.config";
    this.redDeck = DeckReader.readDeck(path1, Player.Red);
    this.blueDeck = DeckReader.readDeck(path2, Player.Blue);
    this.board = new NewBoard(new Board(5, 7, new Random()));
    this.strategy = new FillFirst();
    this.strategy2 = new MaxRowScore();
    this.view = new NewTextualView(board);
  }

  @Test
  public void fillFirstEmptyBoard() {
    board.startGame(redDeck, blueDeck, false, 5);
    this.readonlyBoard = board.copy();
    assertEquals(
            Arrays.toString(new int[]{0, 0, 0}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));

    board.placeCard(0, 0, 0);
  }

  @Test
  public void maxRowScoreEmptyBoard() {
    board.startGame(redDeck, blueDeck, false, 5);
    System.out.println(view);
    this.readonlyBoard = board.copy();
    assertEquals(
            Arrays.toString(new int[]{0, 0, 0}),
            Arrays.toString(strategy2.chooseMove(readonlyBoard)));

    board.placeCard(0, 0, 0);
  }

  @Test
  public void testFillFirstRedPlayer() {
    board.startGame(redDeck, blueDeck, false, 5);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 0}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 0);

    board.drawCard();
    board.placeCard(1, 0, 6);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 1}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 1);
    board.drawCard();
    board.placeCard(0, 0, 5);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 2}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 2);

    board.drawCard();
    board.placeCard(0, 1, 6);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 4}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 4);

    board.drawCard();
    board.placeCard(1, 1, 5);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 1, 0}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 1, 0);
  }

  @Test
  public void testFillFirstBluePlayer() {
    board.startGame(redDeck, blueDeck, false, 5);
    board.drawCard();
    board.placeCard(0, 0, 0);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 6}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 6);

    board.drawCard();
    board.placeCard(0, 1, 0);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 5}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 5);

    board.drawCard();
    board.placeCard(0, 2, 0);

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 4}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 4);

    board.drawCard();
    board.passTurn();

    System.out.println(view);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[]{0, 0, 2}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 2);
  }

  @Test
  public void maxRowScoreWinLosingRow() {
    board.startGame(redDeck, redDeck, false, 5);
    board.drawCard();
    board.placeCard(4, 3, 0);

    board.drawCard();
    board.placeCard(0, 0, 6);

    board.drawCard();
    board.placeCard(2, 2, 0);

    board.drawCard();
    board.placeCard(0, 1, 6);

    board.drawCard();
    board.placeCard(4, 4, 0);

    System.out.println(view);
    this.readonlyBoard = board.copy();
    assertEquals(
            Arrays.toString(new int[]{0, 2, 6}),
            Arrays.toString(strategy2.chooseMove(readonlyBoard)));
  }

  @Test
  public void maxRowScorePass() {
    board.startGame(redDeck, redDeck, false, 5);
    board.drawCard();
    board.placeCard(0, 0, 0);

    board.drawCard();
    board.placeCard(0, 1, 6);

    board.drawCard();
    board.placeCard(0, 2, 0);

    board.drawCard();
    board.placeCard(0, 3, 6);

    board.drawCard();
    board.placeCard(2, 4, 0);

    System.out.println(view);
    this.readonlyBoard = board.copy();
    assertNull(strategy2.chooseMove(readonlyBoard));
  }

  @Test
  public void maxRowScoreWinningAllRows() {
    board.startGame(redDeck, blueDeck, false, 5);
    board.drawCard();
    board.placeCard(0, 0, 0);

    board.passTurn();

    board.drawCard();
    board.placeCard(1, 1, 0);

    board.passTurn();

    board.drawCard();
    board.placeCard(4, 4, 0);


    board.passTurn();

    board.drawCard();
    board.placeCard(0, 2, 0);

    System.out.println(view);
    board.passTurn();

    board.drawCard();
    board.placeCard(3, 3, 0);

    board.passTurn();

    System.out.println(view);
    this.readonlyBoard = board.copy();
    assertNull(strategy2.chooseMove(readonlyBoard));
  }

  @Test
  public void testDevalue() {
    board.startGame(redDeck, redDeck, false, 5);
    board.drawCard();
    board.placeCard(2, 2,0);

    assertEquals("0 [1R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "0 [2R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "1 [Red1] [____] [1R--] [____] [____] [____] [1B--] 0\n" +
            "0 [1R-1] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "Red: [A 1 2, A 1 2, B 1 1, C 1 2, C 1 2, D 1 1]\n" +
            "Blue: [A 1 2, A 1 2, B 1 1, B 1 1, C 1 2]", view.toString());
    board.passTurn();
    board.drawCard();

    board.placeCard(2,1,0);
    assertEquals("0 [2R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "1 [Red1] [____] [1R--] [____] [____] [____] [1B--] 0\n" +
            "0 [1R--] [____] [1R--] [____] [____] [____] [1B--] 0\n" +
            "0 [1R-1] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "Red: [A 1 2, A 1 2, C 1 2, C 1 2, D 1 1, D 1 1, E 3 3]\n" +
            "Blue: [A 1 2, A 1 2, B 1 1, B 1 1, C 1 2, C 1 2]", view.toString());
  }

  @Test
  public void testUpgrade() {
    board.startGame(redDeck, redDeck, false, 5);
    board.passTurn();
    board.drawCard();

    board.placeCard(0,2,6);
    assertEquals("0 [1R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [_+1_] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [1B--] [Blu2] 2\n" +
            "0 [1R--] [____] [____] [____] [____] [____] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [____] [1B--] 0\n" +
            "Red: [A 1 2, A 1 2, B 1 1, B 1 1, C 1 2, C 1 2]\n" +
            "Blue: [A 1 2, B 1 1, B 1 1, C 1 2, C 1 2, D 1 1]", view.toString());

    board.passTurn();
    board.drawCard();
    board.placeCard(3, 2,5);
    assertEquals("0 [1R--] [____] [____] [____] [____] [_+1_] [1B--] 0\n" +
            "0 [1R--] [____] [____] [____] [1B--] [____] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [Blu2] [Blu2] 4\n" +
            "0 [1R--] [____] [____] [____] [____] [_+1_] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [_-1_] [1B--] 0\n" +
            "Red: [A 1 2, A 1 2, B 1 1, B 1 1, C 1 2, C 1 2, D 1 1]\n" +
            "Blue: [A 1 2, B 1 1, B 1 1, C 1 2, D 1 1, D 1 1, E 3 3]", view.toString());

    board.passTurn();
    board.drawCard();
    board.placeCard(1, 1, 6);
    assertEquals("0 [1R--] [____] [____] [____] [____] [_+1_] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [2B--] [____] [Blu1] 1\n" +
            "0 [1R--] [____] [____] [____] [____] [Blu2] [Blu1] 3\n" +
            "0 [1R--] [____] [____] [____] [____] [_+1_] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [____] [_-1_] [1B--] 0\n" +
            "Red: [A 1 2, A 1 2, B 1 1, B 1 1, C 1 2, C 1 2, D 1 1, D 1 1]\n" +
            "Blue: [A 1 2, B 1 1, C 1 2, D 1 1, D 1 1, E 3 3, E 3 3, F 1 1]", view.toString());

    board.passTurn();
    board.drawCard();
    board.placeCard(2, 3,6);
    assertEquals("0 [1R--] [____] [____] [____] [____] [_+1_] [2B--] 0\n" +
            "0 [1R--] [____] [____] [____] [2B--] [____] [Blu2] 2\n" +
            "0 [1R--] [____] [____] [____] [____] [Blu2] [1B--] 2\n" +
            "0 [1R--] [____] [____] [____] [____] [_+1_] [Blu2] 2\n" +
            "0 [1R--] [____] [____] [____] [____] [_-1_] [1B+1] 0\n" +
            "Red: [A 1 2, A 1 2, B 1 1, B 1 1, C 1 2, C 1 2, D 1 1, D 1 1, E 3 3]\n" +
            "Blue: [A 1 2, B 1 1, D 1 1, D 1 1, E 3 3, E 3 3, F 1 1, F 1 1, G 2 2]",
            view.toString());
  }

}
