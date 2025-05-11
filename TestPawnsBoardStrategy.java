package cs3500.pawnsboard.strategy;

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
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.model.ReadonlyPawnsBoardModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test if two strategies work correctly in the pawns board.
 */
public class TestPawnsBoardStrategy {
  String path1;
  String path2;
  List<Card> redDeck = new ArrayList<>();
  List<Card> blueDeck = new ArrayList<>();
  PawnsBoardInterface board;
  ReadonlyPawnsBoardModel readonlyBoard;
  FillFirst strategy;
  MaxRowScore strategy2;

  @Before
  public void startGame() throws IOException {
    this.path1 = "docs" + File.separator + "redDeck.config";
    this.path2 = "docs" + File.separator + "blueDeck.config";
    this.redDeck = DeckReader.readDeck(path1, Player.Red);
    this.blueDeck = DeckReader.readDeck(path2, Player.Blue);
    this.board = new Board(5, 7, new Random());
    this.strategy = new FillFirst();
    this.strategy2 = new MaxRowScore();
  }

  @Test
  public void fillFirstEmptyBoard() {
    board.startGame(redDeck, blueDeck, false, 5);
    this.readonlyBoard = board.copy();
    assertEquals(
            Arrays.toString(new int[]{0,0,0}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
  }

  @Test
  public void maxRowScoreEmptyBoard() {
    board.startGame(redDeck, blueDeck, false, 5);
    this.readonlyBoard = board.copy();
    assertEquals(
            Arrays.toString(new int[]{0,0,0}),
            Arrays.toString(strategy2.chooseMove(readonlyBoard)));
  }

  @Test
  public void testFillFirstRedPlayer() {
    board.startGame(redDeck, blueDeck, false, 5);
    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {0,0,0}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 0);

    board.drawCard();
    board.placeCard(1, 0, 6);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {0,0,1}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0,0, 1);
    board.drawCard();
    board.placeCard(0, 0, 5);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {0,1,0}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 1, 0);

    board.drawCard();
    board.placeCard(0, 1, 6);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {1,0,2}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(1, 0, 2);

    board.drawCard();
    board.placeCard(1, 1, 5);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {1,0,3}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(1, 0, 3);
  }

  @Test
  public void testFillFirstBluePlayer() {
    board.startGame(redDeck, blueDeck, false, 5);
    board.drawCard();
    board.placeCard(0,0,0);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {0,0,6}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 6);

    board.drawCard();
    board.placeCard(0, 1, 0);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {0,0,5}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 0, 5);

    board.drawCard();
    board.placeCard(0, 2, 0);

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {0, 1, 6}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(0, 1, 6);

    board.drawCard();
    board.passTurn();

    board.drawCard();
    this.readonlyBoard = board.copy();
    assertEquals(Arrays.toString(new int[] {1, 0, 4}),
            Arrays.toString(strategy.chooseMove(readonlyBoard)));
    board.placeCard(1, 0, 4);
  }

  @Test
  public void maxRowScoreWinLosingRow() {
    board.startGame(redDeck, redDeck, false, 5);
    board.drawCard();
    board.placeCard(4, 3, 0);

    board.drawCard();
    board.placeCard(0,0,6);

    board.drawCard();
    board.placeCard(2,2,0);

    board.drawCard();
    board.placeCard(0,1,6 );

    board.drawCard();
    board.placeCard(4, 4, 0);

    this.readonlyBoard = board.copy();
    assertEquals(
            Arrays.toString(new int[]{2,2,6}),
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

    this.readonlyBoard = board.copy();
    assertNull(strategy2.chooseMove(readonlyBoard));
  }

  @Test
  public void maxRowScoreWinningAllRows() {
    board.startGame(redDeck, blueDeck, false, 5);
    board.drawCard();
    board.placeCard(0,0,0);

    board.passTurn();

    board.drawCard();
    board.placeCard(1,1,0);

    board.passTurn();

    board.drawCard();
    board.placeCard(4,4,0);

    board.passTurn();

    board.drawCard();
    board.placeCard(4,3,1);

    board.passTurn();

    board.drawCard();
    board.placeCard(4,2,0);

    board.passTurn();

    this.readonlyBoard = board.copy();
    assertNull(strategy2.chooseMove(readonlyBoard));
  }
}
