package cs3500.pawnsboard.player;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs3500.pawnsboard.controller.DeckReader;
import cs3500.pawnsboard.model.Board;
import cs3500.pawnsboard.model.Card;
import cs3500.pawnsboard.model.PawnsBoardInterface;
import cs3500.pawnsboard.model.Player;
import cs3500.pawnsboard.strategy.FillFirst;
import cs3500.pawnsboard.strategy.MaxRowScore;
import cs3500.pawnsboard.view.MockPawnsFrame;
import cs3500.pawnsboard.view.PawnsBoardTextualView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the machine and human player. Making sure it can place and pass properly.
 */
public class PlayerTest {
  String path1;
  String path2;
  List<Card> redDeck = new ArrayList<>();
  List<Card> blueDeck = new ArrayList<>();
  PawnsBoardInterface board;
  PawnsBoardTextualView textView;
  PawnsBoardPlayer player;
  MockPawnsFrame mock;
  FillFirst fillFirst;
  MaxRowScore maxRowScore;
  StringBuilder log;

  @Before
  public void setUp() throws IOException {
    this.log = new StringBuilder();
    this.path1 = "docs" + File.separator + "redDeck.config";
    this.path2 = "docs" + File.separator + "blueDeck.config";
    this.redDeck = DeckReader.readDeck(path1, Player.Red);
    this.blueDeck = DeckReader.readDeck(path2, Player.Blue);
    this.board = new Board(5, 7, new Random());
    this.player = new MachinePlayer(board);
    this.textView = new PawnsBoardTextualView(board);
    this.fillFirst = new FillFirst();
    this.maxRowScore = new MaxRowScore();
    board.startGame(redDeck, blueDeck, false, 4);
  }

  @Test
  public void testConstructorHuman() {
    assertThrows(IllegalArgumentException.class, () -> {
      player = new HumanPlayer(null);
    });
  }

  @Test
  public void testPlaceCardHuman() {
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    player = new HumanPlayer(board);
    mock = new MockPawnsFrame(log, 0, new int[]{3, 0}, Player.Red);
    player.addView(mock);
    board = player.placeCard();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 2_____1 0\n" +
            "2 R1____1 0\n" +
            "0 2_____1 0\n" +
            "Red: [Security 1 2, Lobber 2 1, Lobber 2 1, Mandragora 1 2]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    board.passTurn();
    mock = new MockPawnsFrame(log, 3, new int[]{3, 1}, Player.Red);
    player.addView(mock);
    board = player.placeCard();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 21____1 0\n" +
            "4 RR11__1 0\n" +
            "0 2_____1 0\n" +
            "Red: [Security 1 2, Lobber 2 1, Lobber 2 1, Mandragora 1 2]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    mock = new MockPawnsFrame(log, 1, new int[]{0, 6}, Player.Blue);
    player.addView(mock);
    board = player.placeCard();
    assertEquals("0 1____1B 2\n" +
            "0 1_____2 0\n" +
            "0 21____1 0\n" +
            "4 RR11__1 0\n" +
            "0 2_____1 0\n" +
            "Red: [Security 1 2, Lobber 2 1, Lobber 2 1, Mandragora 1 2]\n" +
            "Blue: [Security 1 2, Lobber 2 1, Lobber 2 1, Mandragora 1 2]", textView.toString());
  }


  @Test
  public void testPassTurnHuman() {
    //passing turn will not draw cards, it is tied to the visual controller
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    player = new HumanPlayer(board);
    mock = new MockPawnsFrame(log, 0, new int[]{3, 0}, Player.Red);
    player.addView(mock);
    board = player.passTurn();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    board.passTurn();
    mock = new MockPawnsFrame(log, 3, new int[]{3, 1}, Player.Red);
    player.addView(mock);
    board = player.passTurn();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    mock = new MockPawnsFrame(log, 1, new int[]{0, 6}, Player.Blue);
    player.addView(mock);
    board = player.passTurn();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());
  }

  @Test
  public void testAutoHuman() {
    player = new HumanPlayer(board);
    assertFalse(player.isAuto());
  }

  @Test
  public void testConstructorMachine() {
    assertThrows(IllegalArgumentException.class, () -> {
      player = new MachinePlayer(null);
    });
  }

  @Test
  public void testPlaceCardMachine() {
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    player.addStrategy(fillFirst);
    board.drawCard();
    board = player.placeCard();
    assertEquals("2 R1____1 0\n" +
            "0 2_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Lobber 2 1, Lobber 2 1, Mandragora 1 2]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    board.passTurn();
    player.addStrategy(maxRowScore);
    board.drawCard();
    board = player.placeCard();
    assertEquals("2 R1____1 0\n" +
            "2 R1____1 0\n" +
            "0 2_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Lobber 2 1, Lobber 2 1, Mandragora 1 2, Mandragora 1 2]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());
  }

  @Test
  public void testPassTurnMachine() {
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    player.addStrategy(fillFirst);
    board = player.passTurn();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());

    player.addStrategy(maxRowScore);
    board = player.passTurn();
    assertEquals("0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "0 1_____1 0\n" +
            "Red: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]\n" +
            "Blue: [Security 1 2, Security 1 2, Lobber 2 1, Lobber 2 1]", textView.toString());
  }

  @Test
  public void testAutoMachine() {
    assertTrue(player.isAuto());

    player.addStrategy(fillFirst);
    assertTrue(player.isAuto());

    player.addStrategy(maxRowScore);
    assertTrue(player.isAuto());
  }
}
